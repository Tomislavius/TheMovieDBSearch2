package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.annotation.NonNull;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesTopRatedFragment extends BaseFragment {

    protected void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getTopRatedMoviesResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                assert response.body() != null;
                moviesRecyclerViewAdapter.setData(response.body().getResults());
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {

            }
        });
    }
}