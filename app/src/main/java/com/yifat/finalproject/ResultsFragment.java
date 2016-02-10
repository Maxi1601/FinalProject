package com.yifat.finalproject;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ResultsFragment extends Fragment implements PlaceHolder.Callbacks, PlacesNearByAsyncTask.Callbacks {

    public Callbacks callbacks;

    // Required empty public constructor
    public ResultsFragment() {
    }

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        try {
//            callbacks = (Callbacks) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement Callbacks");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        // Inflate the layout for this fragment
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

        // if no internet, try to load the last saved result
        if (GeneralHelper.isInternetAvailable() == false) {
            String json = PreferencesHelper.loadPlacesJson(getActivity(), Constants.JSON);
            Log.d("checkJson", json);
            if (json == null || json.isEmpty()) {
                Toast.makeText(getActivity(), "no Internet", Toast.LENGTH_SHORT).show();
                //TODO: complete what happens
            } else {
                updateList(parseJson(json));
            }
        }

        // we have internet, load from google
        try {
            String latitude = String.valueOf(PreferencesHelper.loadLatitude(getActivity(), Constants.LATITUDE));
            String longitude = String.valueOf(PreferencesHelper.loadLongitude(getActivity(), Constants.LONGITUDE));
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=500&key=AIzaSyAUJBJanhO8bYq8Bkmi0Y-K-mC_BWkSfUs");
            PlacesNearByAsyncTask placesNearByAsyncTask = new PlacesNearByAsyncTask(this);
            placesNearByAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(Place place) {
        Toast.makeText(this.getActivity(), place.getName() + ", " + place.getLat() + ", " + place.getLng(), Toast.LENGTH_LONG).show();

//        Activity activity = getActivity();
//
//        if (activity instanceof CoordinatesCallbacks) {
//            Callbacks callbacks = (Callbacks) activity;
//            callbacks.showCoordinates(place.getLat(), place.getLng());
//        }

        Log.d("Try", "here");
        if (callbacks != null) {
            callbacks.showCoordinates(place.getLat(), place.getLng());
        }

    }

    @Override
    public void onAboutToStart() {

    }

    @Override
    public void onSuccess(String result) {

        if (result != null) {
            PreferencesHelper.savePlacesJson(getActivity(), Constants.JSON, result);
            updateList(parseJson(result));
        }

    }

    private ArrayList<Place> parseJson(String result) {
        try {

            ArrayList<Place> places = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArrayResults = jsonObject.getJSONArray("results");

            final int numberOfItemsInResp = jsonArrayResults.length();

            for (int i = 0; i < numberOfItemsInResp; i++) {

                JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(i);

                String name = jsonObjectResult.getString("name");

                JSONObject jsonObjectGeometry = jsonObjectResult.getJSONObject("geometry");
                JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");

                double lat = jsonObjectLocation.getDouble("lat");
                double lng = jsonObjectLocation.getDouble("lng");

                places.add(new Place(name, "", "", lat, lng));

            }

            return places;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onError(String errorMessage) {

    }

    // Callbacks design pattern:
    public interface Callbacks {
        void showCoordinates(double lat, double lng);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private void updateList (ArrayList<Place> places) {
        if (places == null) {
            return;
        }
        PlaceAdapter adapter = new PlaceAdapter(getActivity(), places, this);
        RecyclerView recyclerViewPlaces = (RecyclerView) getActivity().findViewById(R.id.recyclerViewPlaces);
        // Create a linear list of items:
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPlaces.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
