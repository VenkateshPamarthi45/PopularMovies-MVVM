package com.main.popularmovies1.components.detail.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.main.popularmovies1.components.detail.repository.DetailRepoistory;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.local_db.MovieEntity;
import com.main.popularmovies1.local_db.WishListDataBase;


public class DetailMovieViewModel extends ViewModel {

    private static final String TAG = "DetailMovieViewModel";

    private MutableLiveData<Boolean> isMovieWishListed;

    public MutableLiveData<Boolean> getIsMovieWishListed() {
        if(isMovieWishListed == null){
            isMovieWishListed = new MutableLiveData<>();
        }
        return isMovieWishListed;
    }

    public void setIsMovieWishListed(final Boolean isMovieWishListedValue) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isMovieWishListed == null){
                    isMovieWishListed = new MutableLiveData<>();
                }
                isMovieWishListed.setValue(isMovieWishListedValue);
            }
        });

    }

    public void getDetailsIfMovieIsWishlistedInDb(int id, DetailRepoistory detailRepoistory, WishListDataBase wishListDataBase){
        detailRepoistory.isMovieWishlisted(id, wishListDataBase,this);
    }

    public void removeMovieFromWishList(int id, DetailRepoistory detailRepoistory, WishListDataBase wishListDataBase) {
        detailRepoistory.removeMovieFromWishList(id, wishListDataBase, this);
    }
    public void addMovieToWishList(MovieEntity movieEntity, DetailRepoistory detailRepoistory, WishListDataBase wishListDataBase) {
        Log.d(TAG, "addMovieToWishList() called with: movieEntity = [" + movieEntity + "], detailRepoistory = [" + detailRepoistory + "], wishListDataBase = [" + wishListDataBase + "]");
        detailRepoistory.addMovieToWishList(movieEntity, wishListDataBase, this);
    }
}
