package com.example.tomislavrajic.themoviedbsearch2.fragments;

import com.example.tomislavrajic.themoviedbsearch2.utils.DBHelper;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBTVShows;

public abstract class TVShowsBaseFragment extends BaseFragment{

    @Override
    protected abstract void loadMovies();

    @Override
    DBHelper getDBHelper() {
        return new DBTVShows();
    }
}
