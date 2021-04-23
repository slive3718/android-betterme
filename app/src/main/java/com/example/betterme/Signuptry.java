package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Signuptry extends AppCompatActivity {

    Button btnSignUp;
    EditText editTextUsername,editTextPassword,editTextConfirmPassword,editTextEmail;
    SharedPreferences sharedPreferences;

        private static final String SHARED_PREF_NAME="";
        private static final String KEY_NAME="";
        private static final String KEY_PASSWORD="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuptry);



        btnSignUp=(Button) findViewById(R.id.btnSignUp);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword =(EditText) findViewById(R.id.editTextConfirmPassword);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);


        sharedPreferences=getSharedPreferences("",MODE_PRIVATE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


              Post_data();
            }
        });
    }


    public void Add() {
        RequestQueue queue= Volley.newRequestQueue(this);

        String url="http://192.168.0.14/BetterMe_Android_Git/Get_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                editTextUsername.setText("Data:" + response);

                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString(KEY_NAME,editTextUsername.getText().toString());
                     editor.putString(KEY_PASSWORD,editTextPassword.getText().toString());
                     editor.apply();



                Log.d("logr=",response);
                Log.d("session=",KEY_NAME);
                Log.d("session=",KEY_PASSWORD);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editTextUsername.setText("Data: Request Failed" );
                Log.d("log2=", error.toString());
                Log.e("log3=", error.toString());
            }
        });
        queue.add(stringRequest);
    }


    public void Post_data(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.11/Volley/Post_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                        editTextUsername.setText("Data:"+ response);


                Log.d("logr=",response);
            }} ,new Response.ErrorListener() {
                    @Override
                            public void onErrorResponse(VolleyError error){
                              editTextEmail.setText("Data: ERROR");
                        Log.d("log2=", error.toString());
                        Log.e("log3=", error.toString());
                }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("username", editTextUsername.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                params.put("confirmpassword", editTextConfirmPassword.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                return params;
            }

            @Override
            public  Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    }

