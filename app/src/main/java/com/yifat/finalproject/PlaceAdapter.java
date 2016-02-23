package com.yifat.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifat.finalproject.Helpers.Types;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> implements PlaceHolder.Callbacks {

    //region Properties
    private Activity activity;
    private ArrayList<Place> places;
    private PlaceHolder.Callbacks callbacks;
    private ArrayList<PlaceHolder> holders = new ArrayList<>();
    public Types.DistanceFormat distanceFormat = Types.DistanceFormat.METER;
    //endregion

    //region Constructor
    //  Creating Constructor for PlaceAdapter:
    public PlaceAdapter(Activity activity, ArrayList<Place> places, PlaceHolder.Callbacks callbacks) {
        this.activity = activity;
        this.places = places;
        this.callbacks = callbacks;
    }
    //endregion

    public void setDistanceFormat(Types.DistanceFormat distanceFormat) {
        if (this.distanceFormat == distanceFormat) {
            return;
        }
        this.distanceFormat = distanceFormat;
        notifyDataSetChanged();
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linearLayout = layoutInflater.inflate(R.layout.item_place, null);
        PlaceHolder placeHolder = new PlaceHolder(linearLayout, this);
        holders.add(placeHolder);

        return placeHolder;

    }

    // Called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(PlaceHolder holder, int position) {

        Place place = places.get(position);
        holder.bindPlace(place, this.distanceFormat);

    }

    // Returns the total number of items in the data set hold by the adapter
    public int getItemCount() {

        if (places == null) {
            return 0;
        }

        return places.size();

    }

    //region PlaceHolder's Callbacks implementation
    // Called when a place is clicked
    public void onClick(Place place) {
        callbacks.onClick(place);
    }

    // Called when a place is long clicked
    public void onLongClick(View view, Place place) {
        callbacks.onLongClick(view, place);
    }
    //endregion

}
