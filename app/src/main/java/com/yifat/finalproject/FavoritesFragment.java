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

public class FavoritesFragment extends Fragment implements PlaceHolder.Callbacks, PopupHelper.FavoritesHelperCallback{

    private FavoritesLogic favoritesLogic;
    private ArrayList<Place> allFavorites;
    public Callbacks callbacks;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoritesLogic = MainActivity.sharedFavoritesLogic;

        allFavorites = favoritesLogic.getAllFavorites();

        updateList(allFavorites);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) getActivity();
    }

    @Override
    public void onClick(Place place) {

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
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {

    }

    @Override
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {
        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);
    }

    // Callbacks design pattern:
    public interface Callbacks {
        void didClick(FavoritesFragment fragment, Place place);
        void didLongClick(FavoritesFragment fragment,View pressedView, Place place);
    }

    private void updateList(ArrayList<Place> places) {

        // Create a linear list of items:

        PlaceAdapter adapter = new PlaceAdapter(getActivity(), places, this);
        RecyclerView recyclerViewFavorites = (RecyclerView) getActivity().findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFavorites.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void removeFavorite () {
        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);
    }

    public void removeAllFavorites () {

        Log.d("FavoritesFragment", "removeAllFavorites");
        MainActivity.sharedFavoritesLogic.deleteAllFavorites(null);
        allFavorites = null;

//        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);
    }

    public void changeDistanceFormat (Types.DistanceFormat format) {
        RecyclerView recyclerViewFavorites = (RecyclerView) getActivity().findViewById(R.id.recyclerViewFavorites);
        if (recyclerViewFavorites == null) {
            return;
        }
        PlaceAdapter placeAdapter = (PlaceAdapter) recyclerViewFavorites.getAdapter();
        placeAdapter.setDistanceFormat(format);
    }

}
