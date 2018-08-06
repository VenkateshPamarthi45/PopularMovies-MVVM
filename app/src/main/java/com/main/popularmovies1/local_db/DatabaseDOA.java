package com.main.popularmovies1.local_db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface DatabaseDOA {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(MovieEntity movieEntity);

    @Query("SELECT * FROM MovieEntity WHERE id =:id")
    LiveData<MovieEntity> getMovie(int id);

    @Query("SELECT * FROM MovieEntity WHERE id =:id")
    MovieEntity getMovieForWishListCheck(int id);

    @Query("SELECT * FROM MovieEntity")
    List<MovieEntity> getAllMovies();

    @Query("DELETE FROM MovieEntity where id =:id")
    void deleteMovie(int id);

}
