package com.yifat.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifat.finalproject.DataBase.FavoritesLogic;
import com.yifat.finalproject.Helpers.PopupHelper;
import com.yifat.finalproject.Helpers.Types;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements PlaceHolder.Callbacks, PopupHelper.FavoritesHelperCallback {

    //region Properties
    private FavoritesLogic favoritesLogic;
    private ArrayList<Place> allFavorites;
    public Callbacks callbacks;
    //endregion

    //region Constructor
    // Required empty public constructor
    public FavoritesFragment() {
    }
    //endregion

    // Called when a fragment is first attached to its activity
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) getActivity();
    }

    // Called to have the fragment instantiate its UI view:
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        return view;
    }

    // Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoritesLogic = MainActivity.sharedFavoritesLogic;

        allFavorites = favoritesLogic.getAllFavorites();

        updateList(allFavorites);

    }

    // Put the updated arrayList in the RecyclerView
    private void updateList(ArrayList<Place> places) {

        PlaceAdapter adapter = new PlaceAdapter(getActivity(), places, this);
        RecyclerView recyclerViewFavorites = (RecyclerView) getActivity().findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFavorites.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    // Remove one favorite
    public void removeFavorite() {

        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);

    }

    // Remove all favorites
    public void removeAllFavorites() {

        Log.d("FavoritesFragment", "removeAllFavorites");
        MainActivity.sharedFavoritesLogic.deleteAllFavorites(null);
        allFavorites = null;
        updateList(allFavorites);

    }

    // Managing change in distance format - meters/feet
    public void changeDistanceFormat(Types.DistanceFormat format) {

        RecyclerView recyclerViewFavorites = (RecyclerView) getActivity().findViewById(R.id.recyclerViewFavorites);
        if (recyclerViewFavorites == null) {
            return;
        }
        PlaceAdapter placeAdapter = (PlaceAdapter) recyclerViewFavorites.getAdapter();
        placeAdapter.setDistanceFormat(format);

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

    //region PopupHelper's FavoritesHelperCallback implementation
    // Called when a places is added to favorites
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

    // Called when a place is removed from favorites
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {
        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);
    }
    //endregion

    //region Interface
    // Callbacks design pattern:
    public interface Callbacks {
        void didClick(FavoritesFragment fragment, Place place);
        void didLongClick(FavoritesFragment fragment, View pressedView, Place place);
    }
    //endregion

}
