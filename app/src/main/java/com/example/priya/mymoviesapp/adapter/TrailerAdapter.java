package com.example.priya.mymoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
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



public class TrailerAdapter extends CursorAdapter {
    String youtubeKey;
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_trailer_detail, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView trailerTitleTextView = (TextView) view.findViewById(R.id.trailer_name);
        int trailerTitleColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TITLE);
        String trailerTitle = cursor.getString(trailerTitleColumn);
        trailerTitleTextView.setText(trailerTitle);

        ImageView trailerImageView = (ImageView) view.findViewById(R.id.trailer_image);
        int youtubeKeyColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_YOUTUBE_KEY);
        youtubeKey = cursor.getString(youtubeKeyColumn);
        String yt_thumbnail_url = "http://img.youtube.com/vi/" + youtubeKey + "/0.jpg";
        Picasso.with(mContext).load((yt_thumbnail_url)).fit().into(trailerImageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int youtubeKeyColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_YOUTUBE_KEY);
                String youtubeKey = cursor.getString(youtubeKeyColumn);
                Uri videoUri = Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey);
                Log.v(LOG_TAG, "Vedio Uri :" + videoUri);
                Intent playTrailer = new Intent(Intent.ACTION_VIEW, videoUri);
                context.startActivity(playTrailer);
            }
        });

        Button ShareButton = (Button) view.findViewById(R.id.shareTrailer);
        ShareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int youtubeKeyColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_YOUTUBE_KEY);
                String youtubeKey = cursor.getString(youtubeKeyColumn);
                String videoUri = "https://www.youtube.com/watch?v=" + youtubeKey;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        videoUri);
                context.startActivity(shareIntent);
            }
        });

    }
}