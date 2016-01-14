package com.example.priya.mymoviesapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Priya on 1/2/2016.
 */
public class DetailAdapter extends CursorAdapter {
    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_DEFAULT_SIZE = "w342";
    public static final String LOG_TAG = DetailAdapter.class.getSimpleName();
    public DetailAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_detail_movie, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view.findViewById(R.id.detail_image);
        TextView titleTextView = (TextView) view.findViewById(R.id.detail_text_title);
        TextView overviewTextView = (TextView) view.findViewById(R.id.detail_text_overview);
        TextView release_dateTextView = (TextView) view.findViewById(R.id.detail_text_releaseDate);
        TextView ratingTextView = (TextView) view.findViewById(R.id.detail_text_rating);
        TextView popularityTextView = (TextView) view.findViewById(R.id.detail_text_popularity);
        TextView vote_countTextView = (TextView) view.findViewById(R.id.detail_text_votecount);

        Button markFavBtn = (Button) view.findViewById(R.id.fav_btn);
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ORIGINAL_TITTLE));
        String image = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_POSTER));
        String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW));
        String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE));
        String rating = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_RATING));
        String popularity = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_POPULARITY));
        String vote_count = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_VOTE_COUNT));
        final String mMovie_id = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ID));
        final int IS_FAVORITE = cursor.getInt(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_FAVORITE));
        final String _ID = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry._ID));

        if (IS_FAVORITE == 1) {
            String unmark = mContext.getString(R.string.details_button_favorite_remove);
            markFavBtn.setText(unmark);
        } else {
            String mark = mContext.getString(R.string.details_button_favorite_add);
            markFavBtn.setText(mark);
        }


        Uri posterUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_DEFAULT_SIZE)
                .appendPath(image.substring(1)) //remove the heading slash
                .build();

        Picasso.with(mContext).load(posterUri).fit().into(imageView);

        titleTextView.setText(title);
        overviewTextView.setText(overview);
        release_dateTextView.setText(release_date);
        ratingTextView.setText(rating);
        popularityTextView.setText(popularity);
        vote_countTextView.setText(vote_count);


        markFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (IS_FAVORITE) {
                    case 0: {
                        // movie is not favorited
                        // mark it
                        ContentValues addFavorite = new ContentValues();
                        addFavorite.put(MovieContract.MoviesEntry.COLUMN_FAVORITE, 1); //mark as favorite

                        int updatedRows = mContext.getContentResolver().update(
                                MovieContract.MoviesEntry.CONTENT_URI,
                                addFavorite,
                                MovieContract.MoviesEntry._ID + " = ?",
//                            new String[]{Integer.toString(movieId)}
                                new String[]{String.valueOf(_ID)}
                        );

                        if (updatedRows <= 0) {
                            Log.d(LOG_TAG, "Movie not marked as favorite");
                        } else {
                            Log.d(LOG_TAG, "Movie marked as favorite");
                        }
                    }
                    break;

                    case 1: {
                        // movie is favorited
                        // unmark it
                        ContentValues removeFavorite = new ContentValues();
                        removeFavorite.put(MovieContract.MoviesEntry.COLUMN_FAVORITE, 0); //unmark as favorite

                        int updatedRows = mContext.getContentResolver().update(
                                MovieContract.MoviesEntry.CONTENT_URI,
                                removeFavorite,
                                MovieContract.MoviesEntry._ID + " = ?",
                                new String[]{String.valueOf(_ID)}
//                            new String[]{Integer.toString(movieId)}
                        );

                        if (updatedRows < 0) {
                            Log.d(LOG_TAG, "Movie not unmarked as favorite");
                        } else {
                            Log.d(LOG_TAG, "Movie unmarked as favorite");
                        }
                    }
                    break;

                    default:
                        Log.e(LOG_TAG, "What is this?!");

                }
            }
        });
    }
}

