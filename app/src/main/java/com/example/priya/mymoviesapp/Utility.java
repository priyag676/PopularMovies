package com.example.priya.mymoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.priya.mymoviesapp.Sync.MovieSyncAdapter;
import com.example.priya.mymoviesapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Priya on 12/30/2015.
 */
public class Utility {
    public static final String LOG = "Log";

    public static String getDefaultSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sortOrder = prefs.getString(
                context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_default_sort_order));
        return sortOrder;
    }
    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    static public @MovieSyncAdapter.SortOrderStatus
    int getLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_sort_order_status_key), MovieSyncAdapter.SORT_ORDER_UNKNOWN);
    }
    public static Void getReviewJsonList(Context mContext, String JsonStr, String movieId) {
        try {
            JSONObject movieJson = new JSONObject(JsonStr);
            // Getting JSON Array node results
            JSONArray results = movieJson.getJSONArray("results");
            int reviewListLength = results.length();
            Log.d(LOG, reviewListLength + " reviews fetched" + movieId);


            ArrayList<ContentValues> cvList = new ArrayList<>(reviewListLength);
            //Vector<ContentValues> cvList = new Vector<ContentValues>(movieListLength) ;
            // looping through All Results
            for (int i = 0; i < reviewListLength; i++) {

                JSONObject c = results.getJSONObject(i);
                ContentValues cValues = new ContentValues();

                String review_id = c.getString("id");
                String author = c.getString("author");
                String content = c.getString("content");
                cValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review_id);
                cValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, author);
                cValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, content);
                cValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);

                cvList.add(cValues);
            }
            int reviewAdded = 0;
            if (cvList.size() > 0) {
                ContentValues[] values = new ContentValues[cvList.size()];
                cvList.toArray(values);
                reviewAdded = mContext.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, values);
            }
            Log.d(LOG, reviewAdded + " records added into the DB");
        } catch (JSONException e) {
            Log.e(LOG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    public static Void getTrailerJsonList(Context mContext, String movieId, String JsonStr) {
        try {
            JSONObject movieJson = new JSONObject(JsonStr);
            // Getting JSON Array node results
            JSONArray results = movieJson.getJSONArray("results");
            int reviewListLength = results.length();
            Log.d(LOG, reviewListLength + " reviews fetched" + movieId);


            ArrayList<ContentValues> cvList = new ArrayList<>(reviewListLength);
            //Vector<ContentValues> cvList = new Vector<ContentValues>(movieListLength) ;
            // looping through All Results
            for (int i = 0; i < reviewListLength; i++) {

                JSONObject c = results.getJSONObject(i);
                ContentValues cValues = new ContentValues();

                String trailorId = c.getString("id");
                String key = c.getString("key");
                String title = c.getString("name");
                cValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailorId);
                cValues.put(MovieContract.TrailerEntry.COLUMN_YOUTUBE_KEY, key);
                cValues.put(MovieContract.TrailerEntry.COLUMN_TITLE, title);
                cValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);

                cvList.add(cValues);
            }
            int reviewAdded = 0;
            if (cvList.size() > 0) {
                ContentValues[] values = new ContentValues[cvList.size()];
                cvList.toArray(values);
                reviewAdded = mContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, values);
            }
            Log.d(LOG, reviewAdded + " records added into the DB");
        } catch (JSONException e) {
            Log.e(LOG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
