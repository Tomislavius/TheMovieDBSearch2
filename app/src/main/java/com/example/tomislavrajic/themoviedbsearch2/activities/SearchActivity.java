package com.example.tomislavrajic.themoviedbsearch2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.adapters.SearchRecyclerViewAdapter;
import com.example.tomislavrajic.themoviedbsearch2.models.TMDBResponseData;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    //region fields

    public static final String STATUS_CODE = "status_code";

    String userInput;

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

        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getMovieTvShowPeople(BuildConfig.API_KEY, userInput).enqueue(new Callback<TMDBResponseData>() {
            @Override
            public void onResponse(Call<TMDBResponseData> call, Response<TMDBResponseData> response) {

                progressImage.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    recyclerView.setAdapter(new SearchRecyclerViewAdapter(response.body().getResults()));
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = Utils.getErrorMessage(jObjError.getString(STATUS_CODE));
                        Toast.makeText(SearchActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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

        ButterKnife.bind(this);

        setSupportActionBar(searchToolbar);
    }
}