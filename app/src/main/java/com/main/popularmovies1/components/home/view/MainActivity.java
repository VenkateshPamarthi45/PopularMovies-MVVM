package com.main.popularmovies1.components.home.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.main.popularmovies1.R;
import com.main.popularmovies1.common.Constants;
import com.main.popularmovies1.components.detail.DetailActivity;
import com.main.popularmovies1.components.home.CustomMoviesListAdapter;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.components.home.repository.Repository;
import com.main.popularmovies1.components.home.view_model.MoviesViewModel;
import com.main.popularmovies1.databinding.ActivityMainBinding;
import com.main.popularmovies1.local_db.WishListDataBase;
import com.main.popularmovies1.network.ApiClient;
import com.main.popularmovies1.network.ApiInterface;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MoviesViewModel moviesViewModel;
    private LiveData<ArrayList<Movie>> moviesLiveData;
    private MutableLiveData<String> errorMessage;
    private CustomMoviesListAdapter moviesListAdapter;
    ActivityMainBinding activityMainBinding;
    ApiInterface apiService;

    private int currentMoviesTypeValue = Constants.POPULAR_MOVIES_VALUE;
    private WishListDataBase wishListDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        wishListDataBase = Room.databaseBuilder(this,
                WishListDataBase.class, Constants.DATABASE_NAME).build();
        activityMainBinding.moviesRv.setLayoutManager(gridLayoutManager);

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        updateUi();
        if (moviesLiveData.getValue() == null) {
            makeApiCall();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentMoviesTypeValue == Constants.FAVOURITE_MOVIES_VALUE){
            moviesViewModel.getFavouriteMovies(wishListDataBase, new Repository());
        }
    }

    public void makeApiCall(){
        activityMainBinding.progressBar.setVisibility(View.VISIBLE);
        activityMainBinding.moviesRv.setVisibility(View.GONE);
        activityMainBinding.errorMessageTv.setVisibility(View.GONE);
        if(currentMoviesTypeValue == Constants.FAVOURITE_MOVIES_VALUE){
            moviesViewModel.getFavouriteMovies(wishListDataBase, new Repository());
        }else {
            if(isNetworkAvailable()){
                moviesViewModel.getMoviesFromServer(apiService, new Repository(), currentMoviesTypeValue);
            }else{
                errorMessage.setValue(getString(R.string.please_check_internet_connection));
            }
        }

    }

    private void updateUi(){
        moviesLiveData = moviesViewModel.getMovieLiveData();
        moviesLiveData.observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movies) {
                activityMainBinding.progressBar.setVisibility(View.GONE);
                moviesListAdapter = new CustomMoviesListAdapter(movies, new CustomMoviesListAdapter.ListItemClickListener() {
                    @Override
                    public void onItemClicked(Movie movie) {
                        
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(getString(R.string.extra_movie_key),movie);
                        startActivity(intent);
                    }
                });
                activityMainBinding.moviesRv.setAdapter(moviesListAdapter);
                activityMainBinding.moviesRv.setVisibility(View.VISIBLE);
                activityMainBinding.errorMessageTv.setVisibility(View.GONE);
            }
        });


        errorMessage = moviesViewModel.getErrorMessage();
        errorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                activityMainBinding.progressBar.setVisibility(View.GONE);
                activityMainBinding.moviesRv.setVisibility(View.GONE);
                activityMainBinding.errorMessageTv.setText(s);
                activityMainBinding.errorMessageTv.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    private void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popular_movies:
                if(currentMoviesTypeValue != Constants.POPULAR_MOVIES_VALUE){
                    currentMoviesTypeValue = Constants.POPULAR_MOVIES_VALUE;
                    makeApiCall();
                }else{
                    showToastMessage(getString(R.string.showing_popular_movies));
                }
                return true;
            case R.id.top_rated_movies:
                if(currentMoviesTypeValue != Constants.TOP_RATED_MOVIES_VALUE){
                    currentMoviesTypeValue = Constants.TOP_RATED_MOVIES_VALUE;
                    makeApiCall();
                }else{
                    showToastMessage(getString(R.string.showing_top_rated_movies));
                }
                return true;
            case R.id.favourite_movies:
                if(currentMoviesTypeValue != Constants.FAVOURITE_MOVIES_VALUE){
                    currentMoviesTypeValue = Constants.FAVOURITE_MOVIES_VALUE;
                    makeApiCall();
                }else{
                    showToastMessage(getString(R.string.showing_favourite_movies));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
