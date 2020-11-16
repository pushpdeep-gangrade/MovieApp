package com.example.movieapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.models.Actor;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final String AUTH_KEY = "authorizationkey";
    private String mAuthorizationkey;
    public static final String url = "http://10.0.2.2:3000/v1/actors";
    List<Actor> currentActors = new ArrayList<>();
    Button sort, filter;
    RecyclerView movieList;
    String sortStr = "Name";
    String orderStr = "Ascending";
    String filterByStr = "Name";
    String conditionStr = "Equal To";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sort = findViewById(R.id.main_sortButton);
        filter = findViewById(R.id.main_filterButton);
        movieList = findViewById(R.id.main_recyclerView);

        final SharedPreferences sharedPref = getSharedPreferences("info", Context.MODE_PRIVATE);
        if(sharedPref!=null){
            mAuthorizationkey = sharedPref.getString("authKey","");
            Log.d("mauth", mAuthorizationkey);
            loadInitialActors();
            //load();
        }

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sort_dialog_layout, null);

                setSortDialogLayout(dialoglayout);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.filter_dialog_layout, null);

                setFilterDialogLayout(dialoglayout);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialoglayout);
                builder.show();
            }
        });
    }

    private void loadInitialActors(){
        String actorUrl = MainActivity.url;

        RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest getRequest = new StringRequest(Request.Method.POST, actorUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if(response.equals("UNAUTHORIZED")){
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                JSONArray actorArray = new JSONArray(response);

                                for(int i = 0; i < actorArray.length(); i++){
                                    JSONObject actorObject = actorArray.getJSONObject(i);

                                    Actor actor = new Actor();

                                    actor.id = actorObject.getString("_id");
                                    actor.name = actorObject.getString("primaryName");
                                    actor.profession = actorObject.getString("primaryProfession");
                                    actor.bYear = actorObject.getInt("birthYear");
                                    try{
                                        actor.dYear = String.valueOf(actorObject.getInt("deathYear"));
                                    }
                                    catch (Exception e){
                                        actor.dYear = actorObject.getString("deathYear");
                                    }

                                    currentActors.add(actor);

                                }

                                setMovieItemRecyclerView();

                                Log.d("Actors", currentActors.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, "Loaded Actors", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("authorizationkey", mAuthorizationkey);

                return params;
            }
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<>();

                params.put("filters", "");
                params.put("limit", "50");
                params.put("skips", "0");
                params.put("sortMethod", "");

                return params;

            };
        };

        queue.add(getRequest);
    }

    public void setMovieItemRecyclerView(){
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        movieList.setLayoutManager(layoutManager);

        final MovieItemAdapter ad = new MovieItemAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, currentActors,  mAuthorizationkey);

        movieList.setAdapter(ad);
    }

    private void setSortDialogLayout(View dialoglayout){
        final String[] arraySpinnerSort = new String[] {
                "Name", "Birth Year", "Death Year"
        };
        String[] arraySpinnerOrder = new String[] {
                "Ascending", "Descending"
        };

        Spinner sort_spinner = (Spinner) dialoglayout.findViewById(R.id.sort_by_spinner);
        Spinner order_spinner = (Spinner) dialoglayout.findViewById(R.id.order_spinner);
        Button sort_button = (Button) dialoglayout.findViewById(R.id.dialog_sort_button);

        ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerSort);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapterSort);

        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerOrder);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_spinner.setAdapter(adapterOrder);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortStr = arraySpinnerSort[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        order_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderStr = arraySpinnerSort[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setFilterDialogLayout(View dialoglayout){
        final String[] arraySpinnerFilterBy = new String[] {
                "Name", "Profession", "Birth Year", "Death Year"
        };

        final String[] arraySpinnerCondition = new String[] {
                 "Equal To", "Less Than", "Greater Than"
        };

        Spinner filter_by_spinner = (Spinner) dialoglayout.findViewById(R.id.filter_filter_by_spinner);
        Spinner condition_spinner = (Spinner) dialoglayout.findViewById(R.id.filter_condition_spinner);
        Button filter_button = (Button) dialoglayout.findViewById(R.id.filter_filter_button);
        EditText condition_text = (EditText) dialoglayout.findViewById(R.id.filter_condition_text);

        ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerFilterBy);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_by_spinner.setAdapter(adapterSort);

        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerCondition);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        condition_spinner.setAdapter(adapterOrder);

        filter_by_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterByStr = arraySpinnerFilterBy[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        condition_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conditionStr = arraySpinnerCondition[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}