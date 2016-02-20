package com.yifat.finalproject;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yifat.finalproject.Helpers.Constants;

public class MapActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private double lat;
    private double lng;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        lat = intent.getDoubleExtra(Constants.LATITUDE, 0.0);
        lng = intent.getDoubleExtra(Constants.LONGITUDE, 0.0);

        fragment = new MapFragment();

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

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.sharedFavoritesLogic.open();

        ComponentName component=new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ComponentName component=new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
    }

}
