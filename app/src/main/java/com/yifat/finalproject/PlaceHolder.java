package com.yifat.finalproject;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yifat.finalproject.Helpers.Types;
import com.yifat.finalproject.Network.GetPlaceImageAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

public class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, GetPlaceImageAsyncTask.Callbacks {

    //region Properties
    private RelativeLayout relativeLayout;
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewDistance;
    private ImageView imageViewPlace;
    private Callbacks callbacks;
    private Place place;
    //endregion

    //region Constructor
    public PlaceHolder(View relativeLayout, Callbacks callbacks) {
        super(relativeLayout);
        this.relativeLayout = (RelativeLayout)relativeLayout;

        // itemView is the root element of each item's layout
        // (in our case is the RelativeLayout of the item_place.xml)
        textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
        textViewDistance = (TextView) itemView.findViewById(R.id.textViewDistance);
        imageViewPlace = (ImageView) itemView.findViewById(R.id.imageViewPlace);
        this.callbacks = callbacks;

        relativeLayout.setOnClickListener(this);
        relativeLayout.setOnLongClickListener(this);

    }
    //endregion

    private void bindPlace(Place place) {
        bindPlace(place, Types.DistanceFormat.METER);
    }
    // Enter the object data into the ui:
    public void bindPlace(Place place, Types.DistanceFormat format) {

        this.place = place;
        textViewName.setText(place.getName());
        textViewAddress.setText(place.getAddress());

        // Default Measure unit is meter
        double distance = place.getDistance();
        String units = "m";

        if (format == Types.DistanceFormat.FEET) {
            // Convert meters to feet
            distance = distance * 3.28084;
            units = "ft";
        }
        textViewDistance.setText(String.format("%.1f", distance) + " " + units);

        this.imageViewPlace.setImageBitmap(null);

        if (place.placeImage != null) {
            this.imageViewPlace.setImageBitmap(place.placeImage);
        }
        else {
            // Put this specific image from recourse
            this.imageViewPlace.setImageResource(R.drawable.no_image);

            // Download image from the internet
            callImageAsyncTask();
        }

    }

    @Override
    public void onClick(View v) {
        callbacks.onClick(place);
    }

    @Override
    public boolean onLongClick(View v) {
        callbacks.onLongClick(v, place);
        return true;
    }

    //region Asynctask GetPlaceImage
    @Override
    public void onAboutToStart(GetPlaceImageAsyncTask task) {

    }

    @Override
    public void onSuccess(GetPlaceImageAsyncTask task, Bitmap bitmap) {

        // Was a previous Asynctask
        if (!task.getUrl().toString().equals(place.getUrl())) {
            Log.d("PlaceHolder", task.getUrl().toString());
            return;
        }
        this.place.placeImage = bitmap;
        imageViewPlace.setImageBitmap(bitmap);
    }

    @Override
    public void onError(GetPlaceImageAsyncTask task, String errorMessage) {

    }
    //endregion

    public interface Callbacks {
        void onClick(Place place);
        void onLongClick (View view, Place place);
    }

    public void callImageAsyncTask () {

        URL url = null;
        try {
            url = new URL(place.getUrl());

            String stringUrl = url.toString();

            GetPlaceImageAsyncTask getPlaceImageAsyncTask = new GetPlaceImageAsyncTask(this);
            getPlaceImageAsyncTask.execute(stringUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

}
