package com.yifat.finalproject;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yifat.finalproject.Helpers.Constants;
import com.yifat.finalproject.Helpers.GeneralHelper;
import com.yifat.finalproject.Helpers.PreferencesHelper;
import com.yifat.finalproject.Helpers.Types;
import com.yifat.finalproject.Network.PlacesNearByAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ResultsFragment extends Fragment implements PlaceHolder.Callbacks, PlacesNearByAsyncTask.Callbacks {

    public Callbacks callbacks;
    private ArrayList<Place> places;

    // Required empty public constructor
    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            return;
        }

        if (savedInstanceState != null) {
            //TODO: Complete
            Log.d("OnActivityCreated", "savedInstanceState not null");
            places = savedInstanceState.getParcelableArrayList(Constants.STATE_PLACES);
            updateList(places);
            return;
//            Log.d("Results", "return");
        }

        // if no internet, try to load the last saved result
//        if (GeneralHelper.isInternetAvailable() == false) {
//            String json = PreferencesHelper.loadPlacesJson(getActivity(), Constants.JSON);
//            Log.d("checkJson", json);
//            if (json == null || json.isEmpty()) {
//                Toast.makeText(getActivity(), "no Internet", Toast.LENGTH_SHORT).show();
//                //TODO: complete what happens
//            } else {
//                updateList(parseJson(json));
//            }
//            return;
//        }

        // we have internet, load from google
        try {
            String latitude = String.valueOf(PreferencesHelper.loadLatitude(getActivity(), Constants.LATITUDE));
            String longitude = String.valueOf(PreferencesHelper.loadLongitude(getActivity(), Constants.LONGITUDE));
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=500&key=AIzaSyCECLHBTRBDH4mPV-PSeVi7FCT0xhd34XA");
            Log.d("ResultFragment", url.toString());
            PlacesNearByAsyncTask placesNearByAsyncTask = new PlacesNearByAsyncTask(this);
            placesNearByAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(Place place) {

//        Toast.makeText(this.getActivity(), place.getName() + ", " + place.getLat() + ", " + place.getLng(), Toast.LENGTH_LONG).show();

        if (callbacks != null) {
            callbacks.didClick(this, place);
        }

    }

    @Override
    public void onLongClick(View view, Place place) {

        if (callbacks != null) {
            callbacks.didLongClick(this, view, place);
        }

    }

    @Override
    public void onAboutToStart(PlacesNearByAsyncTask task) {

    }

    @Override
    public void onSuccess(PlacesNearByAsyncTask task, String result) {

        if (result != null) {
            updateList(parseJson(result));
            PreferencesHelper.savePlacesJson(getActivity(), Constants.JSON, result);
        }

    }

    @Override
    public void onError(PlacesNearByAsyncTask task, String errorMessage) {

    }

    private ArrayList<Place> parseJson(String result) {
        try {

            places = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArrayResults = jsonObject.getJSONArray("results");

            final int numberOfItemsInResp = jsonArrayResults.length();

            for (int i = 0; i < numberOfItemsInResp; i++) {

                JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(i);

                String name = jsonObjectResult.getString("name");
                String address = jsonObjectResult.getString("vicinity");

                JSONObject jsonObjectGeometry = jsonObjectResult.getJSONObject("geometry");
                JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");

                double lat = jsonObjectLocation.getDouble("lat");
                double lng = jsonObjectLocation.getDouble("lng");

                double distance = calculateDistance(lat, lng);

                String photoReference = null;

                if (jsonObjectResult.has("photos")) {

                    JSONArray jsonArrayPhotos = jsonObjectResult.getJSONArray("photos");

                    if (jsonArrayPhotos != null && jsonArrayPhotos.length() > 0) {

                        JSONObject jsonObjectPhotos = jsonArrayPhotos.getJSONObject(0);

                        photoReference = jsonObjectPhotos.getString("photo_reference");

                        photoReference = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + photoReference + "&key=AIzaSyCECLHBTRBDH4mPV-PSeVi7FCT0xhd34XA";
                        Log.d("ParseJson", photoReference);
                    }

                }

                places.add(new Place(name, address, distance, photoReference, lat, lng));

            }

            return places;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Callbacks design pattern:
    public interface Callbacks {
        void didClick(ResultsFragment fragment, Place place);

        void didLongClick(ResultsFragment fragment, View pressedView, Place place);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private void updateList(ArrayList<Place> places) {
        if (places == null) {
            return;
        }
        PlaceAdapter adapter = new PlaceAdapter(getActivity(), places, this);
        if (getActivity() == null) {
            return;
        }
        RecyclerView recyclerViewPlaces = (RecyclerView) getActivity().findViewById(R.id.recyclerViewPlaces);
        if (recyclerViewPlaces == null) {
            return;
        }
        // Create a linear list of items:
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPlaces.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public double calculateDistance(double lat, double lng) {

        Location userLocation = new Location("point A");

        userLocation.setLatitude(PreferencesHelper.loadLatitude(getActivity(), Constants.LATITUDE));
        userLocation.setLongitude(PreferencesHelper.loadLongitude(getActivity(), Constants.LONGITUDE));

        Location placeLocation = new Location("point B");

        placeLocation.setLatitude(lat);
        placeLocation.setLongitude(lng);

        double distance = userLocation.distanceTo(placeLocation);

        return distance;

    }

    public void searchByTerm(String term) {

        try {
            //TODO: Remove the hardcoded coordinates
            // DON'T want to deal with the GPS giving crap location
            // NOT SOMETHING I CAN CONTROL - SO NO POINT DEAL
            // TLV coordinates are 32.0852999,34.78176759999997
            String latitude = "32.0852999";//String.valueOf(PreferencesHelper.loadLatitude(getActivity(), Constants.LATITUDE));
            String longitude = "34.78176759999997";//String.valueOf(PreferencesHelper.loadLongitude(getActivity(), Constants.LONGITUDE));
            String URLString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=500&name=" + term + "&key=AIzaSyCECLHBTRBDH4mPV-PSeVi7FCT0xhd34XA";
            URL url = new URL(URLString);
            Log.d("SearchByTerm", term);
            PlacesNearByAsyncTask placesNearByAsyncTask = new PlacesNearByAsyncTask(this);
            placesNearByAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void changeDistanceFormat(Types.DistanceFormat format) {
        RecyclerView recyclerViewPlaces = (RecyclerView) getActivity().findViewById(R.id.recyclerViewPlaces);
        PlaceAdapter placeAdapter = (PlaceAdapter) recyclerViewPlaces.getAdapter();
        placeAdapter.setDistanceFormat(format);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: Complete
        outState.putParcelableArrayList(Constants.STATE_PLACES, places);
    }

}
