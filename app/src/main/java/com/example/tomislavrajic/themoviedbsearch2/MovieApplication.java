package com.example.tomislavrajic.themoviedbsearch2;

import android.app.Application;

import io.realm.Realm;

public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}