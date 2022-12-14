package com.sereem.remoteworker.ui.ui_for_main.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.UserLocation;
import com.sereem.remoteworker.ui.ErrorDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * LocationService class, extends Service, used for sending user's locations to the server.
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 2 * 1000;  /* 2 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */
    private static final int MIN_TIME = 2000;
    private static final int MIN_DISTANCE = 1;
    public final static String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    public final static String EXTRA_LATITUDE = "extra_latitude";
    public final static String EXTRA_LONGITUDE = "extra_longitude";
    public final static String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private User user;
    private UserLocation userLocation;
    public static boolean SHUT_DOWN = false;
    private DocumentReference documentReference;
    private DatabaseReference databaseReference;
    private boolean isFirstUpdateAfterTurnOff;

    private final LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "onLocationResult: got location result.");

            Location location = locationResult.getLastLocation();

            if (location != null) {
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                Date date = Calendar.getInstance().getTime();
                userLocation = new UserLocation(geoPoint.getLatitude() + ", "
                        + geoPoint.getLongitude(), date.toString());
                saveUserLocation();
            }
        };

    };;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("LocationService called onCreate");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            String action = intent.getAction();
            if (action != null && action.equals(ACTION_STOP_FOREGROUND_SERVICE)){
                System.out.println("CALLING stopForeGroound and stopSelf");
                SHUT_DOWN = true;
                stopForeground(true);
                stopSelf();
            }
        }
        Log.d(TAG, "onStartCommand: called.");
        getLocation();
        return START_NOT_STICKY;
    }

    private void getLocation() {
        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sendBroadCastMessage(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                sendBroadCastMessage(location);
            }
        });


        Log.d(TAG, "getLocation: getting location information.");
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, mLocationCallback, Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void saveUserLocation(){
        try{
            updateGPSDataInDatabase();
        }catch (NullPointerException e){
            Log.e(TAG, "saveUserLocation: User instance is null, stopping location service.");
            Log.e(TAG, "saveUserLocation: NullPointerException: "  + e.getMessage() );
            stopSelf();
        }

    }

    private void initializeDocumentReference() {
        user = User.getInstance();
        SharedPreferences prefs = this.getSharedPreferences("user", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userLocations")
                .child(user.getUID());
    }

    private void updateGPSDataInDatabase() {
        if (SHUT_DOWN)
        {

            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            stopForeground(true);
            stopSelf();
            return;
        }

        initializeDocumentReferenceForUser();
        initializeDocumentReference();


        if (!getVisibilityPreference())
        {
            if(!getFirstUpdatePreferences()) {
                return;
            } else {
                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                prefs.edit().putBoolean("isFirstUpdateAfterOff", false).apply();
            }
            GeoPoint new_geoPoint = new GeoPoint(0.0, 0.0);
            userLocation = new UserLocation(new_geoPoint.getLatitude() + ", "
                    + new_geoPoint.getLongitude(), null);
        }
        databaseReference.setValue(userLocation);
        if (!getVisibilityPreference()){
            System.out.println("removeLocation was called");
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            stopForeground(true);
            stopSelf();
        }
    }

    private void sendBroadCastMessage(Location location){
        if (location != null){
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
    private boolean getVisibilityPreference(){
        SharedPreferences prefs = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean("visibility", true);
        System.out.println("visibility: " + value);
        return prefs.getBoolean("visibility", true);
    }

    private boolean getFirstUpdatePreferences(){
        SharedPreferences prefs = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.getBoolean("isFirstUpdateAfterOff", true);
    }

    private void initializeDocumentReferenceForUser() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String UID = prefs.getString("UID", "");
        System.out.println("UID is" + UID);
        documentReference = FirebaseFirestore.getInstance().document(
                "/users/" + UID + "/");
        documentReference.addSnapshotListener((value, error) -> {
            if(error != null && error.getCode() != FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                ErrorDialog.show(this);
            }
            else if(value != null && value.exists()) {
                Log.d(TAG, "user name in doc:" + user.getFirstName());
                user = User.createNewInstance(value.toObject(User.class));
            }
        });
    }
}