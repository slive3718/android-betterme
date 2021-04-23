package com.example.betterme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.betterme.model.SetDiets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Homepage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Boolean isFirstRun = true;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<SetDiets> diets;
    SwipeRefreshLayout refreshLayout;
    DietsAdapter dietsAdapter;
    AppCompatSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.dietList);
        spinner = findViewById(R.id.spinner);

        diets = new ArrayList<>();
        dietsAdapter = new DietsAdapter(this, diets, new DietsAdapter.OnClickListener() {
            @Override
            public void onClickComment(DietsAdapter.ViewHolder viewHolder) {
                DietActivity(viewHolder.diets);
            }

            @Override
            public void onClickLike(DietsAdapter.ViewHolder viewHolder) {
                Boolean isLike = !viewHolder.getDiets().getLike_status().equalsIgnoreCase("1");
                likeDiet(viewHolder.getDiets(), isLike);
                viewHolder.getDiets().setLike_status(isLike ? "1" : "0");
                if (isLike) {
                    viewHolder.getDiets().setGetLikeCount(viewHolder.getDiets().getGetLikeCount() + 1);
                } else {
                    viewHolder.getDiets().setGetLikeCount(viewHolder.getDiets().getGetLikeCount() - 1);
                }
                dietsAdapter.notifyItemChanged(viewHolder.getLayoutPosition());// notify header to update like icon
            }

            @Override
            public void onClickHolder(DietsAdapter.ViewHolder viewHolder) {
                DietActivity(viewHolder.diets);
            }
        });
        recyclerView.setAdapter(dietsAdapter);
        extractDiets();

        refreshLayout.setOnRefreshListener(this);
        String[] paths = {"View All", "Top"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Homepage.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstRun) {
            isFirstRun = false;
            new Handler().postDelayed(() -> {
                onRefresh();
            }, 500);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuCommunity:
                Community();
                return true;
            case R.id.menuCommunityThread:
                CommunityThread();
                return true;
            case R.id.menuProfile:
                profile();
                return true;
            case R.id.logout:
                Logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("diet")) {
                SetDiets diet = (SetDiets) data.getSerializableExtra("diet");
                if (diet != null) {
                    for (int i = 0; i < diets.size(); i++) {
                        if (diets.get(i).getId().equalsIgnoreCase(diet.getId())) {
                            diets.set(i, diet);
                            dietsAdapter.notifyItemChanged(i);
                        }
                    }
                }
            }
        }
    }

    public void DietActivity(SetDiets diets) {
        Intent intent = new Intent(Homepage.this, DietDetailsActivity.class);
        intent.putExtra("diet", diets);
        startActivityForResult(intent, 1);

    }

    public void profile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void Logout() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logout();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void Community() {
        Intent intent = new Intent(this, CommunityActivity.class);
        startActivity(intent);
    }

    public void Home() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);

    }


    public void CommunityThread() {
        Intent intent = new Intent(this, CommunityThreads.class);
        startActivity(intent);
    }


    private void extractDiets() {
        String url = APIContants.GET_DIET;
        if (spinner.getSelectedItemPosition() == 1) {
            url = APIContants.GET_TOP_DIET;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                refreshLayout.setRefreshing(false);
                diets.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject dietObject = response.getJSONObject(i);
                        diets.add(new SetDiets(dietObject));
                        Log.d("response1=", response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.d("response1=", response.toString());
                    }
                }
                dietsAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("tag", "onErrorResponse:" + error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void likeDiet(SetDiets diet, Boolean isLike) {
        String URL = APIContants.LIKE_DIET;
        if (!isLike) {
            URL = APIContants.UNLIKE_DIET;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && response.contains("true")) {

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
                params.put("post_id", diet.getId());
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


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        extractDiets();
    }
}