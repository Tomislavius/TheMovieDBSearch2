package com.example.tomislavrajic.themoviedbsearch2.networking;

import com.example.tomislavrajic.themoviedbsearch2.models.ExternalID;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBAPI {

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMoviesResult(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/popular")
    Call<Movies> getPopularMoviesResult(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/now_playing")
    Call<Movies> getNowPlayingMoviesResult(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/upcoming")
    Call<Movies> getUpcomingMoviesResult(@Query("api_key") String api_key, @Query("page") int page);

    @GET("movie/{movieID}")
    Call<ExternalID> getExternalID(@Path("movieID") int movieID, @Query("api_key") String api_key);
}