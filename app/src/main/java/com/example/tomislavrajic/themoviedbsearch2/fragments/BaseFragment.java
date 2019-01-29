package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.v4.app.Fragment;

import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.utils.WatchedSharedPreferences;

public abstract class BaseFragment extends Fragment {

    abstract MoviesRecyclerViewAdapter getAdapter();

    public void refreshData() {
        WatchedSharedPreferences watchedSharedPreferences = new WatchedSharedPreferences(getContext());
        getAdapter().refreshWatchedMoviesList(watchedSharedPreferences.getWatchedMovies());
    }
}
