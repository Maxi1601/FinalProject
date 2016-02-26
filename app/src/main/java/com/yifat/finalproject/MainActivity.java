package com.yifat.finalproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yifat.finalproject.DataBase.FavoritesLogic;
import com.yifat.finalproject.Helpers.Constants;
import com.yifat.finalproject.Helpers.PopupHelper;
import com.yifat.finalproject.Helpers.PreferencesHelper;
import com.yifat.finalproject.Helpers.Types;

public class MainActivity extends AppCompatActivity implements LocationListener, ResultsFragment.Callbacks, PopupHelper.FavoritesHelperCallback {

    //region Properties
    private String deviceType;
    private LocationManager locationManager;
    public static FavoritesLogic sharedFavoritesLogic;
    private Toolbar toolbar;
    private ResultsFragment resultsFragment;
    private MapFragment mapFragment;
    //endregion

    //region onCreate
    // Called when the activity is starting. This is where most initialization should go:
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            resultsFragment = (ResultsFragment) getFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            resultsFragment = new ResultsFragment();
            resultsFragment.callbacks = this;

        }

        // Set a toolbar to replace the action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String json = PreferencesHelper.loadLastResult(this, Constants.JSON);
        Log.d("Json", json);

        MainActivity.sharedFavoritesLogic = new FavoritesLogic(this);

        deviceType = (String) findViewById(R.id.linearLayoutRoot).getTag();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutContainer, resultsFragment)
                .commit();

        // If the device is tablet - show both fragments:
        if (deviceType.equals("tablet")) {

            if (savedInstanceState != null) {
                mapFragment = (MapFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mapFragment");
            } else {
                mapFragment = new MapFragment();
            }

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainerMap, mapFragment)
                    .commit();

        }

        // Get the device service for getting the user location:
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        int milliseconds2Update = 5000; // 5 sec
        int meters2Update = 10; // 10 meters

        try {
            locationManager.requestLocationUpdates(provider, milliseconds2Update, meters2Update, this);
        } catch (SecurityException e) {
            Log.e("locationManager", "SecurityException " + e.toString());
        }

    }
    //endregion

    //region Options Menu
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

            case R.id.deleteAllFavorites:
                MainActivity.sharedFavoritesLogic.deleteAllFavorites(null);
                Toast.makeText(this, "All Favorites have been deleted", Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    //endregion

    // Called when the location changed (meters or seconds)
    public void onLocationChanged(Location location) {

        if (location == null) {
            return;
        }
        resultsFragment.setLocation(location);
        locationManager.removeUpdates(this);

    }

    // Called when the provider status changes
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    // Called when the provider is enabled by the user
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, provider + " has been enabled", Toast.LENGTH_LONG).show();
    }

    // Called when the provider is disabled by the user
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, provider + " has been disabled", Toast.LENGTH_LONG).show();
    }

    // Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for the activity to start interacting with the user
    protected void onResume() {
        super.onResume();

        if (sharedFavoritesLogic == null) {
            return;
        }

        MainActivity.sharedFavoritesLogic.open();

        // Enable BroadcastReceiver when the activity resumes:
        ComponentName component = new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
    }

    // Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed
    protected void onPause() {
        super.onPause();

        // Disable BroadcastReceiver when the activity pauses:
        ComponentName component = new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
    }

    // Called to retrieve per-instance state before the activity is killed so that the state can be restored in onCreate(Bundle)
    protected void onSaveInstanceState(Bundle outState) {

        getFragmentManager().putFragment(outState, "fragment", resultsFragment);
        if (mapFragment != null) {
            getSupportFragmentManager().putFragment(outState, "mapFragment", mapFragment);
        }

        super.onSaveInstanceState(outState);

    }

    //region ResultsFragment's Callbacks implementation
    // Called when a place is clicked
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

    // Called when a place is long clicked
    public void didLongClick(ResultsFragment fragment, View view, Place place) {

        if (place == null) {
            return;
        }
        PopupHelper popupHelper = new PopupHelper(this, view, place);
        popupHelper.callback = this;

        popupHelper.showPopup();

    }
    //endregion

    //region PopupHelper's FavoritesHelperCallback implementation
    // Called when a places is added to favorites
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

    // Called when a place is removed from favorites
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }
    //endregion

}

