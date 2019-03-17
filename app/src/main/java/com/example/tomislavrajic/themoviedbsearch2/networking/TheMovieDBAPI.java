package com.example.tomislavrajic.themoviedbsearch2.networking;

import com.example.tomislavrajic.themoviedbsearch2.models.ExternalID;
import com.example.tomislavrajic.themoviedbsearch2.models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBAPI {

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/popular")
    Call<Movies> getPopularMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/now_playing")
    Call<Movies> getNowPlayingMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<Movies> getUpcomingMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/external_ids")
    Call<ExternalID> getExternalIDForMovie(@Path("movie_id") int movieID, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/external_ids")
    Call<ExternalID> getExternalIDForTVShow(@Path("tv_id") int tvID, @Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<Movies> getPopularTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/top_rated")
    Call<Movies> getTopRatedTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/on_the_air")
    Call<Movies> getOnTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/airing_today")
    Call<Movies> getAiringTodayShowsResult(@Query("api_key") String apiKey, @Query("page") int page);
}