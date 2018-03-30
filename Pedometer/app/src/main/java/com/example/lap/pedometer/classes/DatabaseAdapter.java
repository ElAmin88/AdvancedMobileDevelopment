package com.example.lap.pedometer.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lap on 3/30/2018.
 */

public class DatabaseAdapter {

    // Logcat tag
    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Pedometer";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RUNRECORDS = "runrecords";

    // Common column names
    private static final String KEY_ID = "id";
    public static final int COL_ID = 0;

    // USERS Table - column nmaes
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_WEIGHT = "weight";
    private static final String KEY_USER_STEP_LENGTH = "step_length";

    public static final String[] USER_KEYS = new String[] {KEY_ID, KEY_USER_NAME, KEY_USER_PASSWORD, KEY_USER_STEP_LENGTH, KEY_USER_WEIGHT};

    // RUNRECORDS Table - column names
    private static final String KEY_RUNRECORDS_USERID = "user_id";
    private static final String KEY_RUNRECORDS_STEPS = "steps";
    private static final String KEY_RUNRECORDS_CALORIES = "calories";
    private static final String KEY_RUNRECORDS_DISTANCE = "distance";
    private static final String KEY_RUNRECORDS_TIME = "time";
    private static final String KEY_RUNRECORDS_SPEED = "speed";
    private static final String KEY_RUNRECORDS_DURATION = "duration";

    public static final String[] RUNRECORDS_KEYS = new String[] {KEY_ID, KEY_RUNRECORDS_USERID, KEY_RUNRECORDS_STEPS,
                                                                KEY_RUNRECORDS_CALORIES, KEY_RUNRECORDS_DISTANCE,
                                                                KEY_RUNRECORDS_TIME, KEY_RUNRECORDS_SPEED, KEY_RUNRECORDS_DURATION};


    //USERS Table Create Statement
    private static final String USERS_CREATE_SQL =
            "create table " + TABLE_USERS
                    + " (" + KEY_ID         + " integer primary key autoincrement, "
                    + KEY_USER_NAME         + " text not null, "
                    + KEY_USER_PASSWORD     + " text not null, "
                    + KEY_USER_WEIGHT       + " real not null, "
                    + KEY_USER_STEP_LENGTH  + " real not null, "
                    + ");";

    //RUNRECORDS Table Create Statement
    private static final String RUNRECORDS_CREATE_SQL =
            "create table " + TABLE_RUNRECORDS
                    + " (" + KEY_ID                 + " integer primary key autoincrement, "
                    + KEY_RUNRECORDS_USERID         + " integer not null, "
                    + KEY_RUNRECORDS_STEPS          + " integer not null, "
                    + KEY_RUNRECORDS_CALORIES       + " real not null, "
                    + KEY_RUNRECORDS_DISTANCE       + " real not null, "
                    + KEY_RUNRECORDS_TIME           + " text not null, "
                    + KEY_RUNRECORDS_SPEED          + " real not null, "
                    + KEY_RUNRECORDS_DURATION       + " real not null, "
                    + ");";

    // Context of application who uses us.
    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    //Open the database connection.
    public DatabaseAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    //Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    //Adding a new User to the database.
    public long AddUser(User user) {

        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_NAME, user.getName());
        initialValues.put(KEY_USER_PASSWORD, user.getPassword());
        initialValues.put(KEY_USER_WEIGHT, user.getWeight());
        initialValues.put(KEY_USER_STEP_LENGTH, user.getStepLegth());

        // Insert it into the database.
        return db.insert(TABLE_USERS, null, initialValues);
    }

    //Getting User by Id
    public User getUserByID(int id) {
        User user = null;
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_ID + " = " + id;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            c.moveToFirst();

            String name = c.getString(c.getColumnIndex(KEY_USER_NAME));
            String password = c.getString(c.getColumnIndex(KEY_USER_PASSWORD));
            float weight = c.getFloat(c.getColumnIndex(KEY_USER_PASSWORD));
            float step_length = c.getFloat(c.getColumnIndex(KEY_USER_PASSWORD));

            user = new User();
            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            user.setStepLegth(step_length);
            user.setWeight(weight);
        }

        return user;
    }

    //Getting RunRecord of a User by Id
    public ArrayList<RunRecord> getRunRecordsofUser(int user_id) {
        ArrayList<RunRecord> records = new ArrayList<RunRecord>();
        String selectQuery = "SELECT  * FROM " + TABLE_RUNRECORDS + " WHERE "
                + KEY_RUNRECORDS_USERID + " = " + user_id;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.getCount() > 0){
            if (c.moveToFirst()) {
                do {
                    RunRecord record = new RunRecord();
                    int id = c.getInt((c.getColumnIndex(KEY_ID)));
                    int numSteps = c.getInt((c.getColumnIndex(KEY_RUNRECORDS_STEPS)));
                    float numCalories = c.getInt((c.getColumnIndex(KEY_RUNRECORDS_CALORIES)));
                    float distance = c.getInt((c.getColumnIndex(KEY_RUNRECORDS_DISTANCE)));
                    float duration = c.getInt((c.getColumnIndex(KEY_RUNRECORDS_DURATION)));
                    float speed = c.getInt((c.getColumnIndex(KEY_RUNRECORDS_SPEED)));
                    String time = c.getString((c.getColumnIndex(KEY_RUNRECORDS_TIME)));

                    record.setDistance(distance);
                    record.setDuration(duration);
                    record.setId(id);
                    record.setNumCalories(numCalories);
                    record.setNumSteps(numSteps);
                    record.setSpeed(speed);
                    record.setTime(time);
                    record.setUserId(user_id);

                    records.add(record);

                } while (c.moveToNext());
            }
        }
        return records;
    }

    //Deleting User by ID
    public void deleteUserByID(int user_id) {
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user_id) });

        deleteRunRecord(user_id);
    }

    //Deleting RunRecords of a specifi User
    public void deleteAllRunRecords(int user_id) {
        ArrayList<RunRecord> records = getRunRecordsofUser(user_id);
        for (int i = 0; i < records.size(); i++)
            deleteRunRecord(records.get(i).getId());
    }

    //Adding RunRecord to the database
    public long AddRunRecord(RunRecord record) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RUNRECORDS_STEPS, record.getNumSteps());
        initialValues.put(KEY_RUNRECORDS_CALORIES, record.getNumCalories());
        initialValues.put(KEY_RUNRECORDS_SPEED, record.getSpeed());
        initialValues.put(KEY_RUNRECORDS_DISTANCE, record.getDistance());
        initialValues.put(KEY_RUNRECORDS_TIME, record.getTime());
        initialValues.put(KEY_RUNRECORDS_USERID, record.getUserId());

        // Insert it into the database.
        return db.insert(TABLE_USERS, null, initialValues);
    }

    //Deleting RunRecord by Id
    public void deleteRunRecord(int id) {
        db.delete(TABLE_RUNRECORDS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(USERS_CREATE_SQL);
            _db.execSQL(RUNRECORDS_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNRECORDS);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
