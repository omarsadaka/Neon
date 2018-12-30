package com.example.omar.neon.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.omar.neon.Activities.HomeActivity;
import com.example.omar.neon.Adapter.GetPostsAdapter;
import com.example.omar.neon.Models.Posts;
import com.example.omar.neon.R;
import com.example.omar.neon.Utils.EndlessRecyclerViewScrollListener;
import com.example.omar.neon.Utils.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NwarhaFragment extends Fragment {
    private RecyclerView recyclerView;
    private GetPostsAdapter getPostsAdapter;
    private List<Posts> postsList;
    private static final String URL = "https://neonsci.com/api/get_category_posts/?id=23&count=5&page=";
    private ProgressBar progressBar , loadingProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_nwarha , container ,false);
        recyclerView = view.findViewById(R.id.nwarha_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = view.findViewById(R.id.post_progressBar);
        loadingProgress = view.findViewById(R.id.nwarha_loadindPB);
        postsList = new ArrayList<>();
        getPosts(1);
        getPostsAdapter = new GetPostsAdapter(getContext() , postsList);
        recyclerView.setAdapter(getPostsAdapter);
        getPostsAdapter.notifyDataSetChanged();

        EndlessRecyclerViewScrollListener mixedScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                getPosts(page);
                loadingProgress.setVisibility(View.VISIBLE);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(mixedScrollListener);
        return view;
    }

    public void getPosts(int currentPage){

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL+currentPage, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response" , String.valueOf(response));
                try {
                    JSONArray jsonArray = response.getJSONArray("posts");
                    for (int i =0;i<jsonArray.length() ;i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Posts posts = new Posts();
                        posts.setTitle(jsonObject.getString("title"));
                        posts.setContent(jsonObject.getString("content"));
                        posts.setImage(jsonObject.getString("thumbnail"));
                        postsList.add(posts);
                        getPostsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        loadingProgress.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error" , String.valueOf(error));

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
        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(objectRequest);
    }
}
