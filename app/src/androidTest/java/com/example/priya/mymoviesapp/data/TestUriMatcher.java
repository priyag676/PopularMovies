package com.example.priya.mymoviesapp.data;

/**
 * Created by Priya on 12/28/2015.
 */

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {


    private static final Uri TEST_MOVIES_DIR =MovieContract.MoviesEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.createUriMatcher();

        assertEquals("Error: The WEATHER URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIES_DIR), MovieProvider.MOVIE);
    }
}