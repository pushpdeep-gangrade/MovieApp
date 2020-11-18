package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password, reEnterPassword;
    Button signUp, cancel;
    final String url = "http://104.248.113.55:3001/v1/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        reEnterPassword = findViewById(R.id.signup_reEnterPassword);
        signUp = findViewById(R.id.signup_signupButton);
        cancel = findViewById(R.id.signup_cancelButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signupUrl = url + "signup";

                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

                StringRequest postRequest = new StringRequest(Request.Method.POST, signupUrl,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                if(response.equals("Email id already registered")){
                                    Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                                    finish();
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
                    protected Map<String, String> getParams()
                    {
                        String emailText = email.getText().toString();
                        String passwordText = password.getText().toString();
                        String reEnterPasswordText = reEnterPassword.getText().toString();

                        Map<String, String>  params = new HashMap<>();

                        boolean allValid = true;


                        if (emailText.equals("")) {
                            email.setError("Please enter your email");
                            allValid = false;
                        }


                        if (!passwordText.equals(reEnterPasswordText) || passwordText.equals("")) {
                            if (!passwordText.equals(reEnterPasswordText)) {
                                password.setError("Passwords do not match");
                            } else {
                                password.setError("Please enter a password");
                            }
                            allValid = false;
                        }

                        if(allValid){
                            params.put("email", emailText);
                            params.put("password", passwordText);
                        }

                        return params;
                    }
                };

                queue.add(postRequest);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}