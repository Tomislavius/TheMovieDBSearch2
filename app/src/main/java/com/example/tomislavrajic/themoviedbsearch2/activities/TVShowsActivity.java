package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.os.Bundle;

import com.example.tomislavrajic.themoviedbsearch2.MoreInfoClickListener;
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

        if (savedInstanceState != null && savedInstanceState.getSerializable("Movie") != null) {
            //TODO movie change to CONST
            movieResult = (Result) savedInstanceState.getSerializable("Movie");
            moreInfoDialog.setData(movieResult, false);
            moreInfoDialog.show();
        }
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, boolean isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, false);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }
}