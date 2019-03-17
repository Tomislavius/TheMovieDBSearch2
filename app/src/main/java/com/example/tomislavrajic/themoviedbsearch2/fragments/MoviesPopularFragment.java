package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPopularFragment extends MoviesBaseFragment {

    protected void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getPopularMoviesResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                if (response.isSuccessful()) {
                    moviesRecyclerViewAdapter.setData(response.body().getResults(), true);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = Utils.getErrorMessage(jObjError.getString("status_code"));
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to connect.", Toast.LENGTH_LONG).show();
            }
        });
    }
}