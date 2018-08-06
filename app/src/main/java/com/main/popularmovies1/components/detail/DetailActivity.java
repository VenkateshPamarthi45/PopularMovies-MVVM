package com.main.popularmovies1.components.detail;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.main.popularmovies1.R;
import com.main.popularmovies1.common.Constants;
import com.main.popularmovies1.components.detail.model.MovieReview;
import com.main.popularmovies1.components.detail.model.MovieTrailer;
import com.main.popularmovies1.components.detail.repository.DetailRepoistory;
import com.main.popularmovies1.components.detail.repository.ReviewRepository;
import com.main.popularmovies1.components.detail.repository.TrailerRepository;
import com.main.popularmovies1.components.detail.view_model.DetailMovieViewModel;
import com.main.popularmovies1.components.detail.view_model.MovieReviewsViewModel;
import com.main.popularmovies1.components.detail.view_model.MovieTrailersViewModel;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.databinding.ActivityDetailBinding;
import com.main.popularmovies1.local_db.MovieEntity;
import com.main.popularmovies1.local_db.WishListDataBase;
import com.main.popularmovies1.network.ApiClient;
import com.main.popularmovies1.network.ApiInterface;
import com.main.popularmovies1.utilities.ImageUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Movie movie;
    private static final String TAG = "DetailActivity";
    private ActivityDetailBinding activityDetailBinding;
    private MovieTrailersViewModel movieTrailersViewModel;
    private MovieReviewsViewModel movieReviewsViewModel;
    private DetailMovieViewModel detailMovieViewModel;
    private MutableLiveData<ArrayList<MovieTrailer>> mutableMovieTrailersLiveData;
    private MutableLiveData<ArrayList<MovieReview>> mutableMovieReviewsLiveData;
    private Boolean isMovieWishlisted = false;
    private WishListDataBase wishListDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(getIntent().getExtras() != null){
            movie = getIntent().getExtras().getParcelable(getString(R.string.extra_movie_key));
        }
        wishListDataBase = Room.databaseBuilder(this,
                WishListDataBase.class, Constants.DATABASE_NAME).build();
        updateUi();
        movieTrailersViewModel = ViewModelProviders.of(this).get(MovieTrailersViewModel.class);
        movieReviewsViewModel = ViewModelProviders.of(this).get(MovieReviewsViewModel.class);
        detailMovieViewModel = ViewModelProviders.of(this).get(DetailMovieViewModel.class);
        activityDetailBinding.wishListImageView.setOnClickListener(this);
        observeUiData();
        makeApiCall();
        setTitle(movie.getTitle());
    }

    private void observeUiData() {
        mutableMovieTrailersLiveData = movieTrailersViewModel.getMovieLiveData();
        mutableMovieReviewsLiveData = movieReviewsViewModel.getMovieLiveData();
        mutableMovieTrailersLiveData.observe(this, new Observer<ArrayList<MovieTrailer>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieTrailer> movieTrailers) {
                activityDetailBinding.trailerProgressBar.setVisibility(View.GONE);
                activityDetailBinding.trailersErrorTv.setVisibility(View.GONE);
                CustomMoviesTrailerListAdapter customMoviesTrailerListAdapter = new CustomMoviesTrailerListAdapter(movieTrailers, new CustomMoviesTrailerListAdapter.ListItemClickListener() {
                    @Override
                    public void onItemClicked(MovieTrailer movieTrailer) {
                        String url = getString(R.string.youtube_base_url)+movieTrailer.getKey();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
                activityDetailBinding.trailerVideosRecyclerView.setAdapter(customMoviesTrailerListAdapter);
            }
        });

        MutableLiveData<String> trailersErrorMessage = movieTrailersViewModel.getErrorMessage();
        trailersErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                activityDetailBinding.trailersErrorTv.setText(s);
                activityDetailBinding.trailersErrorTv.setVisibility(View.VISIBLE);
                activityDetailBinding.trailerVideosRecyclerView.setVisibility(View.GONE);
                activityDetailBinding.trailerProgressBar.setVisibility(View.GONE);
            }
        });

        mutableMovieReviewsLiveData.observe(this, new Observer<ArrayList<MovieReview>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieReview> movieReviews) {
                activityDetailBinding.reviewProgressBar.setVisibility(View.GONE);
                activityDetailBinding.reviewsErrorTv.setVisibility(View.GONE);
                CustomMovieReviewsListAdapter customMovieReviewsListAdapter = new CustomMovieReviewsListAdapter(movieReviews);
                activityDetailBinding.reviewsRecyclerView.setAdapter(customMovieReviewsListAdapter);
            }
        });

        MutableLiveData<String> reviewsErrorMessage = movieReviewsViewModel.getErrorMessage();
        reviewsErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                activityDetailBinding.reviewsErrorTv.setText(s);
                activityDetailBinding.reviewsErrorTv.setVisibility(View.VISIBLE);
                activityDetailBinding.reviewsRecyclerView.setVisibility(View.GONE);
                activityDetailBinding.reviewProgressBar.setVisibility(View.GONE);
            }
        });

        MutableLiveData<Boolean> isWishlistedMovie = detailMovieViewModel.getIsMovieWishListed();
        isWishlistedMovie.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isMovieWishlisted = aBoolean;
                if(aBoolean){
                    activityDetailBinding.wishListImageView.setImageResource(R.drawable.ic_favorite);
                }else{
                    activityDetailBinding.wishListImageView.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });
    }

    private void updateUi() {
        Picasso.with(this)
                .load(ImageUtility.generateImageUrl(movie.getPosterPath()))
                .into(activityDetailBinding.coverImageView);
        Picasso.with(this)
                .load(ImageUtility.generateImageUrl(movie.getBackdropPath()))
                .into(activityDetailBinding.backgroundImageView);
        activityDetailBinding.descriptionTv.setText(movie.getOverview());
        activityDetailBinding.titleTv.setText(movie.getTitle());
        String voteAverage = movie.getVoteAverage() + "/10";
        activityDetailBinding.voteAverageTv.setText(voteAverage);
        activityDetailBinding.votesTotalTv.setText(String.format(getString(R.string.votes_value), movie.getVoteCount()));
        activityDetailBinding.releaseDateTv.setText(String.format(getString(R.string.release_date_value), movie.getReleaseDate()));
        activityDetailBinding.trailerVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityDetailBinding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void makeApiCall(){
        detailMovieViewModel.getDetailsIfMovieIsWishlistedInDb(movie.getId(), new DetailRepoistory(), wishListDataBase);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        if(isNetworkAvailable()){
            if(mutableMovieReviewsLiveData.getValue() == null) {
                activityDetailBinding.reviewProgressBar.setVisibility(View.VISIBLE);
                movieReviewsViewModel.getMovieReviewsFromServer(apiService, movie.getId(), new ReviewRepository());
            }
            if(mutableMovieTrailersLiveData.getValue() == null) {
                activityDetailBinding.trailerProgressBar.setVisibility(View.VISIBLE);
                movieTrailersViewModel.getMoviesFromServer(apiService, movie.getId(), new TrailerRepository());
            }
        }else{
            activityDetailBinding.reviewsErrorTv.setText(getString(R.string.please_check_internet_connection));
            activityDetailBinding.trailersErrorTv.setText(getString(R.string.please_check_internet_connection));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.wishListImageView):
                if(isMovieWishlisted){
                    detailMovieViewModel.removeMovieFromWishList(movie.getId(), new DetailRepoistory(), wishListDataBase);
                }else{
                    MovieEntity movieEntity = new MovieEntity(movie.getId(),movie.getVoteAverage(), movie.getTitle(), movie.getPopularity(),movie.getPosterPath(),movie.getOriginalLanguage(),movie.getOriginalTitle(),movie.getBackdropPath(),movie.getOverview(),movie.getReleaseDate(),movie.getVoteCount());
                    detailMovieViewModel.addMovieToWishList(movieEntity, new DetailRepoistory(), wishListDataBase);
                }
                break;
        }
    }
}
