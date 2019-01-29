package com.example.tomislavrajic.themoviedbsearch2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class WatchedSharedPreferences {

    private static final String WATCHED_PREFS = "watchedPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public WatchedSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(WATCHED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveMovie(MoviesResult moviesResult) {
        Gson gson = new Gson();
        String movie = gson.toJson(moviesResult);
        editor.putString(String.valueOf(moviesResult.getId()), movie);
        editor.commit();
    }

    public ArrayList<MoviesResult> getWatchedMovies() {

        Gson gson = new Gson();
        ArrayList<MoviesResult> movies = new ArrayList<>(0);
        Map<String, ?> all = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String movieString = entry.getValue().toString();
            movies.add(gson.fromJson(movieString, MoviesResult.class));
        }
        return movies;
    }

    public void deleteMovie(int id) {
        editor.remove(String.valueOf(id));
        editor.commit();
    }
}
