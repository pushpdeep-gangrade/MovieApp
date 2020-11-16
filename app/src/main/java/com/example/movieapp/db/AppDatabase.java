package com.example.movieapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.movieapp.models.Actor;

@Database(entities = {Actor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActorDAO actorDAO();
}
