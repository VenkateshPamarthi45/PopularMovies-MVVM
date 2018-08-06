package com.main.popularmovies1.components.detail.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.main.popularmovies1.components.detail.model.MovieReview;
import com.main.popularmovies1.components.detail.repository.ReviewRepository;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

public class MovieReviewsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MovieReview>> movieLiveData;
    private MutableLiveData<String> errorMessage;

    public void setMovieLiveData(ArrayList<MovieReview> movieLiveData) {
        this.movieLiveData.setValue(movieLiveData);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setValue(errorMessage);
    }

    public MutableLiveData<ArrayList<MovieReview>> getMovieLiveData() {
        if(movieLiveData == null){
            movieLiveData = new MutableLiveData<>();
        }
        return movieLiveData;
    }

    public MutableLiveData<String> getErrorMessage() {
        if(errorMessage == null){
            errorMessage = new MutableLiveData<>();
        }
        return errorMessage;
    }

    public void getMovieReviewsFromServer(ApiInterface apiService, int movieId, ReviewRepository reviewRepository){
        reviewRepository.getMovieReviews(apiService, movieId, this);
    }
}
