package com.example.priya.mymoviesapp.Sync;

import android.content.Intent;
import android.os.IBinder;
import android.app.Service;
/**
 * Created by Priya on 1/6/2016.
 */
public class MovieAuthService extends Service {
    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new MovieAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}