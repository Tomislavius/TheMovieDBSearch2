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
//TODO Remove unused imorts
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
        init();
    }

    //TODO Create method for setting imdb click listener
    public MoreInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
                init();
    }

    protected MoreInfoDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public void setOnIMDBClickedListener(OnIMDBClickedListener onIMDBClickedListener) {
        this.onIMDBClickedListener = onIMDBClickedListener;
    }

    public void setData(String overview, String posterPath, int voteAverage, int movieID) {
        //TODO Why is view binding inside setData()?
        ImageView mPosterPath = findViewById(R.id.iv_show_more_movie);
        Glide.with(getContext()).load(BuildConfig.POSTER_PATH_URL_W300 + posterPath).into(mPosterPath);
        progressBarUserScore.setProgress(voteAverage);
        //TODO Use string resources
        if (voteAverage == 0) {
            userScore.setText("NR");
        } else {
            userScore.setText(String.valueOf(voteAverage) + "%");
        }
        if (overview.length() < 500) {
            tvOverview.setText(overview);
        } else {
            //TODO use maxLines in xml in combination with ellipsize = end
            tvOverview.setText(overview.substring(0, 500) + "...");
        }
        TheMovieDBAPI service = ServiceGenerator.createService(TheMovieDBAPI.class);

        service.getExternalID(movieID, BuildConfig.API_KEY).enqueue(new Callback<ExternalID>() {
            @Override
            public void onResponse(Call<ExternalID> call, Response<ExternalID> response) {
                //TODO Handle unsuccessful response
                String imdbId = response.body().getImdbId();
                openIMDB.setOnClickListener(v -> onIMDBClickedListener.onIMDBClicked(imdbId));
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
            setOnIMDBClickedListener(null);
            dismiss();
        });
    }

    public interface OnIMDBClickedListener {
        void onIMDBClicked(String imdbID);
    }
}
