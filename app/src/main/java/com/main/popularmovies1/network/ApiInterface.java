package com.main.popularmovies1.network;

import com.main.popularmovies1.components.detail.model.MovieReviewResponse;
import com.main.popularmovies1.components.detail.model.MovieTrailerResponse;
import com.main.popularmovies1.components.home.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewResponse> getMovieReviewDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailerDetails(@Path("id") int id, @Query("api_key") String apiKey);

}