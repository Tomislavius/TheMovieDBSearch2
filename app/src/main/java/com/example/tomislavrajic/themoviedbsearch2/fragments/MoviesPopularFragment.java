package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;
import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.WatchedSharedPreferences;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPopularFragment extends BaseFragment implements MoviesRecyclerViewAdapter.LoadMoreCallback,
        MoviesRecyclerViewAdapter.MoreInfoClickListener, MoviesRecyclerViewAdapter.OnCheckedChangeListener,
        MoreInfoDialog.OnIMDBClickedListener {

    private int page;
    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    @BindView(R.id.rv_popular_movies)
    RecyclerView mRecyclerView;
    private MoreInfoDialog moreInfoDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_popular, container, false);

//        moreInfoDialog = new MoreInfoDialog(getContext(),this);

        page = 1;
        ButterKnife.bind(this, view);

        WatchedSharedPreferences preferences = new WatchedSharedPreferences(getContext());
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(preferences.getWatchedMovies(),
                this, this, this);
        mRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        loadMovies();

        return view;
    }

    private void loadMovies() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getPopularMoviesResult(BuildConfig.API_KEY, page).enqueue(new Callback<Movies>() {
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

    @Override
    public void onLoadMoreClicked() {
        page++;
        loadMovies();
    }

    @Override
    public void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID) {
        moreInfoDialog = new MoreInfoDialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        moreInfoDialog.setData(overview, posterPath, voteAverage, movieID);
        moreInfoDialog.setOnIMDBClickedListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onCheckedChanged(boolean isChecked, MoviesResult moviesResult) {
        WatchedSharedPreferences preferences = new WatchedSharedPreferences(getContext());
        if (isChecked) {
            preferences.saveMovie(moviesResult);
        } else {
            preferences.deleteMovie(moviesResult.getId());
        }
    }

    @Override
    public void onDestroyView() {
        if (moreInfoDialog != null){
            moreInfoDialog.setOnIMDBClickedListener(null);
        }
        super.onDestroyView();
    }

    @Override
    MoviesRecyclerViewAdapter getAdapter() {
        return moviesRecyclerViewAdapter;
    }

    @Override
    public void onIMDBClicked(String imdbID) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_IMDB + imdbID));
        startActivity(browserIntent);
    }
}