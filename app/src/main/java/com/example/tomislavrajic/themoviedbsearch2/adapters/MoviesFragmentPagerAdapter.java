package com.example.tomislavrajic.themoviedbsearch2.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesNowPlayingFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesPopularFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesTopRatedFragment;
import com.example.tomislavrajic.themoviedbsearch2.fragments.MoviesUpcomingFragment;

public class MoviesFragmentPagerAdapter extends FragmentPagerAdapter {

    public MoviesFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MoviesPopularFragment();
        } else if (position == 1) {
            return new MoviesTopRatedFragment();
        } else if (position == 2) {
            return new MoviesUpcomingFragment();
        } else return new MoviesNowPlayingFragment();
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
                return "UPCOMING";
            default:
                return "NOW PLAYING";
        }
    }
}
