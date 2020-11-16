package com.example.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.models.Actor;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {
    Context context;

    String mAuthorizationkey;
    List<Actor> actorArrayList = new ArrayList<>();

    public MovieItemAdapter(@NonNull Context context, int resource, @NonNull List<Actor> actorArrayList,
                            String mAuthorizationkey) {

        this.context = context;
        this.mAuthorizationkey = mAuthorizationkey;
        this.actorArrayList = actorArrayList;

        Log.d("In Constructor", "In Constructor");

    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("In On Create View", "In On Create View");
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_item, parent, false);

        MovieItemViewHolder vh = new MovieItemViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        final Actor actor = actorArrayList.get(position);

        TextView actorName = holder.itemView.findViewById(R.id.actorItem_name);
        TextView actorProfession = holder.itemView.findViewById(R.id.actorItem_profession);
        TextView actorBYear = holder.itemView.findViewById(R.id.actorItem_bYear);
        TextView actorDYear = holder.itemView.findViewById(R.id.actorItem_dYear);

        assert actor != null;
        actorName.setText(actor.name);
        actorProfession.setText(actor.profession);
        actorBYear.setText(String.valueOf(actor.bYear));
        actorDYear.setText(String.valueOf(actor.dYear));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Item clicked", actor.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return actorArrayList.size();
    }


}
