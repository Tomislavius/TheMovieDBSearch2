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
import com.example.tomislavrajic.themoviedbsearch2.utils.WatchedSharedPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchedMoviesActivity extends AppCompatActivity implements WatchedMoviesRecyclerViewAdapter.OnRemoveClickListener,
        WatchedMoviesRecyclerViewAdapter.MoreInfoClickListener, MoreInfoDialog.OnIMDBClickedListener {

    @BindView(R.id.rv_watched_movies)
    RecyclerView mWatchedMoviesRecyclerView;
    @BindView(R.id.tv_empty_layout)
    TextView mEmptyLayout;
    private WatchedSharedPreferences watchedSharedPreferences;
    private WatchedMoviesRecyclerViewAdapter watchedMoviesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);
        ButterKnife.bind(this);

        watchedSharedPreferences = new WatchedSharedPreferences(this);
        ArrayList<MoviesResult> watchedMovies = watchedSharedPreferences.getWatchedMovies();

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

    private void setupRecyclerViewWatchedMovies(ArrayList<MoviesResult> watchedMovies) {
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
        watchedSharedPreferences.deleteMovie(id);
    }

    @Override
    public void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID) {
        MoreInfoDialog moreInfoDialog = new MoreInfoDialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen,this);
        moreInfoDialog.setData(overview, posterPath, voteAverage, movieID);
        moreInfoDialog.show();
    }

    @Override
    public void onIMDBClicked(String imdbID) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BuildConfig.BASE_URL_IMDB + imdbID));
        startActivity(browserIntent);
    }
}
