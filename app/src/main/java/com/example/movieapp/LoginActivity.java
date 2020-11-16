package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private static final String AUTH_KEY = "authorizationkey";
    private String authkey;
    Button login;
    EditText email, password;
    Button createAccount;
    final String url = "http://10.0.2.2:3000/v1/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_loginButton);
        createAccount = findViewById(R.id.login_createAccountLink);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "login");

                String loginUrl = url + "/login";

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                StringRequest postRequest = new StringRequest(Request.Method.POST, loginUrl,
                        new Response.Listener<String>() {
                            @SuppressLint("ApplySharedPref")
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                if (response.equals("Invalid Credentials") || response.equals("No such user found")) {
                                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                                } else {
                                    try {

                                        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
                                        prefs.edit().putString("authKey", authkey).commit();

                                        Log.d("Login", response);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent gotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle authBundle = new Bundle();

                                    authBundle.putString(AUTH_KEY, authkey); //Your id

                                    SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);
                                    prefs.edit().putString("authKey", authkey).commit();

                                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

                                    gotoMainActivity.putExtras(authBundle); //Put your id to your next Intent
                                    startActivity(gotoMainActivity);
                                    finish();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                NetworkResponse response = error.networkResponse;
                                String errorMsg = "";
                                if (response != null && response.data != null) {
                                    String errorString = new String(response.data);
                                    Log.i("log error", errorString);
                                }
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        String emailText = email.getText().toString();
                        String passwordText = password.getText().toString();

                        Map<String, String> params = new HashMap<>();

                        params.put("email", emailText);
                        params.put("password", passwordText);

                        return params;
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        Log.d("TEST", "Headers size:" + response.headers.size());
                        authkey = response.headers.get("AuthorizationKey");
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        if (volleyError.networkResponse != null && volleyError.networkResponse.headers != null) {
                            Log.d("TEST", "Headers size:" + volleyError.networkResponse.headers.size());
                            authkey = null;
                        }
                        return super.parseNetworkError(volleyError);
                    }
                };
                queue.add(postRequest);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(gotoSignUp);
            }
        });
    }
}