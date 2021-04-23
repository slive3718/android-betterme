package com.example.betterme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.betterme.model.SetComment;
import com.example.betterme.model.SetDiets;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DietDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SetDiets diets;
    SwipeRefreshLayout refreshLayout;
    String image_foloder_path = "http://192.168.0.14/betterMeWeb/uploads/posts/";
    DietDetailsAdapter dietDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("diet")) {
            diets = (SetDiets) getIntent().getSerializableExtra("diet");
            getSupportActionBar().setTitle(diets.getTitle());
        } else {
            finish();
        }
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        dietDetailsAdapter = new DietDetailsAdapter(this, diets, new DietDetailsAdapter.OnClickListener() {

            @Override
            public void onClickComment(SetComment comment, String message) {
                hideKeyboard();
                if (comment == null) {
                    postComment(message);
                } else {
                    updateComment(comment, message);
                }
            }

            @Override
            public void onClickLike() {
                Boolean isLike = !diets.getLike_status().equalsIgnoreCase("1");
                likeDiet(isLike);
                diets.setLike_status(isLike ? "1" : "0");
                if (isLike) {
                    diets.setGetLikeCount(diets.getGetLikeCount() + 1);
                } else {
                    diets.setGetLikeCount(diets.getGetLikeCount() - 1);
                }
                dietDetailsAdapter.notifyItemChanged(0);// notify header to update like icon
            }

            @Override
            public void onClickDeleteComment(SetComment comment) {
                deleteComment(comment);
            }
        });
        recyclerView.setAdapter(dietDetailsAdapter);
        refreshLayout.setEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void postComment(String message) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.POST_DIET_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("id")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SetComment comment = new SetComment(jsonObject);
                        diets.getComments().add(comment);
                        dietDetailsAdapter.notifyItemInserted(diets.getComments().size()); // Refresh recyclerview in position
                        dietDetailsAdapter.notifyItemChanged(0); // Notify recyclerview header
                        dietDetailsAdapter.notifyItemChanged(dietDetailsAdapter.getItemCount()-1); // Notify recyclerview footer
                        updateResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Somethingwent wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("tag", "onErrorResponse:" + error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", AppApplication.getInstance().getCurrentUser().getId());
                params.put("post_id", diets.getId());
                params.put("comment", message);
                Log.e("tag", "Params:" + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void updateComment(SetComment comment, String message) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.EDIT_DIET_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("success")) {
                    for (int i = 0; i < diets.getComments().size(); i++) {
                        if (comment.getId().equalsIgnoreCase(diets.getComments().get(i).getId())) {
                            diets.getComments().get(i).setComment(message);
                            dietDetailsAdapter.notifyItemChanged(i + 1); // Notify recyclerview update at position
                            dietDetailsAdapter.editingComment = null;
                            dietDetailsAdapter.notifyItemChanged(dietDetailsAdapter.getItemCount()-1); // Notify recyclerview footer
                            break;
                        }
                    }
                    updateResult();
                } else {
                    Toast.makeText(getApplicationContext(), "Somethingwent wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("tag", "onErrorResponse:" + error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comment_id", comment.getId());
                params.put("comment", message);
                Log.e("tag", "Params:" + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void deleteComment(SetComment comment) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.DELETE_DIET_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("success")) {
                    for (int i = 0; i < diets.getComments().size(); i++) {
                        if (comment.getId().equalsIgnoreCase(diets.getComments().get(i).getId())) {
                            diets.getComments().remove(i);
                            dietDetailsAdapter.notifyItemRemoved(i + 1); // Notify recyclerview deleted at position
                            dietDetailsAdapter.notifyItemChanged(0); // Notify recyclerview header
                            break;
                        }
                    }
                    updateResult();
                } else {
                    Toast.makeText(getApplicationContext(), "Somethingwent wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("tag", "onErrorResponse:" + error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comment_id", comment.getId());
                Log.e("tag", "Params:" + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void likeDiet(Boolean isLike) {
        String URL = APIContants.LIKE_DIET;
        if (!isLike) {
            URL = APIContants.UNLIKE_DIET;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && response.contains("true")) {
                    updateResult();
                } else {
                    Toast.makeText(getApplicationContext(), "Somethingwent wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("tag", "onErrorResponse:" + error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", AppApplication.getInstance().getCurrentUser().getId());
                params.put("post_id", diets.getId());
                Log.e("tag", "Params:" + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void updateResult() {
        Intent intent = new Intent();
        intent.putExtra("diet", diets);
        setResult(RESULT_OK, intent);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}