package com.example.tomislavrajic.themoviedbsearch2.fragments;

import android.content.res.Configuration;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.activities.MoviesActivity;
import com.example.tomislavrajic.themoviedbsearch2.activities.TVShowsActivity;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBHelper;
import com.example.tomislavrajic.themoviedbsearch2.utils.OnBindClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements OnBindClickListener {

    public static final String STATUS_CODE = "status_code";

    //region Fields
    int page;
    protected MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.progress_image_iv)
    ImageView progressImage;

    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this, view);
        page = 1;

        if (getActivity() instanceof MoviesActivity) {
            moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getDBHelper().getWatchedList(),
                    this, (MoviesActivity) getActivity());
        } else {
            moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getDBHelper().getWatchedList(),
                    this, (TVShowsActivity) getActivity());
        }

        recyclerView.setAdapter(moviesRecyclerViewAdapter);

        recyclerView.setVisibility(View.INVISIBLE);
        progressImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .asGif()
                .load(R.drawable.tmdb)
                .into(progressImage);

        setLayoutDependingOnOrientation();

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
        recyclerView.setLayoutManager(layoutManager);
    }

    protected abstract void loadMovies();

    abstract DBHelper getDBHelper();

    public void refreshData() {
        moviesRecyclerViewAdapter.refreshWatchedMoviesList(getDBHelper().getWatchedList());
    }

    @Override
    public void onCheckedChanged(boolean isChecked, Result result) {

        if (isChecked) {
            getDBHelper().saveItem(result);
            Toast.makeText(getContext(), R.string.item_added, Toast.LENGTH_SHORT).show();
        } else {
            getDBHelper().deleteItem(result.getId());
            Toast.makeText(getContext(), R.string.item_removed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadMoreClicked() {
        page++;
        loadMovies();
    }
}