package com.example.tomislavrajic.themoviedbsearch2.networking;

import com.example.tomislavrajic.themoviedbsearch2.models.ExternalID;
import com.example.tomislavrajic.themoviedbsearch2.models.TMDBResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBAPI {

    @GET("movie/top_rated")
    Call<TMDBResponseData> getTopRatedMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/popular")
    Call<TMDBResponseData> getPopularMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/now_playing")
    Call<TMDBResponseData> getNowPlayingMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<TMDBResponseData> getUpcomingMoviesResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/external_ids")
    Call<ExternalID> getExternalIDForMovie(@Path("movie_id") int movieID, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/external_ids")
    Call<ExternalID> getExternalIDForTVShow(@Path("tv_id") int tvID, @Query("api_key") String apiKey);

    @GET("person/{person_id}/external_ids")
    Call<ExternalID> getExternalIDForPerson(@Path("person_id") int personID, @Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<TMDBResponseData> getPopularTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/top_rated")
    Call<TMDBResponseData> getTopRatedTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/on_the_air")
    Call<TMDBResponseData> getOnTVShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/airing_today")
    Call<TMDBResponseData> getAiringTodayShowsResult(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("search/multi")
    Call<TMDBResponseData> getMovieTvShowPeople(@Query("api_key") String apiKey, @Query("query") String query, @Query("page") int page);
}