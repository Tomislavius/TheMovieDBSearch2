package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.os.Bundle;

import com.example.tomislavrajic.themoviedbsearch2.utils.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.adapters.TVShowsFragmentAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public class TVShowsActivity extends BaseActivity implements MoreInfoClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TVShowsFragmentAdapter tvShowsFragmentPagerAdapter = new TVShowsFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tvShowsFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState != null && savedInstanceState.getSerializable(Result.MOVIE) != null) {
            movieResult = (Result) savedInstanceState.getSerializable(Result.MOVIE);
            moreInfoDialog.setData(movieResult, Result.TV_SHOW);
            moreInfoDialog.show();
        }
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, String isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, Result.TV_SHOW);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }
}