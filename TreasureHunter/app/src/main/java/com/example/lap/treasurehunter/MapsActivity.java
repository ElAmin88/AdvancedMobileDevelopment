package com.example.lap.treasurehunter;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final List<LatLng> points_locationA = Arrays.asList(
            new LatLng(30.063707, 31.278252),
            new LatLng(30.063607, 31.278290),
            new LatLng(30.063652, 31.278413),
            new LatLng(30.063754, 31.278367));

    private static final List<LatLng> points_locationB = Arrays.asList(
            new LatLng(30.063247, 31.278421),
            new LatLng(30.063125, 31.278488),
            new LatLng(30.063198, 31.278724),
            new LatLng(30.063312, 31.278663));

    private static final List<LatLng> points_locationHome = Arrays.asList(
            new LatLng(30.060163, 31.514919),
            new LatLng(30.059761, 31.514891),
            new LatLng(30.059795, 31.515378),
            new LatLng(30.060143, 31.515475));

    private MarkerOptions mLocationA_MarkerOption, mLocationB_MarkerOption, mLocationHome_MarkerOption;

    private Polygon locationA, locationB, locationHome;
    private Marker MarkerA, MarkerB, MarkerHome;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String REQUEST_LOCATION_UPDATE = "location_update";
    private static final String KEY_SCORE = "score";
    private static final String KEY_TIME = "time";
    private static final String KEY_NEAREST ="nearest";
    private static final String KEY_REMOVED_LOCATIONS ="removedLocations";

    // UI components
    private Timer mTimer;
    private TextView mTxt_Score, mTxt_Time, mTxt_NearsrLocation;
    private int mTime, mScore;
    private String mNearstLocation;
    private ArrayList<Polygon> removedPolygons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUEST_LOCATION_UPDATE);
            mTime = savedInstanceState.getInt(KEY_TIME);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mNearstLocation = savedInstanceState.getString(KEY_NEAREST);
            removedPolygons = (ArrayList<Polygon>) savedInstanceState.getSerializable(KEY_REMOVED_LOCATIONS);
            for (int i=0; i<removedPolygons.size();i++) {
                if(removedPolygons.get(i) != null) removedPolygons.get(i).remove();
            }

        }
        else
        {
            mTime = 0;
            mScore = 0;
            removedPolygons = new ArrayList<Polygon>();
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        mTxt_Score = (TextView)findViewById(R.id.lbl_score);
        mTxt_Time = (TextView)findViewById(R.id.lbl_time);
        mTxt_NearsrLocation = (TextView)findViewById(R.id.lbl_location);
        mTimer = new Timer();
        mRequestingLocationUpdates = false;
        createLocationRequest();

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Excuted when Location of User Changes
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (final Location location : locationResult.getLocations()) {
                    mLastKnownLocation = location;
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            LatLng position = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            if(inPolygon(position,locationHome)) {
                                locationHome.remove();
                                locationHome = null;
                                MarkerHome.remove();
                                Toast.makeText(getApplicationContext(), "Found Tresure in Location Home", Toast.LENGTH_SHORT).show();
                                mScore++;
                                removedPolygons.add(locationHome);
                            }
                            else if(inPolygon(position,locationA)) {
                                locationA.remove();
                                locationA = null;
                                MarkerA.remove();
                                Toast.makeText(getApplicationContext(), "Found Tresure in Location A", Toast.LENGTH_SHORT).show();
                                mScore++;
                                removedPolygons.add(locationA);
                            }
                            else if(inPolygon(position,locationB)) {
                                locationB.remove();
                                locationB = null;
                                MarkerB.remove();
                                Toast.makeText(getApplicationContext(), "Found Tresure in Location B", Toast.LENGTH_SHORT).show();
                                mScore++;
                                removedPolygons.add(locationB);
                            }
                        }
                    }, 500);
                }
            }

        };
        updateUI();
        startTimer();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            outState.putSerializable(KEY_REMOVED_LOCATIONS,removedPolygons);
            outState.putBoolean(REQUEST_LOCATION_UPDATE, mRequestingLocationUpdates);
            outState.putInt(KEY_SCORE, mScore);
            outState.putInt(KEY_TIME, mTime);
            outState.putString(KEY_NEAREST, mNearstLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationA = googleMap.addPolygon(new PolygonOptions()
                .addAll(points_locationA).strokeColor(Color.RED)
                );
        locationA.setTag("Location A");

        mLocationA_MarkerOption = new MarkerOptions()
                .position(getPolygonCenterPoint(points_locationA))
                .title("Location A")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icn_treasure));
        MarkerA = mMap.addMarker(mLocationA_MarkerOption);

        locationB = googleMap.addPolygon(new PolygonOptions()
                .addAll(points_locationB).strokeColor(Color.RED)
        );
        locationB.setTag("Location B");

        mLocationB_MarkerOption = new MarkerOptions()
                .position(getPolygonCenterPoint(points_locationB))
                .title("Location B")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icn_treasure));
        MarkerB = mMap.addMarker(mLocationB_MarkerOption);

        locationHome = googleMap.addPolygon(new PolygonOptions()
                .addAll(points_locationHome).strokeColor(Color.RED)
        );
        locationHome.setTag("Location Home");
        mLocationHome_MarkerOption = new MarkerOptions()
                .position(getPolygonCenterPoint(points_locationHome))
                .title("Location Home")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icn_treasure));
        MarkerHome = mMap.addMarker(mLocationHome_MarkerOption);

        for (int i=0; i<removedPolygons.size();i++) {
            if(removedPolygons.get(i) != null) removedPolygons.get(i).remove();
        }

        // Prompt the user for permission.
        getLocationPermission();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private boolean inPolygon(LatLng point, Polygon polygon) {
        if (polygon == null)
            return false;
        List<LatLng> polyPointsList = polygon.getPoints();
        if (PolyUtil.containsLocation(point, polyPointsList, false))
            return true;
        return false;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    private LatLng getPolygonCenterPoint(List<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }

    private double distance(LatLng point1, LatLng point2) {
        double lat1 = point1.latitude;
        double lon1 = point1.longitude;
        double lat2 = point2.latitude;
        double lon2 = point2.longitude;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private String getNearestLocation(Location currentLocation)
    {
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        double minLocationA = 999;
        for(int i = 0; i<points_locationA.size();i++){
            double distance = distance(currentLatLng,points_locationA.get(i));
            if(distance < minLocationA) minLocationA = distance;
        }

        double minLocationB = 999;
        for(int i = 0; i<points_locationB.size();i++){
            double distance = distance(currentLatLng,points_locationB.get(i));
            if(distance < minLocationB) minLocationA = distance;
        }

        if (minLocationA < minLocationB) return " Location A";
        else return "Location B";

    }

    private void updateUI()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mLastKnownLocation != null) mNearstLocation = getNearestLocation(mLastKnownLocation);
                if(mTxt_Score != null) mTxt_Score.setText(getResources().getString(R.string.score)+mScore);
                if(mTxt_Time != null) mTxt_Time.setText(getResources().getString(R.string.time)+mTime);
                if(mTxt_NearsrLocation != null) mTxt_NearsrLocation.setText(getResources().getString(R.string.nearest)+mNearstLocation);
                if(mScore > 1) {
                    Toast.makeText(getApplicationContext(),"YOU WIN !!!!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

    }

    private void startTimer()
    {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTime++;
                updateUI();

            }
        },0,1000);
    }


    private void stopTimer()
    {
        mTimer.cancel();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
            startTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        stopTimer();
    }



}
