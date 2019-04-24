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

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //region Field
    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_ITEM = 0;

    private ArrayList<Result> results;
    private List<Result> resultList = new ArrayList<>(0);
    private MoreInfoClickListener moreInfoClickListener;
    private OnBindClickListener onBindClickListener;
    //endregion

    public SearchRecyclerViewAdapter(ArrayList<Result> results,
                                     MoreInfoClickListener moreInfoClickListener,
                                     OnBindClickListener onBindClickListener) {
        this.results = results;
        this.moreInfoClickListener = moreInfoClickListener;
        this.onBindClickListener = onBindClickListener;
        setData(results);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.movies_list_item, viewGroup, false);
            return new SearchViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.button_list_item, viewGroup, false);
            return new LoadMoreViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof SearchViewHolder) {
            if (resultList.get(i).getMediaType().equals("movie")) {
                setFrame((SearchViewHolder) viewHolder);
                ((SearchViewHolder) viewHolder).title.setText(resultList.get(i).getTitle());
                ((SearchViewHolder) viewHolder).releaseDate.setText(resultList.get(i).getReleaseDate());
                ((SearchViewHolder) viewHolder).genre.setText(Utils.getGenreList(resultList.get(i).getGenreIds()));
                ((SearchViewHolder) viewHolder).mediaType.setText(resultList.get(i).getMediaType());
                ((SearchViewHolder) viewHolder).moreInfoButton.setOnClickListener(v ->
                        moreInfoClickListener.onMoreInfoClicked(resultList.get(i), "movie"));

                if (resultList.get(i).getPosterPath() == null ||
                        resultList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
                    Glide.with(viewHolder.itemView.getContext())
                            .load(R.drawable.no_image_available)
                            .into(((SearchViewHolder) viewHolder).posterImage);
                } else {
                    loadPosterImage(resultList.get(i).getPosterPath(), ((SearchViewHolder) viewHolder));
                }

            } else if (resultList.get(i).getMediaType().equals("tv")) {
                setFrame((SearchViewHolder) viewHolder);
                ((SearchViewHolder) viewHolder).title.setText(resultList.get(i).getName());
                ((SearchViewHolder) viewHolder).releaseDate.setText(resultList.get(i).getFirstAirDate());
                ((SearchViewHolder) viewHolder).genre.setText(Utils.getGenreList(resultList.get(i).getGenreIds()));
                ((SearchViewHolder) viewHolder).mediaType.setText(resultList.get(i).getMediaType());
                ((SearchViewHolder) viewHolder).moreInfoButton.setOnClickListener(v ->
                        moreInfoClickListener.onMoreInfoClicked(resultList.get(i), "tv"));

                if (resultList.get(i).getPosterPath() == null ||
                        resultList.get(i).getPosterPath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
                    Glide.with(viewHolder.itemView.getContext())
                            .load(R.drawable.no_image_available)
                            .into(((SearchViewHolder) viewHolder).posterImage);
                } else {
                    loadPosterImage(resultList.get(i).getPosterPath(), ((SearchViewHolder) viewHolder));
                }

            } else if (resultList.get(i).getMediaType().equals("person")) {
                setFrame((SearchViewHolder) viewHolder);
                ((SearchViewHolder) viewHolder).genre.setVisibility(View.GONE);
                ((SearchViewHolder) viewHolder).textGenre.setVisibility(View.GONE);

                ((SearchViewHolder) viewHolder).title.setText(resultList.get(i).getName());
                ((SearchViewHolder) viewHolder).mediaType.setText(resultList.get(i).getMediaType());
                ((SearchViewHolder) viewHolder).moreInfoButton.setOnClickListener(v ->
                        moreInfoClickListener.onMoreInfoClicked(resultList.get(i), "person"));

                if (resultList.get(i).getProfilePath() == null ||
                        resultList.get(i).getProfilePath().equals(BuildConfig.POSTER_PATH_URL_W185 + "null")) {
                    Glide.with(viewHolder.itemView.getContext())
                            .load(R.drawable.no_image_available)
                            .into(((SearchViewHolder) viewHolder).posterImage);
                } else {
                    loadPosterImage(resultList.get(i).getProfilePath(), ((SearchViewHolder) viewHolder));
                }
            }
        } else setLoadMore((LoadMoreViewHolder) viewHolder);
    }

    private void loadPosterImage(String path, SearchViewHolder searchViewHolder) {
        Glide.with(searchViewHolder.itemView.getContext())
                .load(BuildConfig.POSTER_PATH_URL_W185 + path)
                .into(searchViewHolder.posterImage);
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

    private void setLoadMore(LoadMoreViewHolder loadMore) {
        if (resultList.size() > 0) {
            loadMore.mButtonLoadMore.setVisibility(View.VISIBLE);
        } else {
            loadMore.mButtonLoadMore.setVisibility(View.GONE);
        }
        loadMore.mButtonLoadMore.setOnClickListener(v -> onBindClickListener.onLoadMoreClicked());
    }

    private void setFrame(@NonNull SearchViewHolder searchViewHolder) {
        Glide.with(searchViewHolder.blackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(searchViewHolder.blackTrackLeft);
        Glide.with(searchViewHolder.blackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(searchViewHolder.blackTrackRight);
    }

    public void setData(List<Result> results) {
        resultList.addAll(results);
        notifyItemRangeInserted(getItemCount(), results.size());
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        //region View
        @BindView(R.id.tv_movie_title)
        TextView title;

        @BindView(R.id.tv_release_date)
        TextView releaseDate;

        @BindView(R.id.iv_poster_image)
        ImageView posterImage;

        @BindView(R.id.tv_genre)
        TextView genre;

        @BindView(R.id.genre)
        TextView textGenre;

        @BindView(R.id.tv_media_type)
        TextView mediaType;

        @BindView(R.id.iv_black_track_left)
        ImageView blackTrackLeft;

        @BindView(R.id.iv_black_track_right)
        ImageView blackTrackRight;

        @BindView(R.id.bt_more_info)
        Button moreInfoButton;

        @BindView(R.id.iv_watched)
        ImageView watchedIcon;
        //endregion

        private SearchViewHolder(@NonNull View itemView) {
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
}