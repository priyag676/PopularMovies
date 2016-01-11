package com.example.priya.mymoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Priya on 12/27/2015.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
           Students: Uncomment this test once you've written the code to create the MOVIE
           table.  Note that you will have to have chosen the same column names that I did in
           my solution for this test to compile, so if you haven't yet done that, this is
           a good time to change your column names to match mine.
           Note that this only tests that the Location table has the correct columns, since we
           give you the code for the weather table.  This test does not look at the
        */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MoviesEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without movie entry table",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MoviesEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MoviesEntry._ID);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_POSTER);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_ORIGINAL_TITTLE);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_RATING);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_POPULARITY);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_MOVIE_VOTE_COUNT);
        movieColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_FAVORITE);

        final HashSet<String> reviewColumnhashSet = new HashSet<String>();
        reviewColumnhashSet.add(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        reviewColumnhashSet.add(MovieContract.ReviewEntry.COLUMN_CONTENT);
        reviewColumnhashSet.add(MovieContract.ReviewEntry.COLUMN_REVIEW_ID);
        reviewColumnhashSet.add(MovieContract.ReviewEntry.COLUMN_MOVIE_ID);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }

    public void testMoviesTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues movieValues = TestUtilities.createMovieValues();
        long movieRowId = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, movieValues);
        assertTrue(movieRowId != -1);
        Cursor movieCursor = db.query(MovieContract.MoviesEntry.TABLE_NAME,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from movies query", movieCursor.moveToFirst());
        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate", movieCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                movieCursor.moveToNext());
        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                movieCursor.moveToNext());

    }

    public void testReviewTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues reviewValues = TestUtilities.createReviewValues();
        long movieRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, reviewValues);
        assertTrue(movieRowId != -1);
        Cursor reviewcursor = db.query(MovieContract.ReviewEntry.TABLE_NAME,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from review query", reviewcursor.moveToFirst());
        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate", reviewcursor, reviewValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                reviewcursor.moveToNext());
        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                reviewcursor.moveToNext());

    }
    public void testTrailerTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues Values = TestUtilities.createTrailerValues();
        long movieRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, Values);
        assertTrue(movieRowId != -1);
        Cursor cursor = db.query(MovieContract.TrailerEntry.TABLE_NAME,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from review query", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate", cursor, Values);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext());
        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext());

    }
}
