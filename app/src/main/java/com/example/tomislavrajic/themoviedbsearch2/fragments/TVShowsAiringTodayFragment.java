package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsAiringTodayFragment extends TVShowsBaseFragment {

    @Override
    protected void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);
        service.getAiringTodayShowsResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.code() == 200) {
                    moviesRecyclerViewAdapter.setData(response.body().getResults(), false);
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(getContext(), "Internal error: Something went wrong, contact TMDb.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }
}