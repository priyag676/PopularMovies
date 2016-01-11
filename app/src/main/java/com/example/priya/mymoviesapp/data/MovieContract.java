package com.example.priya.mymoviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Priya on 12/27/2015.
 */
public class MovieContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.priya.mymoviesapp.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    public static final String PATH_MOVIE = "movies";
    public static final String PATH_REVIEW = "reviews";
    public static final String PATH_TRAILER = "trailers";

    /* Inner class that defines the table contents of the Movies table */
    public static final class MoviesEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_ORIGINAL_TITTLE = "original_title";
        public static final String COLUMN_MOVIE_OVERVIEW ="overview";
        public static final String COLUMN_MOVIE_RATING ="vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE ="release_date";
        public static final String COLUMN_MOVIE_POPULARITY ="popularity";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_POSTER = "movie_poster";
        public static final String COLUMN_FAVORITE = "favorite"; // pseudo-boolean for favorite movie


        /**
         * Build a Uri for a record of the table, using the ID
         *
         * @param id The ID of the record
         * @return A new Uri with the given ID appended to the end of the path
         */
        public static Uri buildMoviesWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Parse the ID of a record, or return -1 instead
         *
         * @param uri The Uri of the record
         * @return The Id of the record or -1 if this doesn't apply
         */
        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }
    public static final class ReviewEntry implements BaseColumns {
        // Content URI for the ReviewEntry
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        // Constant strings to tell the difference between a list of items (CONTENT_TYPE)
        // and a singe item (CONTENT_ITEM_TYPE)
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "reviews";

        // columns
        public static final String COLUMN_AUTHOR = "author"; //trailer title
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id"; // the movie id from the backend (used for joins)

        /**
         * Get the movie ID in the URI (the ID from the Backend)
         *
         * @param uri The Uri of the review with the movie id appended
         * @return The ID of the movie, or -1 if doesn't exist
         */
        public static long getReviewIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        /**
         * Creates a review uri with the movie id (from the backend) appended
         *
         * @param insertedId The ID of the movie
         * @return The uri of the review
         */
        public static Uri buildReviewWithId(long insertedId) {
            return ContentUris.withAppendedId(CONTENT_URI, insertedId);
        }

        public static Uri buildUriByMovieId(long movieId) {
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(PATH_MOVIE)
                    .appendPath(Long.toString(movieId))
                    .build();
        }
    }
    public static final class TrailerEntry implements BaseColumns {
        // Content URI for the TrailerEntry
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        // Constant strings to tell the difference between a list of items (CONTENT_TYPE)
        // and a singe item (CONTENT_ITEM_TYPE)
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String TABLE_NAME = "trailers";

        // columns
        public static final String COLUMN_TITLE = "title"; //trailer title
        public static final String COLUMN_YOUTUBE_KEY = "youtube_key";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id"; // the movie id from the backend (used for joins)

        /**
         * Get the movie ID in the URI (the ID from the Backend)
         *
         * @param uri The trailer's URI with the movie ID
         * @return The movie ID or -1 if doesn't exist
         */
        public static long getMovieIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        /**
         * Creates a trailer uri with the movie id (from the backend) appended
         *
         * @param movieId The movie ID
         * @return the URI of the trailer
         */
        public static Uri buildTrailerWithId(long movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }
    }

}


