package com.example.movieapp;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {
    public View itemView;

    public MovieItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }
}
