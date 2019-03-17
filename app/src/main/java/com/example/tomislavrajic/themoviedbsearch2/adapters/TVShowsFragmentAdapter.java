package com.example.tomislavrajic.themoviedbsearch2.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesNowPlayingFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesPopularFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesTopRatedFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesUpcomingFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.TVShowsAiringTodayFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.TVShowsOnTVFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.TVShowsPopularFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.TVShowsTopRatedFragment;

public class TVShowsFragmentAdapter extends FragmentPagerAdapter {

    public TVShowsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TVShowsPopularFragment();
        } else if (position == 1) {
            return new TVShowsTopRatedFragment();
        } else if (position == 2) {
            return new TVShowsOnTVFragment();
        } else return new TVShowsAiringTodayFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "POPULAR";
            case 1:
                return "TOP RATED";
            case 2:
                return "ON TV";
            default:
                return "AIRING TODAY";
        }
    }
}