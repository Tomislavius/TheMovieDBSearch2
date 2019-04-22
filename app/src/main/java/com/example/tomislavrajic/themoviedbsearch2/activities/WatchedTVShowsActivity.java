package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.SwipeToDeleteCallback;
import com.example.tomislavrajic.themoviedbsearch2.adapters.WatchedMoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBTVShows;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class WatchedTVShowsActivity extends AppCompatActivity implements WatchedMoviesRecyclerViewAdapter.OnRemoveClickListener,
        MoreInfoClickListener, MoreInfoDialog.OnExternalWebPageClickListener {

    private WatchedMoviesRecyclerViewAdapter watchedMoviesRecyclerViewAdapter;
    private MoreInfoDialog moreInfoDialog;
    private Result movieResult;
    private DBTVShows dbTVShows;

    @BindView(R.id.rv_watched_movies)
    RecyclerView mWatchedMoviesRecyclerView;

    @BindView(R.id.tv_empty_layout)
    TextView mEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);
        ButterKnife.bind(this);

        dbTVShows = DBTVShows.getInstance();

        if (savedInstanceState != null && savedInstanceState.getSerializable("Movie") != null) {
            movieResult = (Result) savedInstanceState.getSerializable("Movie");
            moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            moreInfoDialog.setData(movieResult, false);
            moreInfoDialog.setOnExternalWebPageClickListener(this);
            moreInfoDialog.show();
        }

        setLayoutDependingOnOrientation();
    }

    private void setLayoutDependingOnOrientation() {
        RealmResults<Result> watchedTVShows = dbTVShows.getWatchedList();
        int currentOrientation = getResources().getConfiguration().orientation;
        RecyclerView.LayoutManager layoutManager = null;
        if (watchedTVShows.isEmpty()) {
            showEmptyLayout();
        } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupRecyclerViewWatchedMovies(watchedTVShows);
            layoutManager = new GridLayoutManager(this, 2);
            Toast.makeText(this, "Swipe to remove!", Toast.LENGTH_SHORT).show();
        } else {
            setupRecyclerViewWatchedMovies(watchedTVShows);
            layoutManager = new LinearLayoutManager(this);
            Toast.makeText(this, "Swipe to remove!", Toast.LENGTH_SHORT).show();
        }
        mWatchedMoviesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void setupRecyclerViewWatchedMovies(RealmResults<Result> watchedMovies) {
        watchedMoviesRecyclerViewAdapter = new WatchedMoviesRecyclerViewAdapter(watchedMovies, this, this);
        mWatchedMoviesRecyclerView.setAdapter(watchedMoviesRecyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(watchedMoviesRecyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(mWatchedMoviesRecyclerView);
    }

    private void showEmptyLayout() {
        mEmptyLayout.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Add watched movies!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieRemoved(int id) {
        dbTVShows.deleteItem(id);
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, boolean isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.movieResult = movieResult;
        moreInfoDialog.setData(this.movieResult, false);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (moreInfoDialog != null) {
            moreInfoDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (moreInfoDialog != null && moreInfoDialog.isShowing()) {
            outState.putSerializable("Movie", movieResult);
        }
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
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_TMDB + movieID));
        startActivity(browserIntent);
    }
}