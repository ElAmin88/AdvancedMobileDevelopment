package com.example.lap.lecture_4.classes;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import com.example.lap.lecture_4.EventActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by lap on 3/23/2018.
 */

public class CalendarProvider {
    private Context cntx;
    public CalendarProvider(Context context)
    {
        cntx =context;
    }

    public void AddEvent(String d, String m, String y, String time, String event) {
        int day = 0, month = 0, year = 0, hour = 0, min = 0;
        try {
            day = Integer.parseInt(d);
            month = getMonthNumber(m);
            year = Integer.parseInt(y);
            if(!time.contains(":"))
            {
                String [] temp = time.split(" ");
                time = temp[0]+":"+"00 "+temp[1];
            }
            String t[] = time.split(":");
            if (t[1].contains("a.m.")) {
                hour = Integer.parseInt(t[0]);
                t[1] = t[1].replace(" a.m.", "");
            }
            else {
                hour = Integer.parseInt(t[0]) + 12;
                t[1] = t[1].replace(" p.m.", "");
            }

            min = Integer.parseInt(t[1]);

        } catch (Exception e) {
        }
        long calendarID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hour, min);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, hour + 1, min);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = cntx.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        Uri uri;

        if (ActivityCompat.checkSelfPermission(cntx, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

    }

    private int getMonthNumber (String month)
    {
        switch (month)
        {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return 0;

        }
    }

    public ArrayList<Event> getAllEvents()
    {
        Cursor cursor;
        ArrayList<Event> events = new ArrayList<Event>();
        if (ActivityCompat.checkSelfPermission(cntx, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return events;
        }
        cursor = cntx.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            if (cursor != null)
            {
                int deletedIDX = cursor.getColumnIndex(CalendarContract.Events.DELETED);
                int idIDX = cursor.getColumnIndex(CalendarContract.Events._ID);
                int titleIDX = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int startIDX = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                long id = cursor.getLong(idIDX);
                String title =  cursor.getString(titleIDX);
                int deletd = cursor.getInt(deletedIDX);
                String start = cursor.getString(startIDX);
                String details = "";
                try {

                    long s = Long.parseLong(start);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(s);
                    details = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+ " "+
                            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+ " "+
                            calendar.get(Calendar.DAY_OF_MONTH)+" "+
                            calendar.get(Calendar.HOUR) +":"+calendar.get(Calendar.MINUTE)+ " "+
                            calendar.get(Calendar.YEAR);


                }catch (Exception e){}
                if (deletd != 1 )
                {
                    Event e = new Event(title, details, id);
                    events.add(e);
                }

            }

        }
        return events;
    }

    public void deleteEventById(long id)
    {
        ContentResolver cr = cntx.getContentResolver();
        ContentValues eventvalues = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        eventvalues.put(CalendarContract.Events.DELETED, 1);
        cr.update(deleteUri, eventvalues, null, null);

        ContentValues calendarvalues = new ContentValues();
        calendarvalues.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        calendarvalues.put(CalendarContract.Calendars.VISIBLE, 1);
        cr.update(ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, 1),
                calendarvalues, null, null);
        int rows = cntx.getContentResolver().delete(deleteUri, null, null);
    }

    public void deleteAllEvents()
    {
        ArrayList<Event> events = getAllEvents();
        for (int i =0 ;i<events.size();i++)
        {
            long eventId = events.get(i).getEventId();
            deleteEventById(eventId);
        }
    }

    public void updateEventName(int eventID, String name)
    {
        ContentResolver cr = cntx.getContentResolver();
        Uri eventUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, name);
        cr.update(eventUri, event, null, null);
    }
}
