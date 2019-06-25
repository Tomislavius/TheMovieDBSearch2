package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.os.Bundle;

import com.example.tomislavrajic.themoviedbsearch2.utils.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesFragmentPagerAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public class MoviesActivity extends BaseActivity implements MoreInfoClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        MoviesFragmentPagerAdapter moviesFragmentPagerAdapter = new MoviesFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(moviesFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState != null && savedInstanceState.getSerializable(Result.MOVIE) != null) {
            movieResult = (Result) savedInstanceState.getSerializable(Result.MOVIE);
            moreInfoDialog.setData(movieResult, Result.MOVIE);
            moreInfoDialog.show();
        }
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, String isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, Result.MOVIE);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }
}