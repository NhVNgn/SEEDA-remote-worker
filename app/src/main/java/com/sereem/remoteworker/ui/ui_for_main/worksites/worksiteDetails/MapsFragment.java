package com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.CustomSnackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * MapsFragment extends SupportMapFragment, implements OnMapReadyCallback, LocationListener.
 * Used by LocationsViewFragment to display google map.
 */
public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener {

    GoogleMap mGoogleMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    SiteDatabase siteDB;
    WorkSite userWorkSite = null;

    public Bitmap createSmallerMarker(int marker){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), marker, null);
        Bitmap b = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            //Request Location Permission
            checkLocationPermission();
        }
        // add marker to location
        siteDB = new SiteDatabase(getContext());
        getSite();
        LatLng site_coordinate = getLocationFromAddress(getContext(), userWorkSite.getLocation());
        if (site_coordinate != null)
        {
            String site_title = userWorkSite.getName();
            String site_snippet = userWorkSite.getLocation();
            MarkerOptions marker = new MarkerOptions().position(site_coordinate).title(site_title).snippet(site_snippet);
            Marker m = mGoogleMap.addMarker(marker);
            m.setIcon(BitmapDescriptorFactory.fromBitmap(createSmallerMarker(R.drawable.construction)));
            m.showInfoWindow();
            float zoomLevel = 16.0f;
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(site_coordinate, zoomLevel));
        }

    }

    public LatLng getLocationFromAddress(Context context, String siteAddress){
        Geocoder coder = new Geocoder(context);
        List<Address> addresses;
        LatLng site_coordinates = null;
        try{
            addresses = coder.getFromLocationName(siteAddress, 5);
            if (addresses == null)
                return null;
            Address location = addresses.get(0);
            site_coordinates = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex){
            ex.printStackTrace();
        }
        if (site_coordinates == null)
        {
            site_coordinates = new LatLng(49.279881, -122.921738);
        }
        return site_coordinates;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //optionally, stop location updates if only current location is needed
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {
                CustomSnackbar.create(getView()).setText("Permission denied").show();
            }
        }
    }

    private void getSite() {
        userWorkSite = WorkSite.getChosenWorksite();
    }


}