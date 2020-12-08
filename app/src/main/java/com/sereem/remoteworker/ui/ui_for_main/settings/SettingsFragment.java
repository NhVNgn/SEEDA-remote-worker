package com.sereem.remoteworker.ui.ui_for_main.settings;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.ui.CustomSnackbar;
import com.sereem.remoteworker.ui.ui_for_main.service.LocationService;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * SettingsFragment class, extends PreferenceFragmentCompat, used for switching gps visibility,
 * customizing app theme, and sending feedback.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    String TAG = "SettingsFragment";
    private FusedLocationProviderClient mFusedLocationClient;
    public static Intent serviceIntent;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        setPreferencesFromResource(R.xml.settings, rootKey);
        SwitchPreference autoTheme = getPreferenceManager().findPreference("auto_dark_theme");
        autoTheme.setOnPreferenceClickListener(this);
        SwitchPreference darkTheme = getPreferenceManager().findPreference("dark_theme");
        darkTheme.setOnPreferenceClickListener(this);
        Preference feedback = getPreferenceManager().findPreference("feedback");
        feedback.setOnPreferenceClickListener(preference -> {
            String supportEmail = "for.iee.2018@gmail.com";
            String subject = "Remote Worker Feedback";
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {supportEmail});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(Intent.createChooser(intent, "Choose an Email client:"));
            return true;
        });

        SwitchPreference gpsVisibility = getPreferenceManager().findPreference("gps_visibility");
        gpsVisibility.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isOn = (boolean) newValue;
            if (isOn){
                Snackbar.make(getView(), "GPS visibility is enabled", Snackbar.LENGTH_SHORT).show();
                SharedPreferences prefs = getContext().getSharedPreferences("user", MODE_PRIVATE);
                prefs.edit().putBoolean("visibility", true).apply();
                checkLocationService();
                prefs.edit().putBoolean("isFirstUpdateAfterOff", true).apply();
            }
            else {
                Snackbar.make(getView(), "GPS visibility is disabled", Snackbar.LENGTH_SHORT).show();
                SharedPreferences prefs = getContext().getSharedPreferences("user", MODE_PRIVATE);
                prefs.edit().putBoolean("visibility", false).apply();
            }
            return true;
        });

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        Snackbar.make(getView(), "Restart the app (will be fixed)", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(requireActivity(), available, 1000);
            dialog.show();
        }else{
            CustomSnackbar.create(getView()).setText("You can't make map requests").show();
        }
        return false;
    }

    private boolean checkMapServices(){
        return isServicesOK();
    }
    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.sereem.remoteworker.ui.ui_for_main.service.LocationService.java".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            serviceIntent = new Intent(requireActivity(), LocationService.class);
            System.out.println("Service intent is created");
            System.out.println("MainActivity: visibility " + getVisibilityPreference());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                if (getVisibilityPreference()){
                    System.out.println("startForegroundService is called");
                    requireActivity().startForegroundService(serviceIntent);
                }
            }else{
                if (getVisibilityPreference()) {
                    System.out.println("MainActivity: startService is called");
                    requireActivity().startService(serviceIntent);
                }
            }
        }
    }


    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "getLastKnownLocation: called WW");
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Log.d(TAG, "onComplete: called.");
            if (task.isSuccessful()) {
                Log.d(TAG, "startLocationService: called.");
                startLocationService();
            }
        });

    }

    public void checkLocationService() {
        if(checkMapServices()){
            System.out.println("CheckMapService is true ");
            getLocationPermission();
            getLastKnownLocation();
        }
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
    }

    private boolean getVisibilityPreference(){
        SharedPreferences prefs = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean("visibility", true);
        System.out.println("visibility: " + value);
        return prefs.getBoolean("visibility", true);
    }

}