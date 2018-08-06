package com.main.popularmovies1.components.home.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.main.popularmovies1.common.Constants;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.components.home.model.MoviesResponse;
import com.main.popularmovies1.components.home.view_model.MoviesViewModel;
import com.main.popularmovies1.local_db.MovieEntity;
import com.main.popularmovies1.local_db.WishListDataBase;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static final String TAG = "TrailerRepository";

    public void getMovies(ApiInterface apiService, final MoviesViewModel onResponseListener, int moviesType){
        Call<MoviesResponse> call = null;

        if(moviesType == Constants.POPULAR_MOVIES_VALUE){
            call = apiService.getPopularMovies(Constants.API_KEY);
        }else if(moviesType == Constants.TOP_RATED_MOVIES_VALUE){
            call = apiService.getTopRatedMovies(Constants.API_KEY);
        }
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200){
                    ArrayList<Movie> movies = response.body().getResults();
                    onResponseListener.setMovieLiveData(movies);
                }else{
                    onResponseListener.setErrorMessage(response.message());
                }
            }
            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void getFavouriteMovies(final WishListDataBase wishListDataBase, final MoviesViewModel moviesViewModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MovieEntity> moviesEntitiesList =  wishListDataBase.databaseDOA().getAllMovies();
                if(moviesEntitiesList != null && moviesEntitiesList.size() > 0){
                    ArrayList<Movie> movieArrayList = new ArrayList<>();
                    for (MovieEntity movieEntity: moviesEntitiesList) {
                        Movie movie = generateMovieObjectFromMovieEntity(movieEntity);
                        movieArrayList.add(movie);
                    }
                    moviesViewModel.setMovieLiveData(movieArrayList);
                }else{
                    moviesViewModel.setErrorMessage(Constants.NO_FAVOURITE_MOVIES_STRING);
                }

            }
        }).start();
    }

    private Movie generateMovieObjectFromMovieEntity(MovieEntity movieEntity){
        Movie movie = new Movie();
        movie.setId(movieEntity.getId());
        movie.setVoteAverage(movieEntity.getVote_average());
        movie.setTitle(movieEntity.getTitle());
        movie.setPopularity(movieEntity.getPopularity());
        movie.setPosterPath(movieEntity.getPoster_path());
        movie.setOriginalLanguage(movieEntity.getOriginal_language());
        movie.setOriginalTitle(movieEntity.getOriginal_title());
        movie.setBackdropPath(movieEntity.getBackdrop_path());
        movie.setOverview(movieEntity.getOverview());
        movie.setReleaseDate(movieEntity.getRelease_date());
        movie.setVoteCount(movieEntity.getVote_count());
        return movie;
    }
}
