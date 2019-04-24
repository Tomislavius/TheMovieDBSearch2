package com.example.tomislavrajic.themoviedbsearch2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.networking.ServiceGenerator;
import com.example.tomislavrajic.themoviedbsearch2.networking.TheMovieDBAPI;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreInfoDialog extends Dialog {

    private OnExternalWebPageClickListener onExternalWebPageClickListener;

    //region Fields
    public static final String STATUS_CODE = "status_code";

    private StringBuilder titleAndYear = new StringBuilder();

    @BindView(R.id.tv_overview)
    TextView overview;

    @BindView(R.id.tv_user_score)
    TextView userScore;
    @BindView(R.id.user_score)
    TextView textUserScore;

    @BindView(R.id.tv_show_more_genre)
    TextView genre;

    @BindView(R.id.tv_show_more_title)
    TextView title;

    @BindView(R.id.show_more_genre)
    TextView textGenre;

    @BindView(R.id.tv_person_name)
    TextView personName;

    @BindView(R.id.bt_dismiss)
    Button dismissButton;

    @BindView(R.id.ib_open_imdb)
    ImageButton openIMDBWebPage;

    @BindView(R.id.ib_open_tmdb)
    ImageButton openTMDBWebPage;

    @BindView(R.id.iv_show_more_movie)
    ImageView posterImage;

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

    public void setData(Result movieResult, String isMovie) {
        if (isMovie.equals("movie")) {

            setMoreInfoData(movieResult, movieResult.getTitle(), movieResult.getReleaseDate(), "movie");

        } else if (isMovie.equals("tv")) {

            setMoreInfoData(movieResult, movieResult.getName(), movieResult.getFirstAirDate(), "tv");

        } else if (isMovie.equals("person")) {

//            setMoreInfoData(movieResult, movieResult.getName(), movieResult.getFirstAirDate(), "person");
            title.setVisibility(View.INVISIBLE);
            textGenre.setVisibility(View.GONE);
            textUserScore.setVisibility(View.GONE);
            personName.setVisibility(View.VISIBLE);
            userScore.setVisibility(View.GONE);
            personName.setText(movieResult.getName());
            Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + movieResult.getProfilePath()).into(posterImage);
            getExternalWebpage(movieResult.getId(), movieResult.getMediaType());

            //TODO make condition for person
        }
    }

    private void setMoreInfoData(Result movieResult, String getTitle, String releaseDate, String mediaType) {

        Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + movieResult.getPosterPath()).into(posterImage);
        getExternalWebpage(movieResult.getId(), mediaType);
        setUserScore(movieResult.getVoteAverage());
        overview.setText(movieResult.getOverview());
        genre.setText(Utils.getGenreList(movieResult.getGenreIds()));
        titleAndYear.append(getTitle).append(" (").append(releaseDate.substring(6)).append(")");
        title.setText(titleAndYear);

    }

    private void getExternalWebpage(int iD, String mediaType) {

        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        if (mediaType.equals("movie")) {

            service.getExternalIDForMovie(iD, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
                @Override
                public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, "movie"));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, "movie"));
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = Utils.getErrorMessage(jObjError.getString(STATUS_CODE));
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ExternalID> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mediaType.equals("tv")) {

            service.getExternalIDForTVShow(iD, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
                @Override
                public void onResponse(@NonNull Call<ExternalID> call, @NonNull Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, "tv"));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, "tv"));
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = Utils.getErrorMessage(jObjError.getString(STATUS_CODE));
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ExternalID> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                }
            });
        } else {

            service.getExternalIDForPerson(iD, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
                @Override
                public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, "person"));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, "person"));
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = Utils.getErrorMessage(jObjError.getString(STATUS_CODE));
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ExternalID> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                }
            });
            //TODO solve for person
        }
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
        dismissButton.setOnClickListener(v -> {
            setOnExternalWebPageClickListener(null);
            dismiss();
        });
    }

    public interface OnExternalWebPageClickListener {

        void onIMDBClicked(String imdbID, String isMovie);

        void onTMDBClicked(int movieID, String isMovie);
    }
}