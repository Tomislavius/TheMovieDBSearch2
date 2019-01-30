package com.example.tomislavrajic.themoviedbsearch2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private OnIMDBClickListener onIMDBClickListener;

    //region View
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

    public void setOnIMDBClickListener(OnIMDBClickListener onIMDBClickListener) {
        this.onIMDBClickListener = onIMDBClickListener;
    }

    public void setData(String overview, String posterPath, int voteAverage, int movieID) {
        Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + posterPath).into(mPosterPath);
        progressBarUserScore.setProgress(voteAverage);
        if (voteAverage == 0) {
            userScore.setText(R.string.not_rated);
        } else {
            userScore.setText(String.valueOf(voteAverage) + "%");
        }
        tvOverview.setText(overview);

        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getExternalID(movieID, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
            @Override
            public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                //TODO Handle unsuccessful response
                String imdbId = response.body().getImdbId();
                openIMDB.setOnClickListener(v -> onIMDBClickListener.onIMDBClicked(imdbId));
            }

            @Override
            public void onFailure(Call<ExternalID> call, Throwable t) {
                //TODO Handle no response
            }
        });
    }

    private void init() {
        setContentView(R.layout.show_more);
        ButterKnife.bind(this);
        dismiss.setOnClickListener(v -> {
            setOnIMDBClickListener(null);
            dismiss();
        });
    }

    public interface OnIMDBClickListener {
        void onIMDBClicked(String imdbID);
    }
}