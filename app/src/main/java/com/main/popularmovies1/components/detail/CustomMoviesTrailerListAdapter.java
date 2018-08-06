package com.main.popularmovies1.components.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.popularmovies1.R;
import com.main.popularmovies1.components.detail.model.MovieTrailer;
import com.main.popularmovies1.databinding.TrailerRowItemBinding;

import java.util.ArrayList;

public class CustomMoviesTrailerListAdapter extends RecyclerView.Adapter<CustomMoviesTrailerListAdapter.ViewHolder> {

    private final ArrayList<MovieTrailer> movieArrayList;
    private final ListItemClickListener listItemClickListener;

    public CustomMoviesTrailerListAdapter(ArrayList<MovieTrailer> movieArrayList, ListItemClickListener listItemClickListener) {
        this.movieArrayList = movieArrayList;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public CustomMoviesTrailerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        TrailerRowItemBinding trailerRowItemBinding = DataBindingUtil.inflate(inflater,R.layout.trailer_row_item,parent,false);
        return new ViewHolder(trailerRowItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomMoviesTrailerListAdapter.ViewHolder holder, int position) {
        MovieTrailer movieTrailer = movieArrayList.get(position);
        holder.bindImage(movieTrailer);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public interface ListItemClickListener {
        void onItemClicked(MovieTrailer movieTrailer);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TrailerRowItemBinding trailerRowItemBinding;

        ViewHolder(TrailerRowItemBinding trailerRowItemBinding) {
            super(trailerRowItemBinding.getRoot());
            this.trailerRowItemBinding = trailerRowItemBinding;
            trailerRowItemBinding.getRoot().setOnClickListener(this);
        }

        void bindImage(MovieTrailer movieTrailer){
            trailerRowItemBinding.trailerNameTextView.setText(movieTrailer.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            MovieTrailer movieTrailer = movieArrayList.get(clickedPosition);
            listItemClickListener.onItemClicked(movieTrailer);
        }
    }

}
