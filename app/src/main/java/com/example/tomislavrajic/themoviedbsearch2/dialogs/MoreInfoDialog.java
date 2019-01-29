package com.example.tomislavrajic.themoviedbsearch2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.ExternalID;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreInfoDialog extends Dialog {

    private OnIMDBClickedListener onIMDBClickedListener;

    @BindView(R.id.bt_dismiss)
    Button dismiss;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.tv_user_score)
    TextView userScore;
    @BindView(R.id.stats_progressbar)
    ProgressBar progressBarUserScore;
    @BindView(R.id.ib_open_imdb)
    ImageButton openIMDB;

    public MoreInfoDialog(@NonNull Context context) {
        super(context);
    }

    public MoreInfoDialog(@NonNull Context context, int themeResId, OnIMDBClickedListener onIMDBClickedListener) {
        super(context, themeResId);
        this.onIMDBClickedListener = onIMDBClickedListener;
        init();
    }

    protected MoreInfoDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public void setData(String overview, String posterPath, int voteAverage, int movieID) {
        ButterKnife.bind(this);
        ImageView mPosterPath = findViewById(R.id.iv_show_more_movie);
        Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + posterPath).into(mPosterPath);
        progressBarUserScore.setProgress(voteAverage);
        if (voteAverage == 0) {
            userScore.setText("NR");
        } else {
            userScore.setText(String.valueOf(voteAverage) + "%");
        }
        if (overview.length() < 500) {
            tvOverview.setText(overview);
        } else {
            tvOverview.setText(overview.substring(0, 500) + "...");
        }
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getExternalID(movieID, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
            @Override
            public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                String imdbId = response.body().getImdbId();
                openIMDB.setOnClickListener(v -> onIMDBClickedListener.onIMDBClicked(imdbId));
            }

            @Override
            public void onFailure(Call<ExternalID> call, Throwable t) {

            }
        });
    }

    private void init() {
        setContentView(R.layout.show_more);
        ButterKnife.bind(this);
        dismiss.setOnClickListener(v -> dismiss());
    }

    public interface OnIMDBClickedListener {
        void onIMDBClicked(String imdbID);
    }
}
