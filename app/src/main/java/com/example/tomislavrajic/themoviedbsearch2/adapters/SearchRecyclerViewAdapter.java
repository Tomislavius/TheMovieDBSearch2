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
import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.models.Result;
import com.example.tomislavrajic.themoviedbsearch2.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {

    private List<Result> results;

    public SearchRecyclerViewAdapter(List<Result> results) {

        this.results = results;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_list_item, viewGroup, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {

        setFrame(searchViewHolder);

        if (results.get(i).getMediaType().equals("movie")) {
            searchViewHolder.title.setText(results.get(i).getTitle());
            searchViewHolder.releaseDate.setText(results.get(i).getReleaseDate());
            searchViewHolder.genre.setText(Utils.getGenreList(results.get(i).getGenreIds()));
            loadPosterImage(results.get(i).getPosterPath(), searchViewHolder);
        } else if (results.get(i).getMediaType().equals("tv")) {
            searchViewHolder.title.setText(results.get(i).getName());
            searchViewHolder.releaseDate.setText(results.get(i).getFirstAirDate());
            searchViewHolder.genre.setText(Utils.getGenreList(results.get(i).getGenreIds()));
            loadPosterImage(results.get(i).getPosterPath(), searchViewHolder);
        } else {
            searchViewHolder.genre.setVisibility(View.GONE);
            searchViewHolder.textGenre.setVisibility(View.GONE);
            searchViewHolder.title.setText(results.get(i).getName());
            loadPosterImage(results.get(i).getProfilePath(), searchViewHolder);
        }
    }

    private void loadPosterImage(String path, SearchViewHolder searchViewHolder) {
        Glide.with(searchViewHolder.itemView.getContext())
                .load(BuildConfig.POSTER_PATH_URL_W185 + path)
                .into(searchViewHolder.posterImage);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    private void setFrame(@NonNull SearchViewHolder searchViewHolder) {
        Glide.with(searchViewHolder.blackTrackLeft.getContext())
                .load(R.drawable.movie_track_left)
                .into(searchViewHolder.blackTrackLeft);
        Glide.with(searchViewHolder.blackTrackRight.getContext())
                .load(R.drawable.movie_track_right)
                .into(searchViewHolder.blackTrackRight);
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

        @BindView(R.id.iv_black_track_left)
        ImageView blackTrackLeft;

        @BindView(R.id.iv_black_track_right)
        ImageView blackTrackRight;

        @BindView(R.id.bt_more_info)
        Button moreInfoButton;

        @BindView(R.id.iv_watched)
        ImageView watchedIcon;
        //endregion

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}