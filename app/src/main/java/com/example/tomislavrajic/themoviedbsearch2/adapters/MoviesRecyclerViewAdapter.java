package com.example.tomislavrajic.themoviedbsearch2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.example.tomislavrajic.themoviedbsearch2.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.OnBindClickListener;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region Fields
    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_ITEM = 0;

    private boolean isMovie;

    private List<Result> resultList = new ArrayList<>(0);
    private RealmResults<Result> watchedMovies;
    private OnBindClickListener onBindClickListener;
    private MoreInfoClickListener moreInfoClickListener;
    //endregion

    public MoviesRecyclerViewAdapter(RealmResults<Result> watchedMovies,
                                     OnBindClickListener onBindClickListener,
                                     MoreInfoClickListener moreInfoClickListener) {
        this.watchedMovies = watchedMovies;
        this.onBindClickListener = onBindClickListener;
        this.moreInfoClickListener = moreInfoClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.movies_list_item, viewGroup, false);
            return new MoviesViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.button_list_item, viewGroup, false);
            return new LoadMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder moviesViewHolder, int i) {

        //TODO check does need isMovie
        if (isMovie) {
            if (moviesViewHolder instanceof MoviesViewHolder) {

                ((MoviesViewHolder) moviesViewHolder).title.setText(resultList.get(i).getTitle());
                ((MoviesViewHolder) moviesViewHolder).releaseDate.setText(resultList.get(i).getReleaseDate());
                ((MoviesViewHolder) moviesViewHolder).watchedIcon.setOnClickListener(null);

                setGenre((MoviesViewHolder) moviesViewHolder, i);

                setPosterImage((MoviesViewHolder) moviesViewHolder, i);

                setMoreInfoClickListener((MoviesViewHolder) moviesViewHolder, i, "movie");

                setButtonWatched(moviesViewHolder, i, true);

                setFrame((MoviesViewHolder) moviesViewHolder);

            } else {
                setLoadMore((LoadMoreViewHolder) moviesViewHolder);
            }

        } else {
            if (moviesViewHolder instanceof MoviesViewHolder) {

                ((MoviesViewHolder) moviesViewHolder).title.setText(resultList.get(i).getName());
                ((MoviesViewHolder) moviesViewHolder).releaseDate.setText(resultList.get(i).getFirstAirDate());
                ((MoviesViewHolder) moviesViewHolder).watchedIcon.setOnClickListener(null);

                setGenre((MoviesViewHolder) moviesViewHolder, i);

                setPosterImage((MoviesViewHolder) moviesViewHolder, i);

                setMoreInfoClickListener((MoviesViewHolder) moviesViewHolder, i, "tv");

                setButtonWatched(moviesViewHolder, i, false);

                setFrame((MoviesViewHolder) moviesViewHolder);

            } else {
                setLoadMore((LoadMoreViewHolder) moviesViewHolder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != resultList.size()) {
            return TYPE_ITEM;
        } else {
            return TYPE_BUTTON;
        }
    }

    private void setGenre(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        moviesViewHolder.genre.setText(Utils.getGenreList(resultList.get(i).getGenreIds()));
    }

    private void setPosterImage(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        if (resultList.get(i).getPosterPath() == null ||
                resultList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
            Glide.with(moviesViewHolder.posterImage.getContext())
                    .load(R.drawable.no_image_available)
                    .into(moviesViewHolder.posterImage);
        } else {
            Glide.with(moviesViewHolder.posterImage.getContext())
                    .load(BuildConfig.POSTER_PATH_URL_W185 + resultList.get(i).getPosterPath())
                    .into(moviesViewHolder.posterImage);
        }
    }

    private void setMoreInfoClickListener(@NonNull MoviesViewHolder moviesViewHolder, int i, String isMovie) {
        moviesViewHolder.moreInfoButton.setOnClickListener(v -> moreInfoClickListener.onMoreInfoClicked(resultList.get(i), isMovie));
    }

    private void setButtonWatched(@NonNull RecyclerView.ViewHolder moviesViewHolder, int i, boolean isMovie) {
        if (watchedMovies.size() == 0) {
            resultList.get(i).setChecked(false);
            loadWatchedImage(moviesViewHolder, R.drawable.watched_disabled);
        } else {
            for (int j = 0; j < watchedMovies.size(); j++) {
                assert watchedMovies.get(j) != null;
                if (watchedMovies.get(j).getId() == resultList.get(i).getId()) {
                    resultList.get(i).setChecked(true);
                    break;
                } else {
                    resultList.get(i).setChecked(false);
                }
            }
            if (resultList.get(i).isChecked()) {
                loadWatchedImage(moviesViewHolder, R.drawable.watched_enabled);
            } else {
                loadWatchedImage(moviesViewHolder, R.drawable.watched_disabled);
            }
        }

        if (isMovie) {
            ((MoviesViewHolder) moviesViewHolder).watchedIcon.setOnClickListener(v -> {
                if (resultList.get(i).isChecked()) {
                    resultList.get(i).setChecked(false);
                } else {
                    resultList.get(i).setChecked(true);
                }
                onBindClickListener.onCheckedChanged(resultList.get(i).isChecked()
                        , resultList.get(moviesViewHolder.getAdapterPosition()));
                if (resultList.get(i).isChecked()) {
                    loadWatchedImage(moviesViewHolder, R.drawable.watched_enabled);
                } else {
                    loadWatchedImage(moviesViewHolder, R.drawable.watched_disabled);
                }
            });
        } else {
            ((MoviesViewHolder) moviesViewHolder).watchedIcon.setOnClickListener(v -> {
                if (resultList.get(i).isChecked()) {
                    resultList.get(i).setChecked(false);
                } else {
                    resultList.get(i).setChecked(true);
                }
                onBindClickListener.onCheckedChanged(resultList.get(i).isChecked()
                        , resultList.get(moviesViewHolder.getAdapterPosition()));
                if (resultList.get(i).isChecked()) {
                    loadWatchedImage(moviesViewHolder, R.drawable.watched_enabled);
                } else {
                    loadWatchedImage(moviesViewHolder, R.drawable.watched_disabled);
                }
            });
        }
    }

    private void setFrame(@NonNull MoviesViewHolder moviesViewHolder) {
        Glide.with(moviesViewHolder.blackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(moviesViewHolder.blackTrackLeft);
        Glide.with(moviesViewHolder.blackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(moviesViewHolder.blackTrackRight);
    }

    private void loadWatchedImage(@NonNull RecyclerView.ViewHolder moviesViewHolder, int p) {
        Glide.with(moviesViewHolder.itemView.getContext())
                .load(p)
                .into(((MoviesViewHolder) moviesViewHolder).watchedIcon);
    }

    private void setLoadMore(@NonNull LoadMoreViewHolder loadMore) {
        if (resultList.size() > 0) {
            loadMore.loadMoreButton.setVisibility(View.VISIBLE);
        } else {
            loadMore.loadMoreButton.setVisibility(View.GONE);
        }
        loadMore.loadMoreButton.setOnClickListener(v -> onBindClickListener.onLoadMoreClicked());
    }

    public void refreshWatchedMoviesList(RealmResults<Result> watchedMovies) {
        this.watchedMovies = watchedMovies;
        notifyDataSetChanged();
    }

    public void setData(List<Result> results, boolean isMovie) {
        this.isMovie = isMovie;
        resultList.addAll(results);
        notifyItemRangeInserted(getItemCount(), results.size());
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        //region Fields
        @BindView(R.id.tv_movie_title)
        TextView title;

        @BindView(R.id.tv_release_date)
        TextView releaseDate;

        @BindView(R.id.iv_poster_image)
        ImageView posterImage;

        @BindView(R.id.tv_genre)
        TextView genre;

        @BindView(R.id.iv_black_track_left)
        ImageView blackTrackLeft;

        @BindView(R.id.iv_black_track_right)
        ImageView blackTrackRight;

        @BindView(R.id.bt_more_info)
        Button moreInfoButton;

        @BindView(R.id.iv_watched)
        ImageView watchedIcon;
        //endregion

        private MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        //region Fields
        @BindView(R.id.bt_load_more)
        Button loadMoreButton;
        //endregion

        LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}