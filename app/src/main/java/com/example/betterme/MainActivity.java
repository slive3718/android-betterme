package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    private void Activity_Homepage(){
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }
    private void Activity_SignUp(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    private void Activity_Navigation(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


    }





