package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.os.Bundle;

import com.example.tomislavrajic.themoviedbsearch2.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesFragmentPagerAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public class MoviesActivity extends BaseActivity implements MoreInfoDialog.OnExternalWebPageClickListener, MoreInfoClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getSerializable("Movie") != null) {
            movieResult = (Result) savedInstanceState.getSerializable("Movie");
            moreInfoDialog.setData(movieResult, true);
            moreInfoDialog.show();
        }

        MoviesFragmentPagerAdapter moviesFragmentPagerAdapter = new MoviesFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(moviesFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, boolean isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, true);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }
}