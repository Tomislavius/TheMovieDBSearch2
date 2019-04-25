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
import com.example.tomislavrajic.themoviedbsearch2.utils.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.utils.SwipeToDeleteCallback;
import com.example.tomislavrajic.themoviedbsearch2.adapters.WatchedItemsRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBTVShows;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class WatchedTVShowsActivity extends AppCompatActivity implements WatchedItemsRecyclerViewAdapter.OnRemoveClickListener,
        MoreInfoClickListener, MoreInfoDialog.OnExternalWebPageClickListener {

    //region Fields
    private DBTVShows dbTVShows;
    private MoreInfoDialog moreInfoDialog;
    private Result result;

    @BindView(R.id.rv_watched_movies)
    RecyclerView mWatchedMoviesRecyclerView;

    @BindView(R.id.tv_empty_layout)
    TextView mEmptyLayout;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);

        ButterKnife.bind(this);

        dbTVShows = DBTVShows.getInstance();

        if (savedInstanceState != null && savedInstanceState.getSerializable(Result.TV_SHOW) != null) {
            result = (Result) savedInstanceState.getSerializable(Result.TV_SHOW);
            moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            moreInfoDialog.setData(result, Result.TV_SHOW);
            moreInfoDialog.setOnExternalWebPageClickListener(this);
            moreInfoDialog.show();
        }

        setLayoutDependingOnOrientation();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void setupRecyclerViewWatchedMovies(RealmResults<Result> watchedMovies) {
        WatchedItemsRecyclerViewAdapter watchedItemsRecyclerViewAdapter = new WatchedItemsRecyclerViewAdapter(watchedMovies,
                this, this);

        mWatchedMoviesRecyclerView.setAdapter(watchedItemsRecyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(watchedItemsRecyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(mWatchedMoviesRecyclerView);
    }

    @Override
    public void onMovieRemoved(int id) {
        dbTVShows.deleteItem(id);
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, String isMovie) {
        moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.result = movieResult;
        moreInfoDialog.setData(this.result, Result.TV_SHOW);
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
            outState.putSerializable(Result.TV_SHOW, result);
        }
    }

    @Override
    public void onIMDBClicked(String imdbID, String isMovie) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_IMDB_TITLE + imdbID));
        startActivity(browserIntent);
    }

    @Override
    public void onTMDBClicked(int movieID, String isMovie) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_TMDB_TV_SHOW + movieID));
        startActivity(browserIntent);
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
            Toast.makeText(this, R.string.swipe_to_remove, Toast.LENGTH_SHORT).show();
        } else {
            setupRecyclerViewWatchedMovies(watchedTVShows);
            layoutManager = new LinearLayoutManager(this);
            Toast.makeText(this, R.string.swipe_to_remove, Toast.LENGTH_SHORT).show();
        }
        mWatchedMoviesRecyclerView.setLayoutManager(layoutManager);
    }

    private void showEmptyLayout() {
        mEmptyLayout.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.add_watched_tv_shows, Toast.LENGTH_SHORT).show();
    }
}