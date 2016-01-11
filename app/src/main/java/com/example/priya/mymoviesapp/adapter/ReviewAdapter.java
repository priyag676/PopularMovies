package com.example.priya.mymoviesapp.adapter;

/**
 * Created by Priya on 1/1/2016.
 */
/*public class ReviewAdapter extends ArrayAdapter<Review> {

    private final Context mContext;
    private int layoutResourceId;
    private ArrayList<Review> mReviewData = new ArrayList<Review>();

    public ReviewAdapter(Context mContext, int layoutResourceId, ArrayList<Review> mReviewData) {
        super(mContext, layoutResourceId, mReviewData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mReviewData = mReviewData;
    }


    */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.data.MovieContract;

/**
 * Updates grid data and refresh grid items.
 *//*
    public void setReviewData(ArrayList<Review> mReviewData) {
        this.mReviewData = mReviewData;
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
            holder.author = (TextView) row.findViewById(R.id.review_author_text_view);
            holder.content = (TextView) row.findViewById(R.id.review_content_text_view);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Review item = mReviewData.get(position);
        holder.author.setText(item.getAuthor());
        holder.content.setText(item.getContent());
        return row;
    }

    static class ViewHolder {
        TextView author;
        TextView content;
    }
}*/
public class ReviewAdapter extends CursorAdapter {
    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_review_detail, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView authorTextView = (TextView) view.findViewById(R.id.review_author_text_view);
        TextView contentTextView = (TextView) view.findViewById(R.id.review_content_text_view);

        int authorColumnIndex = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        int contentColumnIndex = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT);

        String author = cursor.getString(authorColumnIndex);
        String content = cursor.getString(contentColumnIndex);

        authorTextView.setText(author);
        contentTextView.setText(content);
    }

}

/*
public class ReviewAdapter extends CursorRecyclerViewAdapter<ReviewAdapter.ViewHolder>
{
    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTextView;
        public TextView contentTextView;
        public ViewHolder(View view) {
            super(view);
            authorTextView = (TextView) view.findViewById(R.id.review_author_text_view);
            contentTextView = (TextView) view.findViewById(R.id.review_content_text_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_detail, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        int authorColumnIndex = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        int contentColumnIndex = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT);

        String author = cursor.getString(authorColumnIndex);
        String content = cursor.getString(contentColumnIndex);

        viewHolder.authorTextView.setText(author);
        viewHolder.contentTextView.setText(content);

    }
}
*/
