package com.example.betterme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.betterme.model.SetCommunityThread;
import com.example.betterme.model.SetDiets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommunityThreads extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_DETAILS = 1;
    private static final int REQUEST_COMMUNITY = 2;
    Boolean isFirstRun = true;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<SetCommunityThread> listItem = new ArrayList<>();
    SwipeRefreshLayout refreshLayout;
    CommunityThreadsAdapter threadsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_threads);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Community Threads");
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.dietList);

        threadsAdapter = new CommunityThreadsAdapter(this, listItem, new CommunityThreadsAdapter.OnClickListener() {
            @Override
            public void onClickItem(SetCommunityThread communityThread) {
                communityThreadDetails(communityThread);
            }
        });
        recyclerView.setAdapter(threadsAdapter);
        refreshLayout.setOnRefreshListener(this);
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
        inflater.inflate(R.menu.menu_community_thread, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCommunity:
                Community();
                return true;
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_DETAILS == requestCode && data.hasExtra("communityThread")) {
                SetCommunityThread thread = (SetCommunityThread) data.getSerializableExtra("communityThread");
                if(thread != null) {
                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.get(i).getCommunityId().equalsIgnoreCase(thread.getCommunityId())) {
                            listItem.set(i, thread);
                            threadsAdapter.notifyItemChanged(i);
                        }
                    }
                }
            }else if(REQUEST_COMMUNITY == requestCode){
                onRefresh();
            }
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }

    public void communityThreadDetails(SetCommunityThread communityThread) {
        Intent intent = new Intent(CommunityThreads.this, CommunityThreadDetailsActivity.class);
        intent.putExtra("communityThread", communityThread);
        startActivityForResult(intent,REQUEST_DETAILS);

    }

    public void Community() {
        Intent intent = new Intent(this, CommunityActivity.class);
        startActivityForResult(intent,REQUEST_COMMUNITY);
    }

    public void Home() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);

    }


    public void CommunityThread() {
        Intent intent = new Intent(this, CommunityThreads.class);
        startActivity(intent);

    }

    public void fetchCommunityThread() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, APIContants.GET_COMMUNITY_THREAD, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response1=", response.toString());
                listItem.clear();
                refreshLayout.setRefreshing(false);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject dietObject = response.getJSONObject(i);
                        String path = "http://192.168.0.12/";
                        listItem.add(new SetCommunityThread(dietObject));
                        Log.d("response1=", response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("response1=", response.toString());
                    }
                }
                threadsAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
                Log.d("tag", "onErrorResponse:" + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        fetchCommunityThread();
    }
}