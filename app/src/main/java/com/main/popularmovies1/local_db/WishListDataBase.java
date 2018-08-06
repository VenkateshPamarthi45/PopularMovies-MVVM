package com.main.popularmovies1.local_db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.main.popularmovies1.components.home.model.Movie;

@Database(entities = {MovieEntity.class}, version = 1)
public abstract class WishListDataBase extends RoomDatabase {

    public abstract DatabaseDOA databaseDOA();

}
