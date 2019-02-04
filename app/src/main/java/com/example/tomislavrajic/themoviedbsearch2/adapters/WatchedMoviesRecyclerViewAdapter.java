package com.example.tomislavrajic.themoviedbsearch2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.MoviesResult;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

public class WatchedMoviesRecyclerViewAdapter extends
        RecyclerView.Adapter<WatchedMoviesRecyclerViewAdapter.WatchedMoviesViewHolder> {

    private RealmResults<MoviesResult> watchedMoviesList;
    private OnRemoveClickListener onRemoveClickListener;
    private MoreInfoClickListener moreInfoClickListener;

    public WatchedMoviesRecyclerViewAdapter(RealmResults<MoviesResult> watchedMoviesList,
                                            OnRemoveClickListener onRemoveClickListener,
                                            MoreInfoClickListener moreInfoClickListener) {
        this.watchedMoviesList = watchedMoviesList;
        this.onRemoveClickListener = onRemoveClickListener;
        this.moreInfoClickListener = moreInfoClickListener;
    }

    @NonNull
    @Override
    public WatchedMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_list_item, viewGroup, false);
        return new WatchedMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchedMoviesViewHolder watchedMoviesViewHolder, int i) {
        watchedMoviesViewHolder.mWatched.setVisibility(View.GONE);
        watchedMoviesViewHolder.mMovieTitle.setText(watchedMoviesList.get(i).getTitle());
        watchedMoviesViewHolder.mReleaseDate.setText(watchedMoviesList.get(i).getReleaseDate());
        watchedMoviesViewHolder.mGenre.setText(Utils.getGenreList((RealmList<Integer>) watchedMoviesList.get(i).getGenreIds()));
        watchedMoviesViewHolder.mMoreInfo.setOnClickListener(v ->
                moreInfoClickListener.onMoreInfoClicked(watchedMoviesList.get(i).getOverview(),
                        watchedMoviesList.get(i).getPosterPath(),
                        watchedMoviesList.get(i).getVoteAverage(),
                        watchedMoviesList.get(i).getId(),
                        watchedMoviesList.get(i).getTitle(),
                        watchedMoviesList.get(i).getReleaseDate(),
                        (RealmList<Integer>) watchedMoviesList.get(i).getGenreIds()));

        if (watchedMoviesList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
            Glide.with(watchedMoviesViewHolder.mPosterPath.getContext())
                    .load(R.drawable.no_image_available)
                    .into((watchedMoviesViewHolder).mPosterPath);
        } else {
            Glide.with(watchedMoviesViewHolder.mPosterPath.getContext())
                    .load(BuildConfig.POSTER_PATH_URL_W185 + watchedMoviesList.get(i).getPosterPath())
                    .into(watchedMoviesViewHolder.mPosterPath);
        }
        Glide.with(watchedMoviesViewHolder.mBlackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(watchedMoviesViewHolder.mBlackTrackLeft);
        Glide.with(watchedMoviesViewHolder.mBlackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(watchedMoviesViewHolder.mBlackTrackRight);
    }

    @Override
    public int getItemCount() {
        return watchedMoviesList.size();
    }

    public void deleteItem(int position) {
        onRemoveClickListener.onMovieRemoved(watchedMoviesList.get(position).getId());
//        watchedMoviesList.re(position);
        notifyItemRemoved(position);
    }

    class WatchedMoviesViewHolder extends RecyclerView.ViewHolder {

        //region View
        @BindView(R.id.tv_movie_title)
        TextView mMovieTitle;
        @BindView(R.id.tv_release_date)
        TextView mReleaseDate;
        @BindView(R.id.iv_movie)
        ImageView mPosterPath;
        @BindView(R.id.tv_genre)
        TextView mGenre;
        @BindView(R.id.iv_black_track_left)
        ImageView mBlackTrackLeft;
        @BindView(R.id.iv_black_track_right)
        ImageView mBlackTrackRight;
        @BindView(R.id.bt_more_info)
        Button mMoreInfo;
        @BindView(R.id.cb_watched)
        CheckBox mWatched;
        //endregion

        private WatchedMoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRemoveClickListener {
        void onMovieRemoved(int id);
    }

    public interface MoreInfoClickListener {
        void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID,
                               String title, String releaseDate, RealmList<Integer> moviesResults);
    }
}
