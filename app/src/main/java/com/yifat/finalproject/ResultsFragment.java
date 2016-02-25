package com.yifat.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
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

    //region Properties
    public Callbacks callbacks;
    private ArrayList<Place> places;
    private Location location;
    private ProgressDialog dialog;
    //endregion

    //region Constructor
    // Required empty public constructor
    public ResultsFragment() {
    }
    //endregion

    // Setting the location of the user and executing asynctask accordingly
    public void setLocation(Location location) {

        Log.d("ResultsFragment", "setLocation");
        this.location = location;
        if (this.location == null) {
            return;
        }

        if (this.places != null) {
            return;
        }
        try {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=500&key=AIzaSyCECLHBTRBDH4mPV-PSeVi7FCT0xhd34XA");
            Log.d("ResultFragment", url.toString());
            PlacesNearByAsyncTask placesNearByAsyncTask = new PlacesNearByAsyncTask(this);
            placesNearByAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            Log.e("setLocation", "url error " + e.toString());
        }

    }

    // Called when a fragment is first attached to its activity
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) getActivity();
    }

    // Called to have the fragment instantiate its UI view:
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        return view;
    }

    // Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            return;
        }

        if (savedInstanceState != null) {
            Log.d("OnActivityCreated", "savedInstanceState not null");
            places = savedInstanceState.getParcelableArrayList(Constants.STATE_PLACES);
            updateList(places);
            return;
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Searching for places...");
        dialog.setMessage("Please Wait");
        dialog.show();
        dialog.setCancelable(false);

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

    }

    // Parsing the result and creating the arrayList of places
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
            Log.e("ResultsFragment", "Error parsing json " + e.toString());
        }

        return null;
    }

    // Called when the fragment is no longer attached to its activity
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    // Put the updated arrayList in the RecyclerView
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
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPlaces.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // Calculating the distance between the user's location and a place's Location
    public double calculateDistance(double lat, double lng) {

        Location userLocation = this.location;
        if (userLocation == null) {
            return -0;
        }

        Location placeLocation = new Location("PlaceLocation");

        placeLocation.setLatitude(lat);
        placeLocation.setLongitude(lng);

        double distance = userLocation.distanceTo(placeLocation);

        return distance;

    }

    // Managing change in distance format - meters/feet
    public void changeDistanceFormat(Types.DistanceFormat format) {

        RecyclerView recyclerViewPlaces = (RecyclerView) getActivity().findViewById(R.id.recyclerViewPlaces);
        PlaceAdapter placeAdapter = (PlaceAdapter) recyclerViewPlaces.getAdapter();
        placeAdapter.setDistanceFormat(format);

    }

    // Executing asynctask according to text entered by the user
    public void searchByTerm(String term) {

        if (this.location == null) {
            return;
        }

        if (term == null || term.length() == 0) {
            // reset the search to previous location
            this.setLocation(this.location);
            return;
        }
        try {
            String latitude = String.valueOf(this.location.getLatitude());
            String longitude = String.valueOf(this.location.getLongitude());
            String URLString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=500&name=" + term + "&key=AIzaSyCECLHBTRBDH4mPV-PSeVi7FCT0xhd34XA";
            URL url = new URL(URLString);
            Log.d("SearchByTerm", term);
            PlacesNearByAsyncTask placesNearByAsyncTask = new PlacesNearByAsyncTask(this);
            placesNearByAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            Log.e("searchByTerm", "url error " + e.toString());
        }

    }

    // Called to retrieve per-instance state before the fragment is killed so that the state can be restored in onActivityCreated(Bundle)
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(Constants.STATE_PLACES, places);

    }

    //region PlaceHolder's Callbacks implementation
    // Called when a place is clicked
    public void onClick(Place place) {

        if (callbacks != null) {
            callbacks.didClick(this, place);
        }

    }

    // Called when a place is long clicked
    public void onLongClick(View view, Place place) {

        if (callbacks != null) {
            callbacks.didLongClick(this, view, place);
        }

    }
    //endregion

    //region PlacesNearByAsyncTask's Callbacks implementation
    // Called when onPreExecute is invoked
    public void onAboutToStart(PlacesNearByAsyncTask task) {
    }

    // Called when onPostExecute is invoked (if errorMassage is null)
    public void onSuccess(PlacesNearByAsyncTask task, String result) {

        if (dialog != null){
            dialog.dismiss();
        }

        if (result != null) {
            updateList(parseJson(result));
            PreferencesHelper.savePlacesJson(getActivity(), Constants.JSON, result);
        }

    }

    // Called when onPostExecute is invoked (if errorMassage isn't null)
    public void onError(PlacesNearByAsyncTask task, String errorMessage) {
        Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
    }
    //endregion

    //region Interface
    // Callbacks design pattern:
    public interface Callbacks {
        void didClick(ResultsFragment fragment, Place place);
        void didLongClick(ResultsFragment fragment, View pressedView, Place place);
    }
    //endregion

}
