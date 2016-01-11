package com.example.priya.mymoviesapp;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Priya on 12/27/2015.
 */
public class FullTestSuite extends TestSuite {

    public static Test suite()
    {
        return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();
    }
    public  FullTestSuite()
    {
        super();
    }
}
