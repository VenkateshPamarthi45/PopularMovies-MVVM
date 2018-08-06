package com.main.popularmovies1.components.detail.repository;

import android.util.Log;

import com.main.popularmovies1.common.Constants;
import com.main.popularmovies1.components.detail.model.MovieTrailer;
import com.main.popularmovies1.components.detail.model.MovieTrailerResponse;
import com.main.popularmovies1.components.detail.view_model.MovieTrailersViewModel;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerRepository {

    private static final String TAG = "TrailerRepository";

    public void getMoviesTrailers(ApiInterface apiService, int movieId, final MovieTrailersViewModel movieTrailersViewModel){
        Call<MovieTrailerResponse> call = apiService.getMovieTrailerDetails(movieId, Constants.API_KEY);
        call.enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200){
                    ArrayList<MovieTrailer> movieTrailers = response.body().getResults();
                    movieTrailersViewModel.setMovieLiveData(movieTrailers);
                }else{
                    movieTrailersViewModel.setErrorMessage(response.message());
                }

            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                movieTrailersViewModel.setErrorMessage(t.getMessage());
            }
        });
    }
}
