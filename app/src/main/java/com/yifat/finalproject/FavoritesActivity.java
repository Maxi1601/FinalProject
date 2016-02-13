package com.yifat.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yifat.finalproject.DataBase.FavoritesLogic;
import com.yifat.finalproject.Helpers.PopupHelper;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements PlaceHolder.Callbacks, PopupHelper.FavoritesHelperCallback{

    private FavoritesLogic favoritesLogic;
    private ArrayList<Place> allFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

//        favoritesLogic = new FavoritesLogic(this);

        favoritesLogic = MainActivity.sharedFavoritesLogic;

        allFavorites = favoritesLogic.getAllFavorites();

        updateList(allFavorites);

        Intent intent = getIntent();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        favoritesLogic.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        favoritesLogic.close();
    }

    @Override
    public void onClick(Place place) {

    }

    @Override
    public void onLongClick(View view, Place place) {

        if (place == null) {
            return;
        }
        PopupHelper popupHelper = new PopupHelper(this, view, place);
        popupHelper.callback = this;

        popupHelper.showPopup();
    }

    private void updateList(ArrayList<Place> places) {
        if (places == null) {
            return;
        }

        PlaceAdapter adapter = new PlaceAdapter(this, places, this);
        RecyclerView recyclerViewFavorites = (RecyclerView) this.findViewById(R.id.recyclerViewFavorites);
        // Create a linear list of items:
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFavorites.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void didAddFavorite(PopupHelper popupHelper, Place favoritesPlace) {
    }

    @Override
    public void didRemoveFavorite(PopupHelper popupHelper, Place favoritesPlace) {
        allFavorites = favoritesLogic.getAllFavorites();
        updateList(allFavorites);
    }
}
