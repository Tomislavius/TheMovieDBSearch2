package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBTVShows extends DBHelper {

    private static DBTVShows INSTANCE;

    //region Fields
    private Realm realm;
    //endregion

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
        Result tvShow = realm.where(Result.class).equalTo(DBHelper.ID, id).findFirst();
        if (tvShow != null) {
            realm.beginTransaction();
            tvShow.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}