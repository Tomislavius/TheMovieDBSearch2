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
import com.example.tomislavrajic.themoviedbsearch2.utils.MoreInfoClickListener;
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class WatchedItemsRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Result, WatchedItemsRecyclerViewAdapter.WatchedMoviesViewHolder> {

    //region Fields
    private RealmResults<Result> watchedItemsList;
    private OnRemoveClickListener onRemoveClickListener;
    private MoreInfoClickListener moreInfoClickListener;
    //endregion

    public WatchedItemsRecyclerViewAdapter(RealmResults<Result> watchedItemsList,
                                           OnRemoveClickListener onRemoveClickListener,
                                           MoreInfoClickListener moreInfoClickListener) {
        super(watchedItemsList, false);
        this.watchedItemsList = watchedItemsList;
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
        watchedMoviesViewHolder.watchedIcon.setVisibility(View.GONE);
        watchedMoviesViewHolder.genre.setText(Utils.getGenreList(watchedItemsList.get(i).getGenreIds()));

        setFrame(watchedMoviesViewHolder);
        setPosterImage(watchedMoviesViewHolder, i);

        watchedMoviesViewHolder.moreInfoButton.setOnClickListener(v ->
                moreInfoClickListener.onMoreInfoClicked(watchedItemsList.get(i), Result.MOVIE));

    }

    @Override
    public int getItemCount() {
        return watchedItemsList.size();
    }

    private void setPosterImage(@NonNull WatchedMoviesViewHolder watchedMoviesViewHolder, int i) {
        if (watchedItemsList.get(i).getPosterPath() == null ||
                watchedItemsList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
            Glide.with(watchedMoviesViewHolder.posterImage.getContext())
                    .load(R.drawable.no_image_available)
                    .into((watchedMoviesViewHolder).posterImage);
        } else {
            Glide.with(watchedMoviesViewHolder.posterImage.getContext())
                    .load(BuildConfig.POSTER_PATH_URL_W185 + watchedItemsList.get(i).getPosterPath())
                    .into(watchedMoviesViewHolder.posterImage);
        }
    }

    private void setFrame(@NonNull WatchedMoviesViewHolder watchedMoviesViewHolder) {
        Glide.with(watchedMoviesViewHolder.blackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(watchedMoviesViewHolder.blackTrackLeft);
        Glide.with(watchedMoviesViewHolder.blackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(watchedMoviesViewHolder.blackTrackRight);
    }

    public void deleteItem(int position) {
        onRemoveClickListener.onMovieRemoved(watchedItemsList.get(position).getId());
        notifyItemRemoved(position);
    }

    class WatchedMoviesViewHolder extends RecyclerView.ViewHolder {

        //region Fields
        @BindView(R.id.tv_movie_title)
        TextView movieTitle;

        @BindView(R.id.tv_release_date)
        TextView releaseDate;

        @BindView(R.id.tv_genre)
        TextView genre;

        @BindView(R.id.iv_poster_image)
        ImageView posterImage;

        @BindView(R.id.iv_black_track_left)
        ImageView blackTrackLeft;

        @BindView(R.id.iv_black_track_right)
        ImageView blackTrackRight;

        @BindView(R.id.iv_watched)
        ImageView watchedIcon;

        @BindView(R.id.bt_more_info)
        Button moreInfoButton;
        //endregion

        private WatchedMoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRemoveClickListener {
        void onMovieRemoved(int id);
    }
}