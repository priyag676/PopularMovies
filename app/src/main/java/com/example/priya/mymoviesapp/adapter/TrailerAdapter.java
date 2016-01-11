package com.example.priya.mymoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
             /*   int youtubeKeyColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_YOUTUBE_KEY);
                String youtubeKey = cursor.getString(youtubeKeyColumn);*/
                Uri videoUri = Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey);

                Intent playTrailer = new Intent(Intent.ACTION_VIEW, videoUri);
                context.startActivity(playTrailer);
            }
        });

    }
    public  String getYoutubeKey()
    {
        return youtubeKey;
    }
}


/*
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private final Context mContext;
    private int layoutResourceId;
    private ArrayList<Review> mReviewData = new ArrayList<Review>();

    private List<Trailer> mTrailerData = new ArrayList<Trailer>();

    public TrailerAdapter(Context mContext, int layoutResourceId, List<Trailer> mTrailerData) {
        super(mContext, layoutResourceId,mTrailerData );
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mTrailerData = mTrailerData;
    }


 */
/*
     * Updates grid data and refresh grid items.
     *//*

    public void setTrailerData(ArrayList<Trailer> mTrailerData) {
        this.mTrailerData = mTrailerData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.trailer_name);
            holder.imageView = (ImageView) row.findViewById(R.id.trailer_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Trailer trailer = mTrailerData.get(position);
        holder.titleTextView.setText(trailer.getName());

        String youtubeKey = trailer.getKey();
        String yt_thumbnail_url = "http://img.youtube.com/vi/" + youtubeKey + "/0.jpg";
        Picasso.with(mContext).load((yt_thumbnail_url)).fit().into(holder.imageView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeKey = trailer.getKey();

                Uri videoUri = Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey);

                Intent playTrailer = new Intent(Intent.ACTION_VIEW, videoUri);
                mContext.startActivity(playTrailer);
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
*/
