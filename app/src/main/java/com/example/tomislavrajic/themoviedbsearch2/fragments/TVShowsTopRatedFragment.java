package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.models.TMDBResponseData;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsTopRatedFragment extends TVShowsBaseFragment {

    @Override
    protected void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getTopRatedTVShowsResult(BuildConfig.API_KEY, page).enqueue(new Callback<TMDBResponseData>() {
            @Override
            public void onResponse(Call<TMDBResponseData> call, Response<TMDBResponseData> response) {
                if (response.isSuccessful()) {
                    moviesRecyclerViewAdapter.setData(response.body().getResults(), false);
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
            public void onFailure(@NonNull Call<TMDBResponseData> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to connect.", Toast.LENGTH_LONG).show();
            }
        });
    }
}