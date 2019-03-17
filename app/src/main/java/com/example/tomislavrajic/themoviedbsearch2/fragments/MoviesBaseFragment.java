package com.example.tomislavrajic.themoviedbsearch2.fragments;

import com.example.tomislavrajic.themoviedbsearch2.utils.DBHelper;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBMovies;

public abstract class MoviesBaseFragment extends BaseFragment {

    @Override
    protected abstract void loadMovies();

    @Override
    DBHelper getDBHelper() {
        return new DBMovies();
    }
}