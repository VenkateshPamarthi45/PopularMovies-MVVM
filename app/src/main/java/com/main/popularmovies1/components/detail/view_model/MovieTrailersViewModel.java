package com.main.popularmovies1.components.detail.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.main.popularmovies1.components.detail.model.MovieTrailer;
import com.main.popularmovies1.components.detail.repository.TrailerRepository;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

public class MovieTrailersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MovieTrailer>> movieLiveData;
    private MutableLiveData<String> errorMessage;

    public MutableLiveData<ArrayList<MovieTrailer>> getMovieLiveData() {
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

    public void setMovieLiveData(ArrayList<MovieTrailer> movieTrailerArrayList) {
        this.movieLiveData.setValue(movieTrailerArrayList);
    }

    public void setErrorMessage(String errorMessageString) {
        this.errorMessage.setValue(errorMessageString);
    }

    public void getMoviesFromServer(ApiInterface apiService, int movieId, TrailerRepository trailerRepository){
        trailerRepository.getMoviesTrailers(apiService, movieId, this);
    }
}
