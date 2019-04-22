package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

import io.realm.RealmResults;

public abstract class DBHelper {

    public abstract RealmResults<Result> getWatchedList();
    public abstract void saveItem(Result result);
    public abstract void deleteItem(int id);
}
