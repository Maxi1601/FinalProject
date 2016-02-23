package com.yifat.finalproject.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPlaceImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    //region Properties
    private Callbacks callbacks;
    private String errorMessage = null;
    private URL url;
    //endregion

    //region Constructor
    public GetPlaceImageAsyncTask(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    //endregion

    // Runs on the UI thread before doInBackground(Params...)
    protected void onPreExecute() {
        callbacks.onAboutToStart(this);
    }

    // Overriding this method to perform a computation on a background thread
    protected Bitmap doInBackground(String... params) {

        Bitmap bitmap = null;

        try {
            bitmap = downloadImage(params[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }

        return bitmap;

    }

    // Downloding Place's image
    private Bitmap downloadImage(String strUrl) throws IOException {

        Bitmap bitmap = null;
        InputStream iStream = null;

        try {

            url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(iStream);

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
        }

        return bitmap;

    }

    // Runs on the UI thread after doInBackground(Params...)
    protected void onPostExecute(Bitmap bitmap) {
        if (errorMessage != null) {
            callbacks.onError(this, errorMessage);
        } else {
            callbacks.onSuccess(this, bitmap);
        }
    }

    public URL getUrl() {
        return url;
    }

    //region Interface
    // Callbacks design pattern:
    public interface Callbacks {
        void onAboutToStart(GetPlaceImageAsyncTask task);
        void onSuccess(GetPlaceImageAsyncTask task, Bitmap bitmap);
        void onError(GetPlaceImageAsyncTask task, String errorMessage);
    }
    //endregion

}
