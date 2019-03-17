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
import com.example.tomislavrajic.themoviedbsearch2.utils.DBMovies;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBTVShows;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements MoviesRecyclerViewAdapter.OnBindClickListener,
        MoreInfoDialog.OnExternalWebPageClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    int page;
    protected MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    private MoreInfoDialog moreInfoDialog;
    private MoviesResult movieResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        page = 1;
        ButterKnife.bind(this, view);

        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getDBHelper().getWatchedList(), this);
        mRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        setLayoutDependingOnOrientation();

        loadMovies();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutDependingOnOrientation();
        if (moreInfoDialog != null) {
            moreInfoDialog.dismiss();
        }
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

    abstract DBHelper getDBHelper();

    public void refreshData() {
        moviesRecyclerViewAdapter.refreshWatchedMoviesList(getDBHelper().getWatchedList());
    }

    @Override
    public void onMoreInfoClicked(MoviesResult movieResult, boolean isMovie) {
        moreInfoDialog = new MoreInfoDialog(Objects.requireNonNull(getContext()),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, isMovie);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onCheckedChanged(boolean isChecked, MoviesResult moviesResult) {

        if (isChecked) {
            getDBHelper().saveItem(moviesResult);
            Toast.makeText(getContext(), "Item added!", Toast.LENGTH_SHORT).show();
        } else {
            getDBHelper().deleteItem(moviesResult.getId());
            Toast.makeText(getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadMoreClicked() {
        page++;
        loadMovies();
    }

    @Override
    public void onIMDBClicked(String imdbID, boolean isMovie) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_IMDB + imdbID));
        startActivity(browserIntent);
    }

    @Override
    public void onTMDBClicked(int movieID, boolean isMovie) {
        if (isMovie) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB + movieID));
            startActivity(browserIntent);
        } else {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_TV_SHOW + movieID));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onDestroy() {
        if (moreInfoDialog != null) {
            moreInfoDialog.setOnExternalWebPageClickListener(null);
            moreInfoDialog.dismiss();
        }
        super.onDestroy();
    }

}