package com.example.priya.mymoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Priya on 12/28/2015.
 */
public class MovieProvider extends ContentProvider {
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = createUriMatcher();
    private MovieDbHelper mOpenHelper;
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 102;
    public static final int TRAILER = 200;
    public static final int TRAILER_WITH_MOVIE_ID = 201;
    public static final int REVIEW = 300;
    public static final int REVIEW_WITH_MOVIE_ID = 301;


    public static UriMatcher createUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", TRAILER_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_WITH_MOVIE_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                cursor = db.query(
                        MovieContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_WITH_ID: {
                long _id = MovieContract.MoviesEntry.getIdFromUri(uri);
                cursor = db.query(
                        MovieContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MovieContract.MoviesEntry._ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case TRAILER:
                cursor = db.query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case TRAILER_WITH_MOVIE_ID: {
                long _id = MovieContract.TrailerEntry.getMovieIdFromUri(uri);
                cursor = db.query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case REVIEW:
            cursor = db.query(
                    MovieContract.ReviewEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
            break;

            case REVIEW_WITH_MOVIE_ID: {
                long _id = MovieContract.ReviewEntry.getReviewIdFromUri(uri);
                cursor = db.query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;

            case TRAILER_WITH_MOVIE_ID:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
            return MovieContract.ReviewEntry.CONTENT_TYPE;

            case REVIEW_WITH_MOVIE_ID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long inserted_id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, values);
                if (inserted_id > 0) {
                    returnUri = MovieContract.MoviesEntry.buildMoviesWithId(inserted_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TRAILER: {
                long insertedId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if (insertedId > 0) {
                    returnUri = MovieContract.TrailerEntry.buildTrailerWithId(insertedId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW: {
                long insertedId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (insertedId > 0) {
                    returnUri = MovieContract.ReviewEntry.buildReviewWithId(insertedId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILER:
                rowsUpdated = db.update(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER:
                rowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:{
                db.beginTransaction();
                int returnCount = 0;

                for (ContentValues value : values) {

                    long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
        }
        case TRAILER: {
            db.beginTransaction();
            int count = 0;

            for (ContentValues item : values) {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, item);
                if (_id != -1) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
            case REVIEW: {
                db.beginTransaction();
                int count = 0;

                for (ContentValues item : values) {
                    long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, item);
                    if (_id != -1) {
                        count++;
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();

                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
