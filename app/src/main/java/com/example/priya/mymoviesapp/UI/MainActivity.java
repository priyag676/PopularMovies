package com.example.priya.mymoviesapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.priya.mymoviesapp.R;
import com.example.priya.mymoviesapp.Sync.MovieSyncAdapter;
import com.example.priya.mymoviesapp.Utility;

public class MainActivity extends AppCompatActivity {
    private String mSortOrder;
    private final String ALLMOVIESFRAGMENT_TAG = "AMFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSortOrder = Utility.getDefaultSortOrder(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new AllMoviesFragment(), ALLMOVIESFRAGMENT_TAG)
                    .commit();
        }*/
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
            //noinspection SimplifiableIfStatement
        }
        if (id == R.id.action_favorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        String sortOrder = Utility.getDefaultSortOrder(this);
        // update the location in our second pane using the fragment manager
        if (sortOrder != null && !sortOrder.equals(mSortOrder)) {
            AllMoviesFragment ff = (AllMoviesFragment) getSupportFragmentManager().findFragmentByTag(ALLMOVIESFRAGMENT_TAG);
            if (null != ff) {
                ff.onSortOrderChanged();
            }
            mSortOrder = sortOrder;
        }
    }
}
