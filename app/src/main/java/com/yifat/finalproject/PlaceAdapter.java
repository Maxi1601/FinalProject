package com.yifat.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifat.finalproject.Helpers.Types;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> implements PlaceHolder.Callbacks{

    private Activity activity;
    private ArrayList<Place> places;
    private PlaceHolder.Callbacks callbacks;
    private ArrayList<PlaceHolder> holders = new ArrayList<>();
    public Types.DistanceFormat distanceFormat = Types.DistanceFormat.METER;

    //  Creating Constructor for PlaceAdapter:
    public PlaceAdapter(Activity activity, ArrayList<Place> places, PlaceHolder.Callbacks callbacks) {
        this.activity = activity;
        this.places = places;
        this.callbacks = callbacks;
    }

    public void setDistanceFormat(Types.DistanceFormat distanceFormat) {
        if (this.distanceFormat == distanceFormat) {
            return;
        }
        this.distanceFormat = distanceFormat;
        notifyDataSetChanged();
    }

    @Override
    // Will be invoked only for the first shown items!
    // Creates the first x layout items.
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linearLayout = layoutInflater.inflate(R.layout.item_place, null);
//        return new PlaceHolder(activity, linearLayout);

        PlaceHolder placeHolder = new PlaceHolder(linearLayout, this);
        holders.add(placeHolder);
        return placeHolder;

    }

    @Override
    // Will be invoked for each item presented.
    // Here we will set the data to the layout:
    public void onBindViewHolder(PlaceHolder holder, int position) {
        Place place = places.get(position);
        holder.bindPlace(place, this.distanceFormat);

    }

    @Override
    public int getItemCount() {
        if (places == null) {
            return 0;
        }
        return places.size();
    }

    @Override
    public void onClick(Place place) {
        callbacks.onClick(place);
    }

    @Override
    public void onLongClick(View view, Place place) {
        callbacks.onLongClick(view, place);
    }

}
