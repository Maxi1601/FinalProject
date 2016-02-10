package com.yifat.finalproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yifat on 2/2/16.
 */
public class PlacesNearByAsyncTask extends AsyncTask<URL, Void, String> {

    private Callbacks callbacks;
    private String errorMessage = null;

    public PlacesNearByAsyncTask(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    protected void onPreExecute() {
        callbacks.onAboutToStart();
    }

    @Override
    protected String doInBackground(URL... params) {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {

            URL url = params[0];

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            int httpStatusCode = connection.getResponseCode();

            if(httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                errorMessage = "Figure out";
                return null;
            }
            if(httpStatusCode != HttpURLConnection.HTTP_OK) {
                errorMessage = connection.getResponseMessage();
                return null;
            }

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String result = "";
            String oneLine = bufferedReader.readLine();
            while(oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        }
        catch(Exception ex) {
            errorMessage = ex.getMessage();
            return null;
        }

        finally {
            try {
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (Exception e) {

            }
        }

    }

    @Override
    protected void onPostExecute(String result) {
        if(errorMessage != null) {
            callbacks.onError(errorMessage);
        }
        else {
            callbacks.onSuccess(result);
        }
    }

    public interface Callbacks {
        void onAboutToStart();
        void onSuccess(String result);
        void onError(String errorMessage);
    }
}
