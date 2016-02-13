package com.yifat.finalproject;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private boolean firstTime = true;
    private SupportMapFragment supportMapFragment;

    // Required empty public constructor
    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap));
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

//         Enable the user tracking
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            // this function will perform when the user location is changed:
            public void onMyLocationChange(Location location) {
                if (!firstTime) {
                    return;
                }
                // Take user location:
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng userLocation = new LatLng(latitude, longitude);

                // Set user location to google maps:
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(userLocation);
                googleMap.clear();
                googleMap.addMarker(markerOptions);

                int zoom = 16;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, zoom);

                googleMap.animateCamera(cameraUpdate);
                firstTime = false;

            }
        });
    }

    public void setNewPlace(Place place) {

        SupportMapFragment supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap));

        // Checking if the xml file is valid:
        if (supportMapFragment instanceof SupportMapFragment) {

            // Send the object for updating the UI:
            GoogleMap googleMap = supportMapFragment.getMap();

            LatLng latLng = new LatLng(place.getLat(), place.getLng());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(place.getName());
            googleMap.clear();
            googleMap.addMarker(markerOptions);

            int zoom = 16;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);

            googleMap.animateCamera(cameraUpdate);

        }
    }

}
