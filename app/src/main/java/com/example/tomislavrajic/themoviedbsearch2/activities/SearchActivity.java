package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.tomislavrajic.themoviedbsearch2.utils.MoreInfoClickListener;
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

public class SearchActivity extends AppCompatActivity implements MoreInfoClickListener,
        OnBindClickListener, MoreInfoDialog.OnExternalWebPageClickListener {

    public static final String STATUS_CODE = "status_code";
    public static final String RESULTS = "results";
    public static final String PAGE = "page";
    public static final String ERROR = "35";

    //region Fields
    private int page = 1;
    private Result result;
    private ArrayList<Result> results;
    private MoreInfoDialog moreInfoDialog;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;

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

        recyclerView.setVisibility(View.INVISIBLE);
        progressImage.setVisibility(View.VISIBLE);

        Glide.with(this)
                .asGif()
                .load(R.drawable.tmdb)
                .into(progressImage);

        loadResults(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        results = new ArrayList<>(0);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(searchRecyclerViewAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(RESULTS) != null) {
                results = (ArrayList<Result>) savedInstanceState.getSerializable(RESULTS);
                searchRecyclerViewAdapter.setData(results, true);
            }
            if (savedInstanceState.getInt(PAGE) != 0) {
                page = savedInstanceState.getInt(PAGE);
            }
            if (savedInstanceState.getSerializable(Result.MOVIE) != null) {
                moreInfoDialog = new MoreInfoDialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                moreInfoDialog.setOnExternalWebPageClickListener(this);
                result = (Result) savedInstanceState.getSerializable(Result.MOVIE);
                moreInfoDialog.setData(result, result.getMediaType());
                moreInfoDialog.show();
            }
        }

        setLayoutDependingOnOrientation();
        setSupportActionBar(searchToolbar);
    }

    private void loadResults(boolean shouldClearData) {
//TODO solve bug with changing userinput
        String userInput = inputSearch.getText().toString();
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getMovieTvShowPeople(BuildConfig.API_KEY, userInput, page).enqueue(new Callback<TMDBResponseData>() {
            @Override
            public void onResponse(@NonNull Call<TMDBResponseData> call, @NonNull Response<TMDBResponseData> response) {

                Utils.hideKeyboard(SearchActivity.this);
                progressImage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);


                if (response.isSuccessful()) {
                    searchRecyclerViewAdapter.setData(response.body().getResults(), shouldClearData);
                    results.addAll(response.body().getResults());
                } else if (inputSearch.length() == 0) {
                    String errorMessage = Utils.getErrorMessage(ERROR);
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
    public void onMoreInfoClicked(Result result, String isMovie) {
        moreInfoDialog = new MoreInfoDialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.result = result;
        moreInfoDialog.setData(this.result, isMovie);
        moreInfoDialog.setOnExternalWebPageClickListener(this);
        moreInfoDialog.show();
    }

    @Override
    public void onIMDBClicked(String imdbID, String mediaType) {
        if (mediaType.equals(Result.MOVIE) || mediaType.equals(Result.TV_SHOW)) {
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
        if (mediaType.equals(Result.MOVIE)) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(BuildConfig.BASE_URL_TMDB_MOVIE + iD));
            startActivity(browserIntent);
        } else if (mediaType.equals(Result.TV_SHOW)) {
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
        Utils.hideKeyboard(this);
        page++;
        loadResults(false);
    }

    private void setLayoutDependingOnOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        RecyclerView.LayoutManager layoutManager;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RESULTS, results);
        outState.putInt(PAGE, page);
        if (moreInfoDialog != null && moreInfoDialog.isShowing()) {
            outState.putSerializable(Result.MOVIE, result);
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