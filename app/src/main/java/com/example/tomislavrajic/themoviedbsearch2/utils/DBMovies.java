package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;

import io.realm.Realm;
import io.realm.RealmResults;

public class DBMovies extends DBHelper {

    private Realm realm;

    public DBMovies() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<MoviesResult> getWatchedList() {
        RealmResults<MoviesResult> results = realm.where(MoviesResult.class).findAll();
        results = results.sort("title");
        return results;
    }

    @Override
    public void saveItem(MoviesResult moviesResult) {
        realm.beginTransaction();
        realm.insertOrUpdate(moviesResult);
        realm.commitTransaction();
    }

    @Override
    public void deleteItem(int id) {
        MoviesResult movie = realm.where(MoviesResult.class).equalTo("id", id).findFirst();
        if (movie != null) {
            realm.beginTransaction();
            movie.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}