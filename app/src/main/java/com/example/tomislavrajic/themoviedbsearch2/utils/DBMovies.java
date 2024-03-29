package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

import io.realm.Realm;
import io.realm.RealmResults;

public class DBMovies extends DBHelper {

    //region Fields
    private Realm realm;
    //endregion

    public DBMovies() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<Result> getWatchedList() {
        RealmResults<Result> results = realm.where(Result.class).findAll();
        results = results.sort(DBHelper.TITLE);
        return results;
    }

    @Override
    public void saveItem(Result result) {
        realm.beginTransaction();
        realm.insertOrUpdate(result);
        realm.commitTransaction();
    }

    @Override
    public void deleteItem(int id) {
        Result movie = realm.where(Result.class).equalTo(DBHelper.ID, id).findFirst();
        if (movie != null) {
            realm.beginTransaction();
            movie.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}