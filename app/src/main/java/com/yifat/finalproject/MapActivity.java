package com.yifat.finalproject;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        lat = intent.getDoubleExtra(Constants.LATITUDE, 0.0);
        lng = intent.getDoubleExtra(Constants.LONGITUDE, 0.0);

        fragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.LATITUDE, lat);
        bundle.putDouble(Constants.LONGITUDE, lng);
        fragment.setArguments(bundle);

//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameLayoutContainer, fragment)
//                .addToBackStack(null)
//                .commit();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
