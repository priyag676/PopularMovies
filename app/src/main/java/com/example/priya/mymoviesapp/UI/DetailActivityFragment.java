package com.example.priya.mymoviesapp.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.priya.mymoviesapp.CustomView.ExpandedListView;
import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.adapter.DetailAdapter;
import com.example.priya.mymoviesapp.adapter.ReviewAdapter;
import com.example.priya.mymoviesapp.adapter.TrailerAdapter;
import com.example.priya.mymoviesapp.data.MovieContract;
import com.example.priya.mymoviesapp.tasks.FetchReviewTask;
import com.example.priya.mymoviesapp.tasks.FetchTrailerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    /*private static final String SHARE_HASHTAG = " #PopularMoviesApp";
    private ShareActionProvider mShareActionProvider;
    String mMovieStr;*/
    private static final int DETAIL_LOADER = 0;
    private static final int REVIEW_LOADER = 1;
    private static final int TRAILER_LOADER = 2;
    private ListView mDetailLayout;
    private ExpandedListView mReviewList;
    private ExpandedListView mTrailerList;
    private DetailAdapter mDetailAdapter;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private CardView mReviewCard;
    private CardView mtrailerCard;
    private CardView mDetailCard;
    String mMovieId;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mMovieId = intent.getStringExtra("movieid");
        Log.v(LOG_TAG, "In on create :" + mMovieId);
        FetchReviewTask reviewTask = new FetchReviewTask(getContext());
        FetchTrailerTask trailerTask = new FetchTrailerTask(getContext());
        reviewTask.execute(mMovieId);
        trailerTask.execute(mMovieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDetailCard = (CardView) rootView.findViewById(R.id.detail_card);
        mReviewCard = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);
        mtrailerCard = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);

        mDetailLayout = (ListView) rootView.findViewById(R.id.detail_movie_listView);
        mReviewList = (ExpandedListView) rootView.findViewById(R.id.review_listView);
        mTrailerList = (ExpandedListView) rootView.findViewById(R.id.trailers_listview);

        mDetailAdapter = new DetailAdapter(getActivity(), null, 0);
        mReviewAdapter = new ReviewAdapter(getActivity(), null, 1);
        mTrailerAdapter = new TrailerAdapter(getActivity(), null, 2);

        mDetailLayout.setAdapter(mDetailAdapter);
        mReviewList.setAdapter(mReviewAdapter);
        mTrailerList.setAdapter(mTrailerAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

    /*    // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }*/
    }


 /*   private Intent createShareForecastIntent() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mMovieStr + "  " + SHARE_HASHTAG);
        return shareIntent;
    }
*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            switch (id) {
                case DETAIL_LOADER:
                    return new CursorLoader(getActivity(),
                            intent.getData(),
                            null,
                            null,
                            null,
                            null);
                case REVIEW_LOADER:
                    Log.v(LOG_TAG, "In on create Loader :" + mMovieId);
                    return new CursorLoader(getActivity(),
                            MovieContract.ReviewEntry.CONTENT_URI,
                            null,
                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "=?",
                            new String[]{mMovieId},
                            null);
                case TRAILER_LOADER:
                    return new CursorLoader(getActivity(),
                            MovieContract.TrailerEntry.CONTENT_URI,
                            null,
                            MovieContract.TrailerEntry.COLUMN_MOVIE_ID + "=?",
                            new String[]{mMovieId},
                            null);

                default:
                    throw new UnsupportedOperationException("Unknown loader id: " + id);
            }
        } else
            return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "In on Load Finish :" + mMovieId);
        switch (loader.getId()) {
            case DETAIL_LOADER:
                mDetailAdapter.swapCursor(cursor);
                Log.v(LOG_TAG, "In on Load Finish :" + loader.getId());
                break;
            case TRAILER_LOADER:
                mTrailerAdapter.swapCursor(cursor);
                Log.v(LOG_TAG, "In on Load Finish :" + loader.getId());
                break;
            case REVIEW_LOADER:
                mReviewAdapter.swapCursor(cursor);
                Log.v(LOG_TAG, "In on Load Finish :" + loader.getId());
                break;

            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }

       /* if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());

        }*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
      /*  mDetailAdapter.swapCursor(null);
        mReviewAdapter.swapCursor(null);*/
        switch (loader.getId()) {
            case DETAIL_LOADER:
                mDetailAdapter.swapCursor(null);
                break;
            case REVIEW_LOADER:
                mReviewAdapter.swapCursor(null);
                break;
            case TRAILER_LOADER:
                mTrailerAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }

    }

}
