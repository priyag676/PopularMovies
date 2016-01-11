package com.example.priya.mymoviesapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priya.mymoviesapp.BuildConfig;
import com.example.priya.mymoviesapp.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Priya on 1/10/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();
    private final Context mContext;
    public FetchTrailerTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            String movieId = params[0];
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.My_Themoviedb_Api_Key)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "url : "+url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
            Utility.getTrailerJsonList(mContext, movieId, jsonStr);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
}

