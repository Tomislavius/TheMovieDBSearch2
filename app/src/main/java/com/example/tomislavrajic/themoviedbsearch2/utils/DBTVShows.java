package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBTVShows extends DBHelper {

    private Realm realm;
    private static DBTVShows INSTANCE;

    public DBTVShows() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("TVShows.realm")
                .build();
        realm = Realm.getInstance(config);
    }

    public static DBTVShows getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBTVShows();
        }
        return INSTANCE;
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
        MoviesResult tvShow = realm.where(MoviesResult.class).equalTo("id", id).findFirst();
        if (tvShow != null) {
            realm.beginTransaction();
            tvShow.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}