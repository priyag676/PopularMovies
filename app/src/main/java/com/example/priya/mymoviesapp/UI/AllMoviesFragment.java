package com.example.priya.mymoviesapp.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.Sync.MovieSyncAdapter;
import com.example.priya.mymoviesapp.Utility;
import com.example.priya.mymoviesapp.adapter.MoviesAdapter;
import com.example.priya.mymoviesapp.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    public AllMoviesFragment() {
    }

    private static final int MOVIE_LOADER = 0;
    private static final String LOG_TAG = AllMoviesFragment.class.getSimpleName();
    private GridView mGridView;
    private MoviesAdapter mMoviesAdapter;
   /// private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            //      mGridAdapter.clear();
            updateData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
      //   mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        //Initialize with empty data
        // mGridData = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(getActivity(), null, 0);
        View emptyView = rootView.findViewById(R.id.listview_empty);
        mGridView.setEmptyView(emptyView);
        mGridView.setAdapter(mMoviesAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get item at position
                //Movie item = (Movie) parent.getItemAtPosition(position);
                //Movie item = mGridAdapter.getItem(position);
                Cursor currentData = (Cursor) parent.getItemAtPosition(position);
                //Pass the image title and url to DetailsActivity
                if (currentData != null) {
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    final int MOVIE_ID_COL = currentData.getColumnIndex(MovieContract.MoviesEntry._ID);
                    final int mId = currentData.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ID);
                    String mmId = currentData.getString(mId);

                    Uri movieUri = MovieContract.MoviesEntry.buildMoviesWithId(currentData.getInt(MOVIE_ID_COL));

                    detailIntent.setData(movieUri);
                    detailIntent.putExtra("Id",MOVIE_ID_COL);
                   detailIntent.putExtra("movieid", mmId);
                    //Start details activity
                    startActivity(detailIntent);
                }
            }
        });



        //mProgressBar.setVisibility(View.VISIBLE);
        return rootView;
    }
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onSortOrderChanged( ) {
        updateData();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }


    private void updateData() {
       /* FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_default_sort_order));
        movieTask.execute(sort_by);*/
        MovieSyncAdapter.syncImmediately(getActivity());

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrderSetting = Utility.getDefaultSortOrder(getActivity());
        String sortOrder;
        Uri movieUri = MovieContract.MoviesEntry.CONTENT_URI;

        if (sortOrderSetting.equals(getString(R.string.pref_default_sort_order))) {
            sortOrder = MovieContract.MoviesEntry.COLUMN_MOVIE_POPULARITY + " DESC";
        } else {
            //sort by rating
            sortOrder = MovieContract.MoviesEntry.COLUMN_MOVIE_RATING + " DESC";
        }

        return new CursorLoader(getActivity(),
                movieUri,
                null,
                null,
                null,
                sortOrder);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //mProgressBar.setVisibility(View.GONE);
        mMoviesAdapter.swapCursor(cursor);
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }



    private void updateEmptyView() {
        if ( mMoviesAdapter.getCount() == 0 ) {
            TextView tv = (TextView) getView().findViewById(R.id.listview_empty);
            if ( null != tv ) {
                // if cursor is empty, why? do we have an invalid location
                int message = R.string.empty_data_list;
                @MovieSyncAdapter.SortOrderStatus int location = Utility.getLocationStatus(getActivity());
                switch (location) {
                    case MovieSyncAdapter.SORT_ORDER_STATUS_SERVER_DOWN:
                        message = R.string.empty_list_server_down;
                        break;
                    case MovieSyncAdapter.SORT_ORDER_STATUS_SERVER_INVALID:
                        message = R.string.empty_list_server_error;
                        break;
                    default:
                        if (!Utility.isNetworkAvailable(getActivity()) ) {
                            message = R.string.empty_list_no_network;
                        }
                }

                tv.setText(message);
            }
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ( key.equals(getString(R.string.pref_sort_order_status_key)) ) {
            updateEmptyView();
        }
    }
}
