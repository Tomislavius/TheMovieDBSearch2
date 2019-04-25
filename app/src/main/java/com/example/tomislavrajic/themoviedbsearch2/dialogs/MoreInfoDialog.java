package com.example.tomislavrajic.themoviedbsearch2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
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


    private static final String STATUS_CODE = "status_code";

    //region Fields
    private StringBuilder titleAndYear = new StringBuilder();
    private OnExternalWebPageClickListener onExternalWebPageClickListener;

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
//    public MoreInfoDialog(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public MoreInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

//    protected MoreInfoDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//        init();
//    }
    //endregion

    public void setOnExternalWebPageClickListener(OnExternalWebPageClickListener onExternalWebPageClickListener) {
        this.onExternalWebPageClickListener = onExternalWebPageClickListener;
    }

    public void setData(Result result, String mediaType) {

        setPosterImage(result, mediaType);
        getExternalWebpage(result.getId(), mediaType);

        switch (mediaType) {
            case Result.MOVIE:

                setMoreInfoData(result, result.getTitle(), result.getReleaseDate());

                break;
            case Result.TV_SHOW:

                setMoreInfoData(result, result.getName(), result.getFirstAirDate());

                break;
            case Result.PERSON:

                title.setVisibility(View.INVISIBLE);
                textGenre.setVisibility(View.GONE);
                textUserScore.setVisibility(View.GONE);
                personName.setVisibility(View.VISIBLE);
                userScore.setVisibility(View.GONE);
                personName.setText(result.getName());

                break;
        }
    }

    private void setMoreInfoData(Result result, String getTitle, String releaseDate) {

        setUserScore(result.getVoteAverage());
        overview.setText(result.getOverview());
        genre.setText(Utils.getGenreList(result.getGenreIds()));
        titleAndYear.append(getTitle).append(" (").append(releaseDate.substring(6)).append(")");
        title.setText(titleAndYear);
    }

    private void setPosterImage(Result result, String mediaType) {

        if (mediaType.equals(Result.MOVIE) || mediaType.equals(Result.TV_SHOW)) {
            if (result.getPosterPath() == null ||
                    result.getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W300 + "null")) {
                Glide.with(getContext())
                        .load(R.drawable.no_image_available)
                        .into(posterImage);
            } else {
                Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + result.getPosterPath()).into(posterImage);
            }
        } else if (mediaType.equals(Result.PERSON)) {
            if (result.getProfilePath() == null ||
                    result.getProfilePath().equals(BuildConfig.POSTER_PATH_URL_W300 + "null")) {
                Glide.with(getContext())
                        .load(R.drawable.no_image_available)
                        .into(posterImage);
            } else {
                Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + result.getProfilePath()).into(posterImage);
            }
        }
    }

    private void getExternalWebpage(int iD, String mediaType) {

        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        if (mediaType.equals(Result.MOVIE)) {

            service.getExternalIDForMovie(iD, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
                @Override
                public void onResponse(@NonNull Call<ExternalID> call, @NonNull Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, Result.MOVIE));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, Result.MOVIE));
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
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mediaType.equals(Result.TV_SHOW)) {

            service.getExternalIDForTVShow(iD, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
                @Override
                public void onResponse(@NonNull Call<ExternalID> call, @NonNull Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, Result.TV_SHOW));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, Result.TV_SHOW));
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
                public void onResponse(@NonNull Call<ExternalID> call, @NonNull Response<ExternalID> response) {
                    if (response.isSuccessful()) {
                        String imdbId = response.body().getImdbId();
                        openIMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onIMDBClicked(imdbId, Result.PERSON));
                        openTMDBWebPage.setOnClickListener(v -> onExternalWebPageClickListener.onTMDBClicked(iD, Result.PERSON));
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
        userScore.setText(String.format("%s%%", String.valueOf(voteAverage)));
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