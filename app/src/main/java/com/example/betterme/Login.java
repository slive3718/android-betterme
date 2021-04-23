package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.betterme.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    SessionManager sessionManager;
    Button btnlogin,btnCreateAccount;
    EditText editTextUsername,editTextPassword,editTextConfirmPassword,editTextEmail;
   // SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//   SharedPreferences.Editor editor = pref.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggin()){
            Homepage();
        }

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount) ;
        btnlogin=(Button) findViewById(R.id.btnlogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
                finish();
            }
        });
    }



    private void  Login(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("onresponse=",response);
                if (response.contains("success")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        sessionManager.createSession(new UserInfo(jsonObject.getJSONArray("usersInfo").getJSONObject(0)));
                        Homepage();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }
                Log.d("onresponse=",response);
            }} ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                //editTextPassword.setText("Data: ERROR");
                Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                Log.d("log2=", error.toString());
                progressDialog.dismiss();
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("username", editTextUsername.getText().toString());
                params.put("password", editTextPassword.getText().toString());

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



    private void Homepage(){
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }

    private void Register(){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}