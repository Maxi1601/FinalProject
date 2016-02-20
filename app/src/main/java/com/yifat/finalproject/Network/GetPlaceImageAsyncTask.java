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

    private Callbacks callbacks;
    private String errorMessage = null;
    private URL url;

    public GetPlaceImageAsyncTask(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    protected void onPreExecute() {
        callbacks.onAboutToStart(this);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bitmap = null;
        try {
            // Starting image download
            bitmap = downloadImage(params[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return bitmap;
    }

    private Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap = null;
        InputStream iStream = null;
        try {
            url = new URL(strUrl);

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (errorMessage != null) {
            callbacks.onError(this, errorMessage);
        } else {
            callbacks.onSuccess(this, bitmap);
        }
    }

    public interface Callbacks {
        void onAboutToStart(GetPlaceImageAsyncTask task);
        void onSuccess(GetPlaceImageAsyncTask task, Bitmap bitmap);
        void onError(GetPlaceImageAsyncTask task, String errorMessage);
    }

    public URL getUrl() {
        return url;
    }

}
