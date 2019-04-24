package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.adapters.SearchRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.dialogs.MoreInfoDialog;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.models.TMDBResponseData;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.OnBindClickListener;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements MoreInfoClickListener
        , OnBindClickListener, MoreInfoDialog.OnExternalWebPageClickListener {

    //region Fields
    public static final String STATUS_CODE = "status_code";

    private int page;

    private String userInput;
    private ArrayList<Result> results = new ArrayList<>(0);
    private MoreInfoDialog moreInfoDialog; //TODO Is this necessary?
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @BindView(R.id.search_toolbar_tb)
    Toolbar searchToolbar;

    @BindView(R.id.user_input_et)
    EditText inputSearch;

    @BindView((R.id.progress_image_iv))
    ImageView progressImage;

    @BindView(R.id.multi_search_rv)
    RecyclerView recyclerView;
    //endregion

    @OnClick(R.id.search_bt)
    void onSearchClicked() {

        progressImage.setVisibility(View.VISIBLE);

        Glide.with(this)
                .asGif()
                .load(R.drawable.tmdb)
                .into(progressImage);

        userInput = inputSearch.getText().toString();

        loadResults();
    }

    private void loadResults() {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getMovieTvShowPeople(BuildConfig.API_KEY, userInput, page).enqueue(new Callback<TMDBResponseData>() {
            @Override
            public void onResponse(Call<TMDBResponseData> call, Response<TMDBResponseData> response) {

                progressImage.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    searchRecyclerViewAdapter.setData(response.body().getResults());

//                    results.addAll(0, response.body().getResults());

                } else if (inputSearch.length() == 0) {
                    String errorMessage = Utils.getErrorMessage("35");
                    Toast.makeText(SearchActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errorMessage = Utils.getErrorMessage(jObjError.getString(STATUS_CODE));
                    Toast.makeText(SearchActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TMDBResponseData> call, Throwable t) {
                Toast.makeText(SearchActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        if (savedInstanceState != null && savedInstanceState.getSerializable("results") != null) {
//            results = (ArrayList<Result>) savedInstanceState.getSerializable("results");
//            moreInfoDialog.setData(results, "results");
//            moreInfoDialog.show();
//        }

        page = 1;

        ButterKnife.bind(this);

        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(results, this, this);

        recyclerView.setAdapter(searchRecyclerViewAdapter);

        if (results.size() != 0) {
            Toast.makeText(this, "NOT EMPTY", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "EMPTY", Toast.LENGTH_SHORT).show();
        }

        setLayoutDependingOnOrientation();

        setSupportActionBar(searchToolbar);
    }

    @Override
    public void onMoreInfoClicked(Result movieResult, String isMovie) {
        MoreInfoDialog moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Result movieResult1 = movieResult;
        moreInfoDialog.setData(movieResult1, isMovie);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onIMDBClicked(String imdbID, String mediaType) {
        if (mediaType.equals("movie") || mediaType.equals("tv")) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_IMDB_TITLE + imdbID));
            startActivity(browserIntent);
        } else {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_IMDB_NAME + imdbID));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onTMDBClicked(int iD, String mediaType) {
        if (mediaType.equals("movie")) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_MOVIE + iD));
            startActivity(browserIntent);
        } else if (mediaType.equals("tv")) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_TV_SHOW + iD));
            startActivity(browserIntent);
        } else {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_PERSON + iD));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onLoadMoreClicked() {
        page++;
        loadResults();
    }

    private void setLayoutDependingOnOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCheckedChanged(boolean isChecked, Result result) {

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("results", results);
//    }

    @Override
    public void onDestroy() {
        if (moreInfoDialog != null) {
            moreInfoDialog.setOnExternalWebPageClickListener(null);
            moreInfoDialog.dismiss();
        }
        super.onDestroy();
    }
}