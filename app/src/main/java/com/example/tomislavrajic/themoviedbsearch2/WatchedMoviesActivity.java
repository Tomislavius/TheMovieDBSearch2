package com.example.tomislavrajic.themoviedbsearch2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomislavrajic.themoviedbsearch2.adapters.WatchedMoviesRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.example.tomislavrajic.themoviedbsearch2.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

public class WatchedMoviesActivity extends AppCompatActivity implements WatchedMoviesRecyclerViewAdapter.OnRemoveClickListener,
        WatchedMoviesRecyclerViewAdapter.MoreInfoClickListener, MoreInfoDialog.OnIMDBClickListener {

    private DBHelper DBHelper;
    private WatchedMoviesRecyclerViewAdapter watchedMoviesRecyclerViewAdapter;
    private MoreInfoDialog moreInfoDialog;
    @BindView(R.id.rv_watched_movies)
    RecyclerView mWatchedMoviesRecyclerView;
    @BindView(R.id.tv_empty_layout)
    TextView mEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);
        ButterKnife.bind(this);

        DBHelper = new DBHelper();
        RealmResults<MoviesResult> watchedMovies = DBHelper.getWatchedMovies();

        if (watchedMovies.isEmpty()) {
            showEmptyLayout();
        } else {
            setupRecyclerViewWatchedMovies(watchedMovies);
            Toast.makeText(this, "Swipe to remove!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void setupRecyclerViewWatchedMovies(RealmResults<MoviesResult> watchedMovies) {
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
        DBHelper.deleteMovie(id);
    }

    @Override
    public void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID,
                                  String title, String releaseDate, RealmList<Integer> movieList) {
        moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        moreInfoDialog.setData(overview, posterPath, voteAverage, movieID, title, releaseDate, movieList);
        moreInfoDialog.setOnIMDBClickListener(this);
        moreInfoDialog.show();
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
}

