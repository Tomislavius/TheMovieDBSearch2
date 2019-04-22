package com.example.tomislavrajic.themoviedbsearch2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Result extends RealmObject implements Serializable {

    //region Fields
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @PrimaryKey
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

    @SerializedName("media_type")
    @Expose
    private String mediaType;

    @SerializedName("name")
    @Expose
    private String name;

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
    private RealmList<Integer> genreIds;

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

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    @Expose(serialize = false, deserialize = false)
    private Boolean isMovie;

    @Expose(serialize = false, deserialize = false)
    private Boolean isChecked;
    //endregion

    //region Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        if (releaseDate.length() > 0) {
            return "Year: " + releaseDate.substring(0, 4);
        } else return "Unknown";
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

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean movie) {
        isMovie = movie;
    }

    public String getName() {
        return name;
    }

    public String getFirstAirDate() {
        if (firstAirDate.length() > 0) {
            return "Year: " + firstAirDate.substring(0, 4);
        } else return "Unknown";
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Double getPopularity() {
        return popularity;
    }
    //endregion
}