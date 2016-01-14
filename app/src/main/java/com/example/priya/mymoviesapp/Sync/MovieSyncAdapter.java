package com.example.priya.mymoviesapp.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.priya.mymoviesapp.BuildConfig;
import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.UI.MainActivity;
import com.example.priya.mymoviesapp.Utility;
import com.example.priya.mymoviesapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * Created by Priya on 1/6/2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_ORDER_STATUS_OK, SORT_ORDER_STATUS_SERVER_DOWN, SORT_ORDER_STATUS_SERVER_INVALID, SORT_ORDER_UNKNOWN})
    public @interface SortOrderStatus {
    }

    public static final int SORT_ORDER_STATUS_OK = 0;
    public static final int SORT_ORDER_STATUS_SERVER_DOWN = 1;
    public static final int SORT_ORDER_STATUS_SERVER_INVALID = 2;
    public static final int SORT_ORDER_UNKNOWN = 3;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    // Interval at which to sync with the movies, in milliseconds.
    // 60 seconds * 60 minutes * 24 hours = 1 Day
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 60* 60 * 24;
    private static final int NOTIFICATION_ID = 3004;
    private static long lastSyncTime = 0L;

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        String sort_by = Utility.getDefaultSortOrder(getContext());

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the tmdb query
            // Possible parameters are avaiable at themoviedb API page, at
            // https://www.themoviedb.org/documentation/api/discover

            final String BASE_URL =
                    //"http://www.google.com/ping?";
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY = "sortby";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY, sort_by)
                    .appendQueryParameter(API_KEY, BuildConfig.My_Themoviedb_Api_Key)
                    .build();
            Log.v(LOG_TAG, " URL : " + builtUri);

            URL url = new URL(builtUri.toString());

            // Create the request to themoviedb , and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            //StringBuffer buffer = new StringBuffer();
            StringBuilder builder = new StringBuilder();
            ;

            if (inputStream == null) {
                // Nothing to do.
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                builder.append(line + "\n");
            }
            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                setSortOrderStatus(getContext(), SORT_ORDER_STATUS_SERVER_DOWN);
                Log.d(LOG_TAG, "No response from the server");
                return;
            }

            movieJsonStr = builder.toString();
            getMovieDataFromJson(getContext(), movieJsonStr);
            //Log.v(LOG_TAG," Forecast Json String: " +movieJsonStr);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            return;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            setSortOrderStatus(getContext(), SORT_ORDER_STATUS_SERVER_DOWN);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            setSortOrderStatus(getContext(), SORT_ORDER_STATUS_SERVER_INVALID);
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
        return;
    }

    private  Void getMovieDataFromJson(Context mContext, String jsonStr) throws JSONException {
        String LOG_TAG = Utility.class.getSimpleName();
        // These are the names of the JSON objects that need to be extracted.
        // JSON Node names
        final String RESULTS = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_POSTER = "poster_path";
        final String PLOT_SYNOPSIS = "overview";
        final String RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        try {
            JSONObject movieJson = new JSONObject(jsonStr);
            // Getting JSON Array node results
            JSONArray results = movieJson.getJSONArray(RESULTS);
            int movieListLength = results.length();
            Log.d(LOG_TAG, movieListLength + " items fetched");
            ArrayList<ContentValues> cvList = new ArrayList<>(movieListLength);
            for (int i = 0; i < movieListLength; i++) {
                JSONObject c = results.getJSONObject(i);
                ContentValues cValues = new ContentValues();

                String movie_id = c.getString(MOVIE_ID);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID, movie_id);

                String original_title = c.getString(MOVIE_ORIGINAL_TITLE);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ORIGINAL_TITTLE, original_title);
                Log.v(LOG_TAG, original_title);
                String poster_path = c.getString(MOVIE_POSTER);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_POSTER, poster_path);
                Log.v(LOG_TAG, poster_path);
                String overview = c.getString(PLOT_SYNOPSIS);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW, overview);

                String rating = c.getString(RATING);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_RATING, rating);

                String release_date = c.getString(RELEASE_DATE);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, release_date);

                String popularity = c.getString(POPULARITY);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_POPULARITY, popularity);

                String vote_count = c.getString(VOTE_COUNT);
                cValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_VOTE_COUNT, vote_count);


                //insert into the DB
                cvList.add(cValues);

            }

            int itemsAdded = 0;
            if (cvList.size() > 0) {
                ContentValues[] values = new ContentValues[cvList.size()];
                cvList.toArray(values);
                itemsAdded = mContext.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, values);
                notification();
            }

            Log.d(LOG_TAG, itemsAdded + " records added into the DB");
            //  Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
            setSortOrderStatus(mContext, SORT_ORDER_STATUS_OK);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            setSortOrderStatus(mContext, SORT_ORDER_STATUS_SERVER_INVALID);
        }
        return null;
    }

    private void notification() {
        Log.v(LOG_TAG, "In Notification");
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( !displayNotifications ) {
            return;
        }

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            lastSyncTime = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSyncTime >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with the weather.
                int smallIcon = R.drawable.ic_launcher_2;
                Bitmap largeIcon = BitmapFactory.decodeResource(
                        getContext().getResources(),
                        R.drawable.ic_launcher_2);
                String title = context.getString(R.string.app_name);
                // Define the text of the forecast.
                String contentText = context.getString(R.string.format_notification);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(smallIcon)
                                .setLargeIcon(largeIcon)
                                .setContentTitle(title)
                                .setContentText(contentText);
                Intent resultIntent = new Intent(context, MainActivity.class);
// The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.apply();
            }
        }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
        public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    /* Helper method to schedule the sync adapter periodic execution
    */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    static private void setSortOrderStatus(Context c, @SortOrderStatus int sortOrderStatus) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_sort_order_status_key), sortOrderStatus);
        spe.commit();
    }


}
