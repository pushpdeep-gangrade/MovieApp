package com.example.movieapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapp.models.Actor;

import java.util.List;

@Dao
public interface ActorDAO {

    @Query("SELECT * FROM actor")
    List<Actor> getAll();

    @Query("SELECT * FROM actor WHERE actor.active_status = 'current'")
    List<Actor> getCurrent();

    @Query("SELECT * FROM actor WHERE actor.active_status = 'previous'")
    List<Actor> getPrevious();

    @Query("SELECT * FROM actor WHERE actor.personal_status = 'favorite'")
    List<Actor> getFavorites();

    @Update
    void updateStatus(Actor actor);

    @Update
    void updateAllStatus(List<Actor> actors);

    @Insert
    void insertAll(List<Actor> actors);

    @Insert
    void insertOne(Actor actor);

    @Delete
    void delete(Actor actor);

    @Delete
    void deleteAll(List<Actor> actors);

}
