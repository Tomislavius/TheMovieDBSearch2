package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.tomislavrajic.themoviedbsearch2.utils.DBHelper;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public abstract class BaseFragment extends Fragment implements MoviesRecyclerViewAdapter.OnBindClickListener,
        MoreInfoDialog.OnExternalWebPageClickListener {

    int page;
    private MoreInfoDialog moreInfoDialog;
    protected MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        page = 1;
        ButterKnife.bind(this, view);

        setLayoutDependingOnOrientation();

        DBHelper preferences = new DBHelper();
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(preferences.getWatchedMovies(), this);
        mRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        loadMovies();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutDependingOnOrientation();
    }

    private void setLayoutDependingOnOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }
        mRecyclerView.setLayoutManager(layoutManager);
    }

    protected abstract void loadMovies();

    public void refreshData() {
        DBHelper DBHelper = new DBHelper();
        moviesRecyclerViewAdapter.refreshWatchedMoviesList(DBHelper.getWatchedMovies());
    }

    @Override
    public void onMoreInfoClicked(MoviesResult movieResult) {
        moreInfoDialog = new MoreInfoDialog(Objects.requireNonNull(getContext()),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        moreInfoDialog.setData(movieResult);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onCheckedChanged(boolean isChecked, MoviesResult moviesResult) {
        DBHelper dbHelper = new DBHelper();
        if (isChecked) {
            dbHelper.saveMovie(moviesResult);
            Toast.makeText(getContext(), "Movie added to Watched Movies!", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.deleteMovie(moviesResult.getId());
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
            moreInfoDialog.setOnExternalWebPageClickListener(null);
        }
        super.onDestroyView();
    }
}