package com.example.movieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.db.AppDatabase;
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
    AppDatabase db;
    RecyclerView rv;
    boolean loadFavorites;

    public MovieItemAdapter(@NonNull Context context, int resource, @NonNull List<Actor> actorArrayList,
                            String mAuthorizationkey, AppDatabase db, RecyclerView rv, boolean loadFavorites) {

        this.context = context;
        this.mAuthorizationkey = mAuthorizationkey;
        this.actorArrayList = actorArrayList;
        this.db = db;
        this.rv = rv;
        this.loadFavorites = loadFavorites;

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
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, final int position) {
        final Actor actor = actorArrayList.get(position);

        TextView actorName = holder.itemView.findViewById(R.id.actorItem_name);
        TextView actorProfession = holder.itemView.findViewById(R.id.actorItem_profession);
        TextView actorBYear = holder.itemView.findViewById(R.id.actorItem_bYear);
        TextView actorDYear = holder.itemView.findViewById(R.id.actorItem_dYear);
        final CheckBox favorite = holder.itemView.findViewById(R.id.favorite_checkBox);

        assert actor != null;
        actorName.setText(actor.name);
        actorProfession.setText(actor.profession);
        actorBYear.setText(String.valueOf(actor.bYear));
        actorDYear.setText(String.valueOf(actor.dYear));

        if(actor.personalStatus.equals("favorite")){
            favorite.setChecked(true);
        }
        else{
            favorite.setChecked(false);
        }

        /*favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    actor.personalStatus = "favorite";
                    db.actorDAO().updateStatus(actor);
                }
                else{
                    actor.personalStatus = "general";
                    db.actorDAO().updateStatus(actor);
                }
            }
        });*/

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //favorite.setChecked(!favorite.isChecked());
                if(favorite.isChecked()){
                    actor.personalStatus = "favorite";
                    db.actorDAO().updateStatus(actor);
                }
                else{
                    actor.personalStatus = "general";
                    db.actorDAO().updateStatus(actor);
                    if(loadFavorites){
                        actorArrayList = db.actorDAO().getFavorites();
                        setMovieItemRecyclerView();
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Item clicked", actor.toString());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this record?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.actorDAO().delete(actor);
                                actorArrayList.remove(actor);
                                setMovieItemRecyclerView();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                builder.create();
                builder.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return actorArrayList.size();
    }

    public void setMovieItemRecyclerView(){
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);

        final MovieItemAdapter ad = new MovieItemAdapter(context,
                android.R.layout.simple_list_item_1, actorArrayList,  mAuthorizationkey, db, rv, loadFavorites);

        rv.setAdapter(ad);
    }

}
