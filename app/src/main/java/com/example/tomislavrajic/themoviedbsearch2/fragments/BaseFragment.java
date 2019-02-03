package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.example.tomislavrajic.themoviedbsearch2.utils.WatchedSharedPreferences;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements MoviesRecyclerViewAdapter.OnBindClickListener,
        MoreInfoDialog.OnIMDBClickListener {

    int page;
    private MoreInfoDialog moreInfoDialog;
    protected MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        page = 1;
        ButterKnife.bind(this, view);

        WatchedSharedPreferences preferences = new WatchedSharedPreferences(getContext());
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(preferences.getWatchedMovies(), this);
        mRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        loadMovies();

        return view;
    }

    protected abstract void loadMovies();

    public void refreshData() {
        WatchedSharedPreferences watchedSharedPreferences = new WatchedSharedPreferences(getContext());
        moviesRecyclerViewAdapter.refreshWatchedMoviesList(watchedSharedPreferences.getWatchedMovies());
    }

    @Override
    public void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID,
                                  String title, String releaseDate) {
        moreInfoDialog = new MoreInfoDialog(Objects.requireNonNull(getContext()),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        moreInfoDialog.setData(overview, posterPath, voteAverage, movieID, title, releaseDate);
        moreInfoDialog.setOnIMDBClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onCheckedChanged(boolean isChecked, MoviesResult moviesResult) {
        WatchedSharedPreferences preferences = new WatchedSharedPreferences(getContext());
        if (isChecked) {
            preferences.saveMovie(moviesResult);
            Toast.makeText(getContext(), "Movie added to Watched Movies!", Toast.LENGTH_SHORT).show();
        } else {
            preferences.deleteMovie(moviesResult.getId());
            Toast.makeText(getContext(), "Movie removed from Watched Movies!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadMoreClicked() {
        page++;
        loadMovies();
    }

    @Override
    public void onIMDBClicked(String imdbID) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_IMDB + imdbID));
        startActivity(browserIntent);
    }

    @Override
    public void onTMDBClicked(int movieID) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_TMDB + movieID));
        startActivity(browserIntent);
    }

    @Override
    public void onDestroyView() {
        if (moreInfoDialog != null) {
            moreInfoDialog.setOnIMDBClickListener(null);
        }
        super.onDestroyView();
    }
}
