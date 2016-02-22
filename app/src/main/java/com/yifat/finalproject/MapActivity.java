package com.yifat.finalproject;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yifat.finalproject.Helpers.Constants;

public class MapActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private double lat;
    private double lng;
    private Toolbar toolbar;

    // Called when the activity is starting. This is where most initialization should go:
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            fragment = new MapFragment();
        }

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        lat = intent.getDoubleExtra(Constants.LATITUDE, 0.0);
        lng = intent.getDoubleExtra(Constants.LONGITUDE, 0.0);

        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.LATITUDE, lat);
        bundle.putDouble(Constants.LONGITUDE, lng);
        fragment.setArguments(bundle);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutContainer, fragment)
                .addToBackStack(null)
                .commit();
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

            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            case R.id.action_search:
                break;

            case R.id.action_favorites:
                intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                break;

            case R.id.km:
                break;

            case R.id.miles:
                break;

            case R.id.deleteAllFavorites:
                MainActivity.sharedFavoritesLogic.deleteAllFavorites(null);
                Toast.makeText(this, "All Favorites have been deleted", Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    // Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for the activity to start interacting with the user
    protected void onResume() {
        super.onResume();

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

    protected void onSaveInstanceState(Bundle outState) {

        getSupportFragmentManager().putFragment(outState, "fragment", fragment);

        super.onSaveInstanceState(outState);
    }

}
