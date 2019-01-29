package com.example.tomislavrajic.themoviedbsearch2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalID {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @SerializedName("facebook_id")
    @Expose
    private String facebookId;

    @SerializedName("instagram_id")
    @Expose
    private Object instagramId;

    @SerializedName("twitter_id")
    @Expose
    private String twitterId;

    public String getImdbId() {
        return imdbId;
    }
}
