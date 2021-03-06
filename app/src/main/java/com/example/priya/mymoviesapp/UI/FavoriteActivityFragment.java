package com.example.priya.mymoviesapp.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.adapter.MoviesAdapter;
import com.example.priya.mymoviesapp.data.MovieContract;
/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = FavoriteActivityFragment.class.getSimpleName();
    public static final int FAVORITE_LOADER = 0;

    MoviesAdapter favMoviesAdapter;
    GridView favMoviesGridView;

    public FavoriteActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        favMoviesGridView = (GridView) rootView.findViewById(R.id.fav_movies_gridview);
        favMoviesAdapter = new MoviesAdapter(getActivity(), null, 0); // cursor added on load
        View emptyView = rootView.findViewById(R.id.listview_empty_fav);
        favMoviesGridView.setEmptyView(emptyView);
        favMoviesGridView.setAdapter(favMoviesAdapter);

        favMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor currentData = (Cursor) parent.getItemAtPosition(position);
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

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MovieContract.MoviesEntry.CONTENT_URI,
                null,
                MovieContract.MoviesEntry.COLUMN_FAVORITE + "= ?",
                new String[]{Integer.toString(1)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Cursor loaded, " + data.getCount() + " favorite movies");
        favMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favMoviesAdapter.swapCursor(null);
    }
}
