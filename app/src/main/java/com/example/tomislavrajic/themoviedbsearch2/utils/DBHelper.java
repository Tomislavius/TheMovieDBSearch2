package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class DBHelper {

    private Realm realm;

    public DBHelper() {
        realm = Realm.getDefaultInstance();
    }

    public void saveMovie(MoviesResult moviesResult) {
        realm.beginTransaction();
        realm.insertOrUpdate(moviesResult);
        realm.commitTransaction();
    }

    public OrderedRealmCollection<MoviesResult> getWatchedMovies() {
        return realm.where(MoviesResult.class).findAll();
    }

    public void deleteMovie(int id) {

        MoviesResult movie = realm.where(MoviesResult.class).equalTo("id", id).findFirst();
        if (movie != null) {
            realm.beginTransaction();
            movie.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}