package com.example.tomislavrajic.themoviedbsearch2.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMDBResponseData {

    //region Fields
    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    //endregion

    public List<Result> getResults() {
        return results;
    }
}