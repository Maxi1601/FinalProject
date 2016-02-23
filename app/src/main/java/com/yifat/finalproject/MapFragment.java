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
import com.yifat.finalproject.Helpers.Constants;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //region Properties
    private boolean firstTime = true;
    private Place place;
    //endregion

    //region Construstor
    // Required empty public constructor
    public MapFragment() {
    }
    //endregion

    //region onCreateView
    // Called to have the fragment instantiate its user interface view:
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if (savedInstanceState != null) {
            place = savedInstanceState.getParcelable(Constants.STATE_MAP);
        }

        SupportMapFragment supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap));
        supportMapFragment.getMapAsync(this);

        return view;

    }
    //endregion

    //region OnMapReadyCallback implementation
    // Callback interface for when the map is ready to be used
    public void onMapReady(final GoogleMap googleMap) {

        // Enable the user tracking
        googleMap.setMyLocationEnabled(true);

        if (place != null) {
            googleMap.setMyLocationEnabled(false);
            setNewPlace(place);
        }

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            // this function will perform when the user location is changed:
            public void onMyLocationChange(Location location) {
                if (!firstTime || place != null) {
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
    //endregion

    // Shows a clicked place on the map
    public void setNewPlace(Place place) {

        this.place = place;

        SupportMapFragment supportMapFragment = ((SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap));

        // Checking if the xml file is valid:
        if (supportMapFragment instanceof SupportMapFragment) {

            // Send the object for updating the UI:
            GoogleMap googleMap = supportMapFragment.getMap();
            googleMap.clear();

            if (place == null) {
                googleMap.setMyLocationEnabled(true);
                return;
            }

            LatLng latLng = new LatLng(place.getLat(), place.getLng());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(place.getName());
            googleMap.addMarker(markerOptions);

            int zoom = 16;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);

            googleMap.animateCamera(cameraUpdate);

        }
    }

    // Called to retrieve per-instance state before the fragment is killed so that the state can be restored in onActivityCreated(Bundle)
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.STATE_MAP, place);

    }

}
