package com.main.popularmovies1.components.detail.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.main.popularmovies1.components.detail.view_model.DetailMovieViewModel;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.local_db.MovieEntity;
import com.main.popularmovies1.local_db.WishListDataBase;

public class DetailRepoistory {

    private static final String TAG = "DetailRepoistory";

    public void isMovieWishlisted(final int id, final WishListDataBase wishListDataBase, final DetailMovieViewModel detailMovieViewModel){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MovieEntity movie = wishListDataBase.databaseDOA().getMovieForWishListCheck(id);
                if(movie != null){
                    detailMovieViewModel.setIsMovieWishListed(true);
                }else{
                    detailMovieViewModel.setIsMovieWishListed(false);
                }
            }
        }).start();

    }

    public void removeMovieFromWishList(final int id, final WishListDataBase wishListDataBase, final DetailMovieViewModel detailMovieViewModel){
        new Thread(new Runnable() {
            @Override
            public void run() {
                wishListDataBase.databaseDOA().deleteMovie(id);
                detailMovieViewModel.setIsMovieWishListed(false);
            }
        }).start();

    }

    public void addMovieToWishList(final MovieEntity movieEntity, final WishListDataBase wishListDataBase, final DetailMovieViewModel detailMovieViewModel) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                wishListDataBase.databaseDOA().insertMovie(movieEntity);
                detailMovieViewModel.setIsMovieWishListed(true);
            }
        }).start();

    }
}
