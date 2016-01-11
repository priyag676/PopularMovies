package com.example.priya.mymoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.priya.mymoviesapp.data.MovieContract.MoviesEntry;
import com.example.priya.mymoviesapp.data.MovieContract.ReviewEntry;
import com.example.priya.mymoviesapp.data.MovieContract.TrailerEntry;
/**
 * Created by Priya on 12/27/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, " +
                MoviesEntry.COLUMN_MOVIE_ORIGINAL_TITTLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_POPULARITY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
                "UNIQUE (" + MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " ( "
                + ReviewEntry._ID + " INTEGER PRIMARY KEY, "
                + ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, "
                + ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL "
              /*+ " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + ")"*/
                + ");";
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " ( "
                + TrailerEntry._ID + " INTEGER PRIMARY KEY, "
                + TrailerEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + TrailerEntry.COLUMN_YOUTUBE_KEY + " TEXT NOT NULL, "
                + TrailerEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL "
                /*+ " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_MOVIE_ID + ")"*/
                + ");";


        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(db);
    }
}
