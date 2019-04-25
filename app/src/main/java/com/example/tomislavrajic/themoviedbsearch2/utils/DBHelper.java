package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

import io.realm.RealmResults;

public abstract class DBHelper {

    static final String TITLE = "title";
    static final String ID = "id";

    public abstract RealmResults<Result> getWatchedList();
    public abstract void saveItem(Result result);
    public abstract void deleteItem(int id);
}
