package com.example.betterme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    String server_url_insert = "https://maindatabook.com/betterme_volleyandroid_insert.php";
    Button btnSignUp, btnlogin;
    EditText editTextUsername, editTextPassword, editTextConfirmPassword, editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify()) {
                    SignUp();
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private boolean verify() {
        boolean flag = true;
        if (TextUtils.isEmpty(editTextUsername.getText().toString().trim())) {
            editTextUsername.setError("Required!");
            flag = false;
        }
        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())) {
            editTextEmail.setError("Required!");
            flag = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()).matches()) {
            editTextEmail.setError("Invalid email address!");
            flag = false;
        }

        String pass = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            editTextPassword.setError("Required!");
            flag = false;
        } else if (TextUtils.isEmpty(confirmPass)) {
            editTextConfirmPassword.setError("Required!");
            flag = false;
        } else if (!confirmPass.equals(pass)) {
            editTextPassword.setError("Password Mismatch!");
            editTextConfirmPassword.setError("Password Mismatch!");
            flag = false;
        }

        return flag;
    }

    private void SignUp() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // editTextUsername.setText("Data:"+ response);
                if (response.contains("success")) {

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    Login();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                Log.d("onresponse=", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //editTextPassword.setText("Data: ERROR");
                Toast.makeText(getApplicationContext(), "Invalid Data Input", Toast.LENGTH_SHORT).show();
                Log.d("log2=", error.toString());
                Log.e("log3=", error.toString());
                progressDialog.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", editTextUsername.getText().toString());
                params.put("email", editTextUsername.getText().toString());
                params.put("password", editTextPassword.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void Login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }


}

