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

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_ITEM = 0;
    private List<MoviesResult> moviesResultList = new ArrayList<>(0);
    private RealmResults<MoviesResult> watchedMovies;
    private OnBindClickListener onBindClickListener;

    public MoviesRecyclerViewAdapter(RealmResults<MoviesResult> watchedMovies, OnBindClickListener onBindClickListener) {
        this.watchedMovies = watchedMovies;
        this.onBindClickListener = onBindClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_list_item, viewGroup, false);
            return new MoviesViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_list_item, viewGroup, false);
            return new LoadMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder moviesViewHolder, int i) {

        if (moviesViewHolder instanceof MoviesViewHolder) {

            ((MoviesViewHolder) moviesViewHolder).mGenre.setText(Utils.getGenreList((RealmList<Integer>) moviesResultList.get(i).getGenreIds()));
            ((MoviesViewHolder) moviesViewHolder).mMovieTitle.setText(moviesResultList.get(i).getTitle());
            ((MoviesViewHolder) moviesViewHolder).mReleaseDate.setText(moviesResultList.get(i).getReleaseDate());

            setMoreInfo((MoviesViewHolder) moviesViewHolder, i);

            setWatched(moviesViewHolder, i);

            getPosterImage((MoviesViewHolder) moviesViewHolder, i);

            getFrame((MoviesViewHolder) moviesViewHolder);

        } else {
            setLoadMore((LoadMoreViewHolder) moviesViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != moviesResultList.size()) {
            return TYPE_ITEM;
        } else {
            return TYPE_BUTTON;
        }
    }

    private void setLoadMore(@NonNull LoadMoreViewHolder moviesViewHolder) {
        if (moviesResultList.size() > 0) {
            moviesViewHolder.mButtonLoadMore.setVisibility(View.VISIBLE);
        } else {
            moviesViewHolder.mButtonLoadMore.setVisibility(View.GONE);
        }
        moviesViewHolder
                .mButtonLoadMore.setOnClickListener(v -> onBindClickListener.onLoadMoreClicked());
    }

    private void setWatched(@NonNull RecyclerView.ViewHolder moviesViewHolder, int i) {
        if (watchedMovies.size() == 0) {
            ((MoviesViewHolder) moviesViewHolder).mWatched.setChecked(false);
        } else {
            for (int j = 0; j < watchedMovies.size(); j++) {
                if (watchedMovies.get(j).getId() == moviesResultList.get(i).getId()) {
                    moviesResultList.get(i).setChecked(true);
                    break;
                } else {
                    moviesResultList.get(i).setChecked(false);
                }
            }
            ((MoviesViewHolder) moviesViewHolder).mWatched.setChecked(moviesResultList.get(i).isChecked());
        }

        ((MoviesViewHolder) moviesViewHolder).mWatched.setOnCheckedChangeListener((buttonView, isChecked) ->
                onBindClickListener.onCheckedChanged(isChecked, moviesResultList.get(moviesViewHolder.getAdapterPosition())));
    }

    private void setMoreInfo(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        moviesViewHolder.mMoreInfo.setOnClickListener(v -> onBindClickListener
                .onMoreInfoClicked(moviesResultList.get(i).getOverview(),
                        moviesResultList.get(i).getPosterPath(),
                        moviesResultList.get(i).getVoteAverage(),
                        moviesResultList.get(i).getId(),
                        moviesResultList.get(i).getTitle(),
                        moviesResultList.get(i).getReleaseDate(),
                        moviesResultList.get(i).getGenreIds()));
    }

    private void getPosterImage(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        if (moviesResultList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
            Glide.with(moviesViewHolder.mPosterPath.getContext())
                    .load(R.drawable.no_image_available)
                    .into(moviesViewHolder.mPosterPath);
        } else {
            Glide.with(moviesViewHolder.mPosterPath.getContext())
                    .load(BuildConfig.POSTER_PATH_URL_W185 + moviesResultList.get(i).getPosterPath())
                    .into(moviesViewHolder.mPosterPath);
        }
    }

    private void getFrame(@NonNull MoviesViewHolder moviesViewHolder) {
        Glide.with(moviesViewHolder.mBlackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(moviesViewHolder.mBlackTrackLeft);
        Glide.with(moviesViewHolder.mBlackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(moviesViewHolder.mBlackTrackRight);
    }

    public void refreshWatchedMoviesList(RealmResults<MoviesResult> watchedMovies) {
        this.watchedMovies = watchedMovies;
        notifyDataSetChanged();
    }

    public void setData(List<MoviesResult> results) {
        moviesResultList.addAll(results);
        notifyItemRangeInserted(getItemCount(), results.size());
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

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

        private MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bt_load_more)
        Button mButtonLoadMore;

        LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnBindClickListener {

        void onLoadMoreClicked();

        void onMoreInfoClicked(String overview, String posterPath, int voteAverage, int movieID,
                               String title, String releaseDate, List<Integer> genreIds);

        void onCheckedChanged(boolean isChecked, MoviesResult moviesResult);

    }
}

