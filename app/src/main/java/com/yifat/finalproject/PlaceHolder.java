package com.yifat.finalproject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yifat on 2/1/16.
 */
public class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Activity activity;
    private LinearLayout linearLayout;
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewDistance;
    private Callbacks callbacks;
    private Place place;


    public PlaceHolder(View linearLayout, Callbacks callbacks) {
        super(linearLayout);
        this.linearLayout = (LinearLayout)linearLayout;

        // itemView is the root element of each item's layout
        // (in our case is the LinearLayout of the item_place.xml)
        textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
        textViewDistance = (TextView) itemView.findViewById(R.id.textViewDistance);
        this.callbacks = callbacks;

        linearLayout.setOnClickListener(this);

    }

    // Enter the object data into the ui:
    public void bindPlace(Place place) {

        this.place = place;
        textViewName.setText(place.getName());
        //TODO: Complete
        textViewAddress.setText(place.getAddress());
        textViewDistance.setText("");
    }

    @Override
    public void onClick(View v) {
        callbacks.onClick(place);
    }

    public interface Callbacks {
        void onClick(Place place);
    }
}
