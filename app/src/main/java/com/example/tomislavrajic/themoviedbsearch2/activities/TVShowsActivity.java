package com.example.tomislavrajic.themoviedbsearch2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesFragmentPagerAdapter;
import com.example.tomislavrajic.themoviedbsearch2.adapters.TVShowsFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVShowsActivity extends AppCompatActivity {

    public static final int WATCHED_MOVIES_REQUEST_CODE = 1313;


    //region View
    @BindView(R.id.tl_top_rated)
    TabLayout mTabLayout;

    @BindView(R.id.vp_top_rated)
    ViewPager mViewPager;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        TVShowsFragmentAdapter tvShowsFragmentPagerAdapter = new TVShowsFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tvShowsFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.watched_movies:
                intent = new Intent(this, WatchedMoviesActivity.class);
                startActivityForResult(intent, WATCHED_MOVIES_REQUEST_CODE);
                return true;
            case R.id.watched_tv_shows:
//                intent = new Intent(this, WatchedMoviesTVShows.class);
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
