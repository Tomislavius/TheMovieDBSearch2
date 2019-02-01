package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.WatchedSharedPreferences;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseFragment extends Fragment implements MoviesRecyclerViewAdapter.LoadMoreCallback,
        MoviesRecyclerViewAdapter.MoreInfoClickListener, MoviesRecyclerViewAdapter.OnCheckedChangeListener,
        MoreInfoDialog.OnIMDBClickListener {

    private MoreInfoDialog moreInfoDialog;
//    private int page = 1;

    abstract MoviesRecyclerViewAdapter getAdapter();

    public void refreshData() {
        WatchedSharedPreferences watchedSharedPreferences = new WatchedSharedPreferences(getContext());
        getAdapter().refreshWatchedMoviesList(watchedSharedPreferences.getWatchedMovies());
    }

//    protected void loadMovies() {
//        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);
//
//        service.getNowPlayingMoviesResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
//            @Override
//            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
//                assert response.body() != null;
//                getAdapter().setData(response.body().getResults());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
//
//            }
//        });
//    }
public void getMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID) {
    moreInfoDialog = new MoreInfoDialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    moreInfoDialog.setData(overview, posterPath, voteAverage, movieID);
    moreInfoDialog.setOnIMDBClickListener(this);
    moreInfoDialog.show();
}
}
