package com.example.movieapp.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Actor {
    @PrimaryKey @NonNull
    public String id;

    @ColumnInfo(name = "full_name")
    public String name;

    @ColumnInfo(name = "profession")
    public String profession;

    @ColumnInfo(name = "b_Year")
    public int bYear;

    @ColumnInfo(name = "d_Year")
    public String dYear;
}
