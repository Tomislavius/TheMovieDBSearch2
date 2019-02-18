package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPopularFragment extends BaseFragment {

    protected void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getPopularMoviesResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                if (response.code() == 200) {
                    moviesRecyclerViewAdapter.setData(response.body().getResults());
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to connect.", Toast.LENGTH_LONG).show();
            }
        });
    }
}