package com.example.betterme;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.betterme.model.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivAvatar;
    AppCompatSpinner spinner;
    EditText etFirstName, etMiddleName, etLastName, etBirthdate, etAge, etWeight, etHeight, etCity, etProvince, etContact;
    final Calendar myCalendar = Calendar.getInstance();
    String[] gender = {"Male", "Female"};
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etBirthdate = findViewById(R.id.etBirthdate);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etCity = findViewById(R.id.etCity);
        etProvince = findViewById(R.id.etProvice);
        etContact = findViewById(R.id.etContact);
        spinner = findViewById(R.id.spinner);
        ivAvatar = findViewById(R.id.ivAvatar);
        initDaptePicker();
        initSpinner();
        initUserInfo();
    }

    private void initDaptePicker() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etBirthdate.setOnClickListener(v -> {
            new DatePickerDialog(ProfileActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "MMM dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this,
                android.R.layout.simple_spinner_item, gender);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUserInfo() {
        userInfo = AppApplication.getInstance().getCurrentUser();
        etFirstName.setText(userInfo.getFirst_name());
        etMiddleName.setText(userInfo.getMiddle_name());
        etLastName.setText(userInfo.getLast_name());
        etHeight.setText(userInfo.getHeight());
        etWeight.setText(userInfo.getWeight());
        etAge.setText(userInfo.getAge());
        etCity.setText(userInfo.getCity());
        etProvince.setText(userInfo.getProvince());
        etContact.setText(userInfo.getContact());
        if (gender[1].equalsIgnoreCase(userInfo.getSex())) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(0);
        }
        myCalendar.setTime(DateHelper.getDateFromString(userInfo.getDob(), DateHelper.SERVER_DATE_FORMAT));
        updateLabel();

        Glide.with(this)
                .load(userInfo.getImageURL())
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivAvatar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_community_thread_post, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuPost:
                post();
//                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void post() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                userInfo.setFirst_name(etFirstName.getText().toString().trim());
                userInfo.setMiddle_name(etMiddleName.getText().toString().trim());
                userInfo.setLast_name(etLastName.getText().toString().trim());
                userInfo.setAge(etAge.getText().toString().trim());
                userInfo.setSex(gender[spinner.getSelectedItemPosition()]);
                userInfo.setCity(etCity.getText().toString().trim());
                userInfo.setProvince(etProvince.getText().toString().trim());
                userInfo.setContact(etContact.getText().toString().trim());
                SessionManager sessionManager = new SessionManager(ProfileActivity.this);
                sessionManager.createSession(userInfo);
                AppApplication.getInstance().setCurrentUser(userInfo);
                Toast.makeText(getApplicationContext(), "Successfully updated.", Toast.LENGTH_SHORT).show();
                // editTextUsername.setText("Data:"+ response);
//                if (response.contains("success")) {
//                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();


//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid Data Input", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", AppApplication.getInstance().getCurrentUser().getId());
                params.put("first_name", etFirstName.getText().toString().trim());
                params.put("middle_name", etMiddleName.getText().toString().trim());
                params.put("last_name", etLastName.getText().toString().trim());
                params.put("age", etAge.getText().toString().trim());
                params.put("sex", gender[spinner.getSelectedItemPosition()]);
                params.put("city", etCity.getText().toString().trim());
                params.put("province", etProvince.getText().toString().trim());
                params.put("contact", etContact.getText().toString().trim());

                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.SERVER_DATE_FORMAT, Locale.US);
                params.put("dob", sdf.format(myCalendar.getTime()));

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

}