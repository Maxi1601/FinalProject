package com.yifat.finalproject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener, ResultsFragment.Callbacks {

    private String deviceType;
    private LocationManager locationManager;
//    private ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String json = PreferencesHelper.loadPlacesJson(this, Constants.JSON);
        Log.d("Json", json);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                switch (v.getId()) {
//                    case R.id.toolbar_search:
//                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });

        deviceType = (String) findViewById(R.id.linearLayoutRoot).getTag();

        Toast.makeText(this, "Running on " + deviceType, Toast.LENGTH_LONG).show();

        // Creating a resultFragment and setting "this" as the callback:
        ResultsFragment resultsFragment = new ResultsFragment();
        resultsFragment.callbacks = this;

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutContainer, resultsFragment)
                .commit();

        // If the device is tablet - show both fragments:
        if (deviceType.equals("tablet")) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainerMap, new MapFragment())
                    .commit();

        }

        // Get the device service for getting the user location:
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.NETWORK_PROVIDER;
        String provider = LocationManager.GPS_PROVIDER;
        int milliseconds2Update = 5000; // 5 sec
        int meters2Update = 10; // 10 meters

        try {
            locationManager.requestLocationUpdates(provider, milliseconds2Update, meters2Update, this);
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    // Creating OPTIONS MENU:
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // The possible actions of the menu:
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);

    }

    // Called when the location changed (meters or seconds):
    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        PreferencesHelper.saveLatitude(this, Constants.LATITUDE, latitude);
        PreferencesHelper.saveLongitude(this, Constants.LONGITUDE, longitude);

        loadLocation();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void loadLocation (){

        PreferencesHelper.loadLatitude(this, Constants.LATITUDE);
        PreferencesHelper.loadLongitude(this, Constants.LONGITUDE);

        String latitude = String.valueOf(PreferencesHelper.loadLatitude(this, Constants.LATITUDE));
        String longitude = String.valueOf(PreferencesHelper.loadLongitude(this, Constants.LONGITUDE));

        String message = "Location: " + latitude + ", " + longitude;

        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showCoordinates(double lat, double lng) {

        if (deviceType.equals("phone")) {

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra(Constants.LATITUDE, lat);
            intent.putExtra(Constants.LONGITUDE, lng);
            startActivity(intent);

        } else if (deviceType.equals("tablet")) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            MapFragment fragment = (MapFragment)fragmentManager.findFragmentById(R.id.frameLayoutContainerMap);
            fragment.setNewPlace(lat,lng);

        }
    }

}
