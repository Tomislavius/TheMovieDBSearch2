package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.tomislavrajic.themoviedbsearch2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //region Views
    @BindView(R.id.bt_movies)
    Button mButtonMovies;

    @BindView(R.id.bt_tv_shows)
    Button mButtonTVShows;

    @BindView(R.id.bt_search)
    Button mButtonSearch;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mButtonMovies.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
            startActivity(intent);
        });

        mButtonTVShows.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TVShowsActivity.class);
            startActivity(intent);
        });
    }
}