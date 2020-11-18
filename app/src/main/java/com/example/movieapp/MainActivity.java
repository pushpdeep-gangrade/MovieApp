package com.example.movieapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String url = "http://104.248.113.55:3001/v1/actors";
    List<Actor> currentActors = new ArrayList<>();
    Button sort;
    RecyclerView movieList;
    String sortStr = "primaryName";
    String orderStr = "Ascending";
    String filterByStr = "primaryName";
    String conditionStr = "Equal To";
    String conditionEditText = "";
    int loadMoreSkip = 0;
    EditText conditionEdit;
    Button loadMore, loadPrevious, loadFavorites;
    boolean useSort = false, useFilter = false;
    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sort = findViewById(R.id.main_sortButton);
        movieList = findViewById(R.id.main_recyclerView);
        loadMore = findViewById(R.id.main_load_more_button);
        loadPrevious = findViewById(R.id.main_load_previous_button);
        loadFavorites = findViewById(R.id.main_load_favorites_button);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "movieDB").allowMainThreadQueries().build();

        final SharedPreferences sharedPref = getSharedPreferences("info", Context.MODE_PRIVATE);

        if(sharedPref!=null){
            mAuthorizationkey = sharedPref.getString("authKey","");
            Log.d("mauth", mAuthorizationkey);

            /*loadMoreSkip = sharedPref.getInt("loadMoreSkip",0);
            Log.d("Load More Skip", String.valueOf(loadMoreSkip));*/

            if(db.actorDAO().getAll().size() == 0){
                loadInitialActors();
            }
            else{
                setMovieItemRecyclerView(db.actorDAO().getCurrent(), false);
            }

            if(db.actorDAO().getPrevious().size() > 0){
                loadPrevious.setVisibility(View.VISIBLE);
            }
            else{
                loadPrevious.setVisibility(View.GONE);
            }
        }

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.sort_dialog_layout, null);

                setSortFilterDialogLayout(dialoglayout);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        loadFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadFavorites.getText().toString().equals("Load Favorites")){
                    setMovieItemRecyclerView(db.actorDAO().getFavorites(), true);
                    loadFavorites.setText("Load General");
                    loadPrevious.setVisibility(View.GONE);

                }
                else if(loadFavorites.getText().toString().equals("Load General")){
                    setMovieItemRecyclerView(db.actorDAO().getCurrent(), false);
                    loadFavorites.setText("Load Favorites");
                    if(db.actorDAO().getPrevious().size() > 0){
                        loadPrevious.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreSkip += 50;
                db.actorDAO().deleteAll(db.actorDAO().getPrevious());
                ArrayList<Actor> updated = new ArrayList<>();
                for(int i = 0; i < db.actorDAO().getCurrent().size(); i++){
                    Actor actor = db.actorDAO().getCurrent().get(i);
                    actor.activeStatus = "previous";
                    updated.add(actor);
                }
                db.actorDAO().updateAllStatus(updated);
                loadElements(true);
                loadPrevious.setVisibility(View.VISIBLE);

               /* sharedPref.edit().putInt("loadMoreSkip", loadMoreSkip).commit();

                loadMoreSkip = sharedPref.getInt("loadMoreSkip",0);
                Log.d("Load More Skip", String.valueOf(loadMoreSkip));*/
            }
        });

        loadPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreSkip -= 50;

                if(loadMoreSkip < 0){
                    loadMoreSkip = 0;
                }

                db.actorDAO().deleteAll(db.actorDAO().getCurrent());
                ArrayList<Actor> updated = new ArrayList<>();
                for(int i = 0; i < db.actorDAO().getPrevious().size(); i++){
                    Actor actor = db.actorDAO().getPrevious().get(i);
                    actor.activeStatus = "current";
                    updated.add(actor);
                }
                db.actorDAO().updateAllStatus(updated);
                loadElements(false);
                loadPrevious.setVisibility(View.GONE);


            /*    if(loadMoreSkip >= 0 && db.actorDAO().getPrevious().size() == 0){
                    db.actorDAO().deleteAll(db.actorDAO().getCurrent());
                    loadElements(true);
                }
                else if(db.actorDAO().getPrevious().size() > 0){
                    db.actorDAO().deleteAll(db.actorDAO().getCurrent());
                    ArrayList<Actor> updated = new ArrayList<>();
                    for(int i = 0; i < db.actorDAO().getPrevious().size(); i++){
                        Actor actor = db.actorDAO().getPrevious().get(i);
                        actor.activeStatus = "current";
                        updated.add(actor);
                    }
                    db.actorDAO().updateAllStatus(updated);
                    loadElements(false);
                }

                if(loadMoreSkip == 0){
                    loadPrevious.setVisibility(View.GONE);
                }

                sharedPref.edit().putInt("loadMoreSkip", loadMoreSkip).commit();

                loadMoreSkip = sharedPref.getInt("loadMoreSkip",0);
                Log.d("Load More Skip", String.valueOf(loadMoreSkip));*/


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
                                currentActors.clear();
                                loadMoreSkip = 0;

                                for(int i = 0; i < actorArray.length(); i++){
                                    JSONObject actorObject = actorArray.getJSONObject(i);

                                    Actor actor = new Actor();

                                    actor.id = actorObject.getString("_id");
                                    actor.name = actorObject.getString("primaryName");
                                    actor.profession = actorObject.getString("primaryProfession");
                                    actor.activeStatus = "current";
                                    actor.personalStatus = "general";

                                    try{
                                        actor.bYear = String.valueOf(actorObject.getInt("birthYear"));
                                    }
                                    catch (Exception e){
                                        actor.bYear = actorObject.getString("birthYear");
                                    }
                                    try{
                                        actor.dYear = String.valueOf(actorObject.getInt("deathYear"));
                                    }
                                    catch (Exception e){
                                        actor.dYear = actorObject.getString("deathYear");
                                    }

                                    currentActors.add(actor);

                                    db.actorDAO().insertOne(actor);

                                }

                                setMovieItemRecyclerView(db.actorDAO().getCurrent(), false);

                                Log.d("Actors", db.actorDAO().getAll().toString());
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

                params.put("filters", "{}");
                params.put("limit", "50");
                params.put("skips", "0");
                params.put("sortMethod", "{}");

                return params;

            };
        };

        queue.add(getRequest);
    }

    private void loadElements(final boolean add){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.addHeader("authorizationkey", mAuthorizationkey);

        String sort = "{}";

        Log.d("Order str", orderStr);

        if(useSort){
            if(orderStr.equals("Ascending")){
                sort = "{\"" + sortStr + "\": 1}";
                Log.d("Sort str", sort);
            }
            else{
                sort = "{\"" + sortStr + "\": -1}";
                Log.d("Sort str", sort);
            }
        }


        if(conditionEdit != null){
            conditionEditText = conditionEdit.getText().toString();
        }

        String filter = "{}";

        if(!conditionEditText.equals("") && useFilter){
            if(conditionStr.equals("Greater Than")){
                filter = "{\n" +
                        "        \"$and\":[\n" +
                        "            {\n" +
                        "                \"" + filterByStr + "\": {\"$gte\":" + conditionEditText + "}\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }";
            }
            else if(conditionStr.equals("Less Than")){
                filter = "{\n" +
                        "        \"$and\":[\n" +
                        "            {\n" +
                        "                \"" + filterByStr + "\": {\"$lte\":" + conditionEditText + "}\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }";
            }
            else if(conditionStr.equals("Equal To")){
                filter = "{\n" +
                        "        \"$and\":[\n" +
                        "            {\n" +
                        "                \"" + filterByStr + "\": {\"$eq\":" + conditionEditText + "}\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }";
            }
        }

        Log.d("Filter str", filter);

        params.put("filters", filter);
        params.put("limit", "50");
        params.put("skips", String.valueOf(loadMoreSkip));
        params.put("sortMethod", sort);

        client.post(MainActivity.url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String str = new String(responseBody, "UTF-8");

                            try {
                                JSONArray actorArray = new JSONArray(str);
                                currentActors.clear();

                                if(!add){
                                    db.actorDAO().deleteAll(db.actorDAO().getCurrent());
                                }

                                for(int i = 0; i < actorArray.length(); i++){
                                    JSONObject actorObject = actorArray.getJSONObject(i);

                                    Actor actor = new Actor();

                                    actor.id = actorObject.getString("_id");
                                    actor.name = actorObject.getString("primaryName");
                                    actor.profession = actorObject.getString("primaryProfession");
                                    actor.activeStatus = "current";
                                    actor.personalStatus = "general";

                                    try{
                                        actor.bYear = String.valueOf(actorObject.getInt("birthYear"));
                                    }
                                    catch (Exception e){
                                        actor.bYear = actorObject.getString("birthYear");
                                    }
                                    try{
                                        actor.dYear = String.valueOf(actorObject.getInt("deathYear"));
                                    }
                                    catch (Exception e){
                                        actor.dYear = actorObject.getString("deathYear");
                                    }

                                    db.actorDAO().insertOne(actor);
                                }

                                currentActors = db.actorDAO().getCurrent();

                                setMovieItemRecyclerView(db.actorDAO().getCurrent(), false);

                                Log.d("Actors", currentActors.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, "Actors loaded successfully", Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MainActivity.this, "Unable to load actors", Toast.LENGTH_SHORT).show();
                        String str = "";

                        try {
                            str = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Log.d("demo", str);
                    }
                }
        );
    }

    public void setMovieItemRecyclerView(List<Actor> actorList, boolean loadFavorites){
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        movieList.setLayoutManager(layoutManager);

        final MovieItemAdapter ad = new MovieItemAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, actorList,  mAuthorizationkey, db, movieList, loadFavorites);

        movieList.setAdapter(ad);
    }

    private void setSortFilterDialogLayout(View dialoglayout){
        final String[] arraySpinnerSort = new String[] {
                "Name", "Birth Year", "Death Year"
        };
        final String[] arraySpinnerOrder = new String[] {
                "Ascending", "Descending"
        };

        final String[] arraySpinnerFilterBy = new String[] {
                "Name", "Profession", "Birth Year", "Death Year"
        };

        final String[] arraySpinnerCondition = new String[] {
                "Equal To", "Less Than", "Greater Than"
        };

        Spinner sort_spinner = (Spinner) dialoglayout.findViewById(R.id.sort_by_spinner);
        Spinner order_spinner = (Spinner) dialoglayout.findViewById(R.id.order_spinner);
        Button sort_button = (Button) dialoglayout.findViewById(R.id.dialog_sort_button);

        Spinner filter_by_spinner = (Spinner) dialoglayout.findViewById(R.id.filter_filter_by_spinner);
        Spinner condition_spinner = (Spinner) dialoglayout.findViewById(R.id.filter_condition_spinner);
        EditText condition_text = (EditText) dialoglayout.findViewById(R.id.filter_condition_text);

        Switch useSortSwitch = (Switch) dialoglayout.findViewById(R.id.use_sort_switch);
        Switch useFilterSwitch = (Switch) dialoglayout.findViewById(R.id.use_filter_switch);

        useSortSwitch.setChecked(useSort);
        useFilterSwitch.setChecked(useFilter);

        if(!conditionEditText.equals("")){
            condition_text.setText(conditionEditText);
        }

        conditionEdit = condition_text;

        ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerSort);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapterSort);

        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerOrder);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_spinner.setAdapter(adapterOrder);

        ArrayAdapter<String> adapterFilterBy = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerFilterBy);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_by_spinner.setAdapter(adapterFilterBy);

        ArrayAdapter<String> adapterCondition = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, arraySpinnerCondition);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        condition_spinner.setAdapter(adapterCondition);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortStr = arraySpinnerSort[i];
                if(sortStr.equals("Name")){
                    sortStr = "primaryName";
                }
                else if(sortStr.equals("Birth Year")){
                    sortStr = "birthYear";
                }
                else if(sortStr.equals("Death Year")){
                    sortStr = "deathYear";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        order_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderStr = arraySpinnerOrder[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filter_by_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterByStr = arraySpinnerFilterBy[i];
                if(filterByStr.equals("Name")){
                    filterByStr = "primaryName";
                }
                else if(filterByStr.equals("Birth Year")){
                    filterByStr = "birthYear";
                }
                else if(filterByStr.equals("Death Year")){
                    filterByStr = "deathYear";
                }
                else if(filterByStr.equals("Profession")){
                    filterByStr = "primaryProfession";
                }
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

        useSortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useSort = b;
            }
        });

        useFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useFilter = b;
            }
        });

        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadElements(false);
            }
        });

    }





}