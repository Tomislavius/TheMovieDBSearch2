package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;

import io.realm.RealmResults;

public abstract class DBHelper {

    public abstract RealmResults<MoviesResult> getWatchedList();
    public abstract void saveItem(MoviesResult moviesResult);
    public abstract void deleteItem(int id);
}
