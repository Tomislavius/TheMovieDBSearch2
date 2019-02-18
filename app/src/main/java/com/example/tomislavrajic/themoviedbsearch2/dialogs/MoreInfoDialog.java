package com.example.tomislavrajic.themoviedbsearch2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.ExternalID;
import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreInfoDialog extends Dialog {

    private OnExternalWebPageClickListener onExternalWebPageClickListener;

    //region View
    @BindView(R.id.bt_dismiss)
    Button dismiss;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.tv_user_score)
    TextView userScore;
    @BindView(R.id.tv_show_more_genre)
    TextView tvShowMoreGenre;
    @BindView(R.id.tv_show_more_title)
    TextView showMoreTitle;
    @BindView(R.id.stats_progressbar_red)
    ProgressBar progressBarUserScoreRed;
    @BindView(R.id.background_progressbar_red)
    ProgressBar backgroundProgressBarRed;
    @BindView(R.id.stats_progressbar_yellow)
    ProgressBar progressBarUserScoreYellow;
    @BindView(R.id.background_progressbar_yellow)
    ProgressBar backgroundProgressBarYellow;
    @BindView(R.id.stats_progressbar_green)
    ProgressBar progressBarUserScoreGreen;
    @BindView(R.id.background_progressbar_green)
    ProgressBar backgroundProgressBarGreen;
    @BindView(R.id.ib_open_imdb)
    ImageButton openIMDB;
    @BindView(R.id.ib_open_tmdb)
    ImageButton openTMDB;
    @BindView(R.id.iv_show_more_movie)
    ImageView mPosterPath;
    //endregion

    //region Default constructors
    public MoreInfoDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MoreInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected MoreInfoDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }
    //endregion

    public void setOnExternalWebPageClickListener(OnExternalWebPageClickListener onExternalWebPageClickListener) {
        this.onExternalWebPageClickListener = onExternalWebPageClickListener;
    }

    public void setData(MoviesResult movieResult) {
        Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + movieResult.getPosterPath()).into(mPosterPath);
        StringBuilder titleAndYear = new StringBuilder();
        titleAndYear.append(movieResult.getTitle()).append(" (").append(movieResult.getReleaseDate().substring(6)).append(")");
        setUserScore(movieResult.getVoteAverage());
        tvOverview.setText(movieResult.getOverview());
        tvShowMoreGenre.setText(Utils.getGenreList(movieResult.getGenreIds()));
        showMoreTitle.setText(titleAndYear);
        getExternalWebpage(movieResult.getId());
    }

    private void getExternalWebpage(int movieID) {
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);
        service.getExternalID(movieID, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
            @Override
            public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                if (response.code() == 200) {
                    String imdbId = response.body().getImdbId();
                    openIMDB.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId));
                    openTMDB.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(movieID));
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ExternalID> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to connect.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUserScore(int voteAverage) {

        if (voteAverage == 0) {
            backgroundProgressBarYellow.setVisibility(View.VISIBLE);
            userScore.setText(R.string.not_rated);
            return;
        } else if (voteAverage >= 1 && voteAverage <= 39) {
            backgroundProgressBarRed.setVisibility(View.VISIBLE);
            progressBarUserScoreRed.setVisibility(View.VISIBLE);
            progressBarUserScoreRed.setProgress(voteAverage);
        } else if (voteAverage >= 40 && voteAverage <= 69) {
            backgroundProgressBarYellow.setVisibility(View.VISIBLE);
            progressBarUserScoreYellow.setVisibility(View.VISIBLE);
            progressBarUserScoreYellow.setProgress(voteAverage);
        } else if (voteAverage >= 70 && voteAverage <= 100) {
            backgroundProgressBarGreen.setVisibility(View.VISIBLE);
            progressBarUserScoreGreen.setVisibility(View.VISIBLE);
            progressBarUserScoreGreen.setProgress(voteAverage);
        }
        userScore.setText(String.valueOf(voteAverage) + "%");
    }

    private void init() {
        setContentView(R.layout.show_more);
        ButterKnife.bind(this);
        dismiss.setOnClickListener(v -> {
            setOnExternalWebPageClickListener(null);
            dismiss();
        });
    }

    public interface OnExternalWebPageClickListener {

        void onIMDBClicked(String imdbID);

        void onTMDBClicked(int movieID);
    }
}