package com.main.popularmovies1.components.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.popularmovies1.R;
import com.main.popularmovies1.components.detail.model.MovieReview;
import com.main.popularmovies1.components.detail.model.MovieTrailer;
import com.main.popularmovies1.databinding.ReviewRowItemBinding;
import com.main.popularmovies1.databinding.TrailerRowItemBinding;

import java.util.ArrayList;

public class CustomMovieReviewsListAdapter extends RecyclerView.Adapter<CustomMovieReviewsListAdapter.ViewHolder> {

    private final ArrayList<MovieReview> movieReviews;

    public CustomMovieReviewsListAdapter(ArrayList<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @NonNull
    @Override
    public CustomMovieReviewsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ReviewRowItemBinding reviewRowItemBinding = DataBindingUtil.inflate(inflater,R.layout.review_row_item,parent,false);
        return new ViewHolder(reviewRowItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomMovieReviewsListAdapter.ViewHolder holder, int position) {
        MovieReview movieReview = movieReviews.get(position);
        holder.bindData(movieReview);
    }

    @Override
    public int getItemCount() {
        return movieReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final ReviewRowItemBinding reviewRowItemBinding;

        ViewHolder(ReviewRowItemBinding reviewRowItemBinding) {
            super(reviewRowItemBinding.getRoot());
            this.reviewRowItemBinding = reviewRowItemBinding;
        }

        void bindData(MovieReview movieReview){
            reviewRowItemBinding.reviewAuthorTextView.setText(movieReview.getAuthor());
            reviewRowItemBinding.reviewContentTextView.setText(movieReview.getContent());
        }
    }

}
