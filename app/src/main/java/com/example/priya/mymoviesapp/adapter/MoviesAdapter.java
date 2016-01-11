package com.example.priya.mymoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Priya on 12/30/2015.
 */
public class MoviesAdapter extends CursorAdapter {
    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_DEFAULT_SIZE = "w185";
    public static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    public MoviesAdapter
            (Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        View row = view;
        if (row != null) {
        TextView movieNameView = (TextView) view.findViewById(R.id.text);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.image);

        int moviePosterColumn = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_POSTER);
        int movieNameColumn = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_MOVIE_ORIGINAL_TITTLE);

        String moviePoster = cursor.getString(moviePosterColumn);
        String movieName = cursor.getString(movieNameColumn);

            movieNameView.setText(movieName);

            Uri imageUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                    .appendPath(IMAGE_DEFAULT_SIZE)
                    .appendPath(moviePoster.substring(1))
                    .build();

            Log.d(LOG_TAG + " - Image uri:", imageUri.toString());
            Picasso.with(mContext).load((imageUri)).fit().into(posterImageView);
        }
        //Picasso.with(mContext).load((imageUri)).placeholder(R.drawable.loading).into(posterImageView);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
    }

}
