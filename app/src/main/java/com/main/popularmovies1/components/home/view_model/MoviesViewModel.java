package com.main.popularmovies1.components.home.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;

import com.main.popularmovies1.components.home.repository.Repository;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.local_db.WishListDataBase;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

public class MoviesViewModel extends ViewModel{
    private MutableLiveData<ArrayList<Movie>> movieLiveData;
    private MutableLiveData<String> errorMessage;

    public MutableLiveData<ArrayList<Movie>> getMovieLiveData() {
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

    public void setMovieLiveData(final ArrayList<Movie> movieArrayList) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                movieLiveData.setValue(movieArrayList);
            }
        });

    }

    public void setErrorMessage(final String errorString) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                errorMessage.setValue(errorString);
            }
        });
    }

    public void getMoviesFromServer(ApiInterface apiService, Repository repository, int moviesType){
        repository.getMovies(apiService,this, moviesType);
    }

    public void getFavouriteMovies(WishListDataBase wishListDataBase, Repository repository){
        repository.getFavouriteMovies(wishListDataBase, this);
    }

}
