package com.yifat.finalproject.Network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlacesNearByAsyncTask extends AsyncTask<URL, Void, String> {

    //region Properties
    private Callbacks callbacks;
    private String errorMessage = null;
    //endregion

    //region Constructor
    public PlacesNearByAsyncTask(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    //endregion

    // Runs on the UI thread before doInBackground(Params...)
    protected void onPreExecute() {
        callbacks.onAboutToStart(this);
    }

    // Overriding this method to perform a computation on a background thread
    protected String doInBackground(URL... params) {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {

            URL url = params[0];

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int httpStatusCode = connection.getResponseCode();

            if (httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                errorMessage = "Bad Http Request";
                return null;
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                errorMessage = connection.getResponseMessage();
                return null;
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String result = "";
            String oneLine = bufferedReader.readLine();
            while (oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            return null;
        } finally {
            try {
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (Exception e) {
                Log.e("doInBackground", "Error closing inputStreamReader " + e.toString());
            }
        }

    }

    // Runs on the UI thread after doInBackground(Params...)
    protected void onPostExecute(String result) {
        if (errorMessage != null) {
            callbacks.onError(this, errorMessage);
        } else {
            callbacks.onSuccess(this, result);
        }
    }

    //region Interface
    // Callbacks design pattern:
    public interface Callbacks {
        void onAboutToStart(PlacesNearByAsyncTask task);
        void onSuccess(PlacesNearByAsyncTask task, String result);
        void onError(PlacesNearByAsyncTask task, String errorMessage);
    }
    //endregion

}
