package com.main.popularmovies1.components.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.popularmovies1.R;
import com.main.popularmovies1.components.home.model.Movie;
import com.main.popularmovies1.databinding.MovieGridItemBinding;
import com.main.popularmovies1.utilities.ImageUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomMoviesListAdapter extends RecyclerView.Adapter<CustomMoviesListAdapter.ViewHolder> {

    private final ArrayList<Movie> movieArrayList;
    private final ListItemClickListener listItemClickListener;

    public CustomMoviesListAdapter(ArrayList<Movie> movieArrayList, ListItemClickListener listItemClickListener) {
        this.movieArrayList = movieArrayList;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public CustomMoviesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        MovieGridItemBinding movieGridItemBinding = DataBindingUtil.inflate(inflater,R.layout.movie_grid_item,parent,false);
        return new ViewHolder(movieGridItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomMoviesListAdapter.ViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        holder.bindImage(movie);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public interface ListItemClickListener {
        void onItemClicked(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final MovieGridItemBinding movieGridItemBinding;

        ViewHolder(MovieGridItemBinding movieGridItemBinding) {
            super(movieGridItemBinding.getRoot());
            this.movieGridItemBinding = movieGridItemBinding;
            movieGridItemBinding.getRoot().setOnClickListener(this);
        }

        void bindImage(Movie movie){
            Picasso.with(movieGridItemBinding.getRoot().getContext())
                    .load(ImageUtility.generateImageUrl(movie.getPosterPath()))
                    .into(movieGridItemBinding.movieImageView);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie movie = movieArrayList.get(clickedPosition);
            listItemClickListener.onItemClicked(movie);
        }
    }

}
