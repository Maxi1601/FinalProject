package com.yifat.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yifat.finalproject.DataBase.FavoritesLogic;
import com.yifat.finalproject.Helpers.PopupHelper;
import com.yifat.finalproject.Helpers.PreferencesHelper;
import com.yifat.finalproject.Helpers.Types;

public class MainActivity extends AppCompatActivity implements LocationListener, ResultsFragment.Callbacks, PopupHelper.FavoritesHelperCallback {

    private String deviceType;
    private LocationManager locationManager;
    public static FavoritesLogic sharedFavoritesLogic;
    Toolbar toolbar;
    ResultsFragment resultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // passing in null because otherwise crashes when rotating the device
        //TODO: Need to understand how to handle rotation with out deallocating all of the current fragmenets
        super.onCreate(null);


        setContentView(R.layout.activity_main);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String json = PreferencesHelper.loadPlacesJson(this, Constants.JSON);
        Log.d("Json", json);

        MainActivity.sharedFavoritesLogic = new FavoritesLogic(this);

        deviceType = (String) findViewById(R.id.linearLayoutRoot).getTag();

        Toast.makeText(this, "Running on " + deviceType, Toast.LENGTH_LONG).show();

        // Creating a resultFragment and setting "this" as the callback:
        resultsFragment = new ResultsFragment();
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.NETWORK_PROVIDER;
        String provider = LocationManager.GPS_PROVIDER;
        int milliseconds2Update = 5000; // 5 sec
        int meters2Update = 10; // 10 meters

        try {
            locationManager.requestLocationUpdates(provider, milliseconds2Update, meters2Update, this);
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (deviceType != "phone") {
            return;
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout ly = (LinearLayout)findViewById(R.id.linearLayoutRoot);

        } else  {

        }
    }

    // Creating OPTIONS MENU:
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // The possible actions of the menu:
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                EditText editTextSearch = (EditText) toolbar.findViewById(R.id.editTextSearch);
                String term = editTextSearch.getText().toString();
                resultsFragment.searchByTerm(term);
                break;

            case R.id.action_favorites:
                intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                break;

            case R.id.km:
                Types.DistanceFormat format = Types.DistanceFormat.METER;
                resultsFragment.changeDistanceFormat(format);
                break;

            case R.id.miles:
                resultsFragment.changeDistanceFormat(Types.DistanceFormat.FEET);
                break;

            case R.id.delete:
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.sharedFavoritesLogic.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        favoritesLogic.close();
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

    public void loadLocation() {

        PreferencesHelper.loadLatitude(this, Constants.LATITUDE);
        PreferencesHelper.loadLongitude(this, Constants.LONGITUDE);

        String latitude = String.valueOf(PreferencesHelper.loadLatitude(this, Constants.LATITUDE));
        String longitude = String.valueOf(PreferencesHelper.loadLongitude(this, Constants.LONGITUDE));

        String message = "Location: " + latitude + ", " + longitude;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void didClick(ResultsFragment fragment, Place place) {

        if (deviceType.equals("phone")) {

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra(Constants.LATITUDE, place.getLat());
            intent.putExtra(Constants.LONGITUDE, place.getLng());
            startActivity(intent);

        } else if (deviceType.equals("tablet")) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.frameLayoutContainerMap);
            mapFragment.setNewPlace(place);

        }
    }

    @Override
    public void didLongClick(ResultsFragment fragment, View view, Place place) {

        if (place == null) {
            return;
        }
        PopupHelper popupHelper = new PopupHelper(this, view, place);
        popupHelper.callback = this;

        popupHelper.showPopup();

    }

    @Override
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

    @Override
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

}
