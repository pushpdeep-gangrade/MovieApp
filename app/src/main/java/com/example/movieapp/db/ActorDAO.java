package com.example.movieapp.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.movieapp.models.Actor;

import java.util.List;

@Dao
public interface ActorDAO {

    @Query("SELECT * FROM actor")
    List<Actor> getAll();
}
