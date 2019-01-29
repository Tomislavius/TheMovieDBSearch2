package com.example.tomislavrajic.themoviedbsearch2.models;

import java.util.List;

import com.example.tomislavrajic.themoviedbsearch2.BuildConfig;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesResult {

    //region Fields
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("video")
    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    private boolean isChecked;
    //endregion

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return "Year: " + releaseDate.substring(0, 4);
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public int getVoteAverage() {
        return (int) (voteAverage * 10);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}