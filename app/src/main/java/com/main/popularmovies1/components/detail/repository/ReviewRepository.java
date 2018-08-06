package com.main.popularmovies1.components.detail.repository;

import android.util.Log;

import com.main.popularmovies1.common.Constants;
import com.main.popularmovies1.components.detail.model.MovieReview;
import com.main.popularmovies1.components.detail.model.MovieReviewResponse;
import com.main.popularmovies1.components.detail.view_model.MovieReviewsViewModel;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    private static final String TAG = "TrailerRepository";

    public void getMovieReviews(ApiInterface apiService, int movieId, final MovieReviewsViewModel movieReviewsViewModel){
        Call<MovieReviewResponse> call = apiService.getMovieReviewDetails(movieId, Constants.API_KEY);
        call.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200){
                    ArrayList<MovieReview> movieReviews = response.body().getResults();
                    movieReviewsViewModel.setMovieLiveData(movieReviews);
                }else{
                    movieReviewsViewModel.setErrorMessage(response.message());
                }

            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                movieReviewsViewModel.setErrorMessage(t.getMessage());
            }
        });
    }
}
