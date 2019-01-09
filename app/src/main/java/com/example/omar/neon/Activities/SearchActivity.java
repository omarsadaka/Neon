package com.example.omar.neon.Activities;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.omar.neon.Adapter.GetPostsAdapter;
import com.example.omar.neon.Models.Posts;
import com.example.omar.neon.R;
import com.example.omar.neon.Utils.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ImageView back;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private GetPostsAdapter adapter;
    private List<Posts> postsList;
    private String searchWord;
    private TextView result;
    private static final String URL = "https://neonsci.com/api/get_search_results/?search=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        result = findViewById(R.id.result);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.searchProgress);
        searchWord = getIntent().getExtras().getString("searchKay");
        if (searchWord.isEmpty()){
            result.setText(R.string.result);
            progressBar.setVisibility(View.GONE);
        }
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        postsList = new ArrayList<>();
        Log.e("word" , searchWord);
        getPosts();
        adapter = new GetPostsAdapter(this , postsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                SearchActivity.this.finish();
            }
        });

    }

    public void getPosts() {


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL+searchWord, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));

                try {
                    if (response.getString("count").equals("0")){
                        result.setText(R.string.result);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        JSONArray jsonArray = response.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Posts posts = new Posts();
                            posts.setTitle(jsonObject.getString("title"));
                            posts.setContent(jsonObject.getString("content"));
                            posts.setImage(jsonObject.getString("thumbnail"));
                            postsList.add(posts);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", String.valueOf(error));

            }
        });
        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(SearchActivity.this).addToRequestQueue(objectRequest);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
