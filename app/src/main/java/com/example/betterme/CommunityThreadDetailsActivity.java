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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.betterme.model.SetComment;
import com.example.betterme.model.SetCommunityThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommunityThreadDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SetCommunityThread communityThread;
    SwipeRefreshLayout refreshLayout;
    CommunityThreadDetailsAdapter communityThreadDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("communityThread")) {
            communityThread = (SetCommunityThread) getIntent().getSerializableExtra("communityThread");
            getSupportActionBar().setTitle(communityThread.getThreadTitle());
        } else {
            finish();
        }
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        communityThreadDetailsAdapter = new CommunityThreadDetailsAdapter(this, communityThread, new CommunityThreadDetailsAdapter.OnClickListener() {
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
                //TODO API request
            }

            @Override
            public void onClickDeleteComment(SetComment comment) {
                deleteComment(comment);
            }
        });
        recyclerView.setAdapter(communityThreadDetailsAdapter);
        refreshLayout.setEnabled(false);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postComment(String message) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.POST_COMMUNITY_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("id")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SetComment comment = new SetComment(jsonObject);
                        communityThread.getComments().add(comment);
                        communityThreadDetailsAdapter.notifyItemInserted(communityThread.getComments().size()); // Refresh recyclerview in position
                        communityThreadDetailsAdapter.notifyItemChanged(0); // Notify recyclerview header
                        communityThreadDetailsAdapter.editingComment = null;
                        communityThreadDetailsAdapter.notifyItemChanged(communityThreadDetailsAdapter.getItemCount()-1); // Notify recyclerview footer
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
                params.put("comment_user_id", AppApplication.getInstance().getCurrentUser().getId());
                params.put("community_id", communityThread.getCommunityId());
                params.put("comment_content", message);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.EDIT_COMMUNITY_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("success")) {
                    for (int i = 0; i < communityThread.getComments().size(); i++) {
                        if (comment.getId().equalsIgnoreCase(communityThread.getComments().get(i).getId())) {
                            communityThread.getComments().get(i).setComment(message);
                            communityThreadDetailsAdapter.notifyItemChanged(i + 1); // Notify recyclerview update at position
                            communityThreadDetailsAdapter.editingComment = null;
                            communityThreadDetailsAdapter.notifyItemChanged(communityThreadDetailsAdapter.getItemCount()-1); // Notify recyclerview footer
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
//                params.put("comment_user_id", AppApplication.getInstance().getCurrentUser().getId());
//                params.put("community_id", communityThread.getCommunityId());
//                params.put("comment_content", message);
                params.put("comment_id", comment.getId());
                params.put("comment_content", message);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIContants.DELETE_COMMUNITY_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response != null && response.contains("success")) {
                    for (int i = 0; i < communityThread.getComments().size(); i++) {
                        if (comment.getId().equalsIgnoreCase(communityThread.getComments().get(i).getId())) {
                            communityThread.getComments().remove(i);
                            communityThreadDetailsAdapter.notifyItemChanged(0); // Notify recyclerview header
                            communityThreadDetailsAdapter.notifyItemRemoved(i + 1); // Notify recyclerview deleted at position
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

    private void updateResult() {
        Intent intent = new Intent();
        intent.putExtra("communityThread", communityThread);
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