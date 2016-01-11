package com.example.priya.mymoviesapp.data;

import android.test.AndroidTestCase;

/**
 * Created by Priya on 12/28/2015.
 */
public class TestMovieContract extends AndroidTestCase
{
    // intentionally includes a slash to make sure Uri is getting quoted correctly

    private static final long TEST_ID = 1;
    /*public void testBuildMovie() {
        Uri moviesUri = MovieContract.MoviesEntry.buildMovieUri(TEST_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMovie in " +
                        "MoviesContract.",
                moviesUri);
        assertEquals("Error: Movie not properly appended to the end of the Uri",
                TEST_ID, moviesUri.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match our expected result",
                moviesUri.toString(),"content://com.example.priya.mymoviesapp.app/movies/1");
    }*/
}

