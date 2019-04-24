package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.fragments.BaseFragment;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements MoreInfoDialog.OnExternalWebPageClickListener {

    //region Fields
    public static final int REQUEST_CODE = 1313;
    public static final String MOVIE = "Movie";

    MoreInfoDialog moreInfoDialog;
    Result movieResult;

    @BindView(R.id.test)
    TabLayout mTabLayout;

    @BindView(R.id.vp_top_rated)
    ViewPager mViewPager;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (moreInfoDialog != null && moreInfoDialog.isShowing()) {
            outState.putSerializable(MOVIE, movieResult);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.watched_movies:
                intent = new Intent(this, WatchedMoviesActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            case R.id.watched_tv_shows:
                intent = new Intent(this, WatchedTVShowsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    if (fragments.get(i) instanceof BaseFragment) {
                        ((BaseFragment) fragments.get(i)).refreshData();
                    }
                }
            }
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
        if (isMovie.equals("movie")) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_MOVIE + movieID));
            startActivity(browserIntent);
        } else if (isMovie.equals("tv")) {
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