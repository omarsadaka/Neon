package com.example.omar.neon.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.omar.neon.Adapter.MyExpandapleListAdapter;
import com.example.omar.neon.Adapter.RecyclerviewAdapter;
import com.example.omar.neon.Data.ImageConverter;
import com.example.omar.neon.Fragments.HagsBaladyFragment;
import com.example.omar.neon.Fragments.NashraFragment;
import com.example.omar.neon.Fragments.NwarhaFragment;
import com.example.omar.neon.Fragments.WhyFragment;
import com.example.omar.neon.Models.Posts;
import com.example.omar.neon.R;
import com.example.omar.neon.Utils.RequestQueueSingleton;
import com.example.omar.neon.databinding.ActivityHomeBinding;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView openDrawer, search , back, neon , closeDrawer;
    private LinearLayout signOut , home , aboutUs;
    private ImageView userImage;
    private TextView userName;
    private TextView postTitle;
     public EditText searchView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter adapter;
    private List<Posts> articleList;
    private List<String> strings;
    private Button why, nwarha, nashra, hagesBaldy;
    private Button goSearch;
    private ShimmerLayout shimmerLayout;
    private static final String URL = "https://neonsci.com/api/get_recent_posts/";
    private static final String postURL = "https://neonsci.com/api/user/fb_connect/?access_token=";
    ExpandableListView expandableListView;
    String heading_item[];
    String l1[];
    private int id;
    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
//        mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_home);

        createViews();
        createClicks();
        getFbData();
        showClickedItem(why);
        try {

            id = getIntent().getExtras().getInt("id");
            if (id == 0) {
                showClickedItem(nashra);
                startFragment(new NashraFragment());
            } else if (id == 1) {
                showClickedItem(nwarha);
                startFragment(new NwarhaFragment());
            } else if (id == 2) {
                showClickedItem(why);
                startFragment(new WhyFragment());
            } else if (id == 3) {
                showClickedItem(hagesBaldy);
                startFragment(new HagsBaladyFragment());
            }

        } catch (Exception ignored) {

        }

        startFragment(new WhyFragment());

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridlm = new GridLayoutManager(this, 1);
        gridlm.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridlm);
        articleList = new ArrayList<>();
        strings = new ArrayList<>();
        getPosts();
        adapter = new RecyclerviewAdapter(this, articleList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        expandableList();

    }

    public void createViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        openDrawer = findViewById(R.id.openDrawer);
        recyclerView = findViewById(R.id.recyclerview);
        why = findViewById(R.id.why);
        nwarha = findViewById(R.id.nwarha);
        nashra = findViewById(R.id.nashra);
        postTitle = findViewById(R.id.post_title);
        search = findViewById(R.id.search);
        searchView = findViewById(R.id.search_view);
        back = findViewById(R.id.back);
        neon = findViewById(R.id.neon);
        expandableListView = findViewById(R.id.exp_list);
        hagesBaldy = findViewById(R.id.hags_balady);
        goSearch = findViewById(R.id.search2);
        userImage = findViewById(R.id.imageDrawer);
        userName = findViewById(R.id.userName);
        signOut = findViewById(R.id.sign_out);
        shimmerLayout = findViewById(R.id.shimmer_horizontal);
        closeDrawer = findViewById(R.id.closeDrawer);
        home = findViewById(R.id.home_drawer);
        aboutUs = findViewById(R.id.aboutUs_drawer);


    }

    public void createClicks() {
        openDrawer.setOnClickListener(this);
        why.setOnClickListener(this);
        nwarha.setOnClickListener(this);
        nashra.setOnClickListener(this);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        hagesBaldy.setOnClickListener(this);
        goSearch.setOnClickListener(this);
        signOut.setOnClickListener(this);
        closeDrawer.setOnClickListener(this);
        home.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
    }

    public void getPosts() {


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    JSONArray jsonArray = response.getJSONArray("posts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Posts posts = new Posts();
                        posts.setTitle(jsonObject.getString("title"));
                        posts.setContent(jsonObject.getString("content"));
                        posts.setImage(jsonObject.getString("thumbnail"));
                        articleList.add(posts);
                        adapter.notifyDataSetChanged();
                        shimmerLayout.stopShimmerAnimation();
                        shimmerLayout.setVisibility(View.GONE);
                        postTitle.setVisibility(View.VISIBLE);
                        strings.add(jsonObject.getString("title"));
                    }

                    title = strings.get(new Random().nextInt(strings.size()));
                    postTitle.setText(title);
                    Log.e("list" , String.valueOf(strings.size()));
                    Log.e("list" , String.valueOf(strings));
                    for ( int j =0 ;j<=strings.size()-1 ;j++) {
                        final int finalJ = j;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                postTitle.setText(strings.get(finalJ));
                            }
                        }, 2000);

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
        RequestQueueSingleton.getInstance(HomeActivity.this).addToRequestQueue(objectRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.openDrawer:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.why:
                showClickedItem(why);
                searchVisibility();
                startFragment(new WhyFragment());
                break;
            case R.id.nwarha:
                showClickedItem(nwarha);
                searchVisibility();
                startFragment(new NwarhaFragment());
                break;
            case R.id.nashra:
                showClickedItem(nashra);
                searchVisibility();
                startFragment(new NashraFragment());
                break;
            case R.id.hags_balady:
                showClickedItem(hagesBaldy);
                searchVisibility();
                startFragment(new HagsBaladyFragment());
                break;
            case R.id.search:
                openDrawer.setVisibility(View.GONE);
                neon.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                goSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.back:
                searchVisibility();
                break;
            case R.id.search2:
               Intent intent = new Intent(HomeActivity.this , SearchActivity.class);
               intent.putExtra("searchKay" ,searchView.getText().toString() );
               startActivity(intent);
                break;
            case R.id.sign_out:
                LoginManager.getInstance().logOut();
                Toast.makeText(this, "LogOut", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this , MainActivity.class));
                HomeActivity.this.finish();
                break;
            case R.id.closeDrawer:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.home_drawer:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.aboutUs_drawer:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this , AboutActivity.class));
                break;

        }
    }

    private void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    private void showClickedItem(Button clickedItem) {
        why.setBackground(null);
        why.setTextColor(getResources().getColor(R.color.colorBlack));
        nwarha.setBackground(null);
        nwarha.setTextColor(getResources().getColor(R.color.colorBlack));
        nashra.setBackground(null);
        nashra.setTextColor(getResources().getColor(R.color.colorBlack));
        hagesBaldy.setBackground(null);
        hagesBaldy.setTextColor(getResources().getColor(R.color.colorBlack));
        clickedItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        clickedItem.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void searchVisibility() {
        searchView.setVisibility(View.GONE);
        searchView.setText("");
        back.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        neon.setVisibility(View.VISIBLE);
        openDrawer.setVisibility(View.VISIBLE);
        goSearch.setVisibility(View.GONE);
    }
    public void expandableList(){

        List<String> Heading = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        HashMap<String, List<String>> childList = new HashMap<String, List<String>>();
        heading_item = getResources().getStringArray(R.array.Array_tittle);
        l1 = getResources().getStringArray(R.array.Home_item);

        for (String title : heading_item) {
            Heading.add(title);
        }
        for (String title : l1) {
            L1.add(title);
        }

        childList.put(Heading.get(0), L1);
        final MyExpandapleListAdapter myAdapter = new MyExpandapleListAdapter(Heading, childList, this);
        expandableListView.setAdapter(myAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int cp = (int) MyExpandapleListAdapter.getposition(groupPosition, childPosition);
                if (cp == 0) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(nashra);
                    searchVisibility();
                    startFragment(new NashraFragment());

                    // Toast.makeText(HomeActivity.this, "zeroo", Toast.LENGTH_SHORT).show();
                } else if (cp == 1) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(nwarha);
                    searchVisibility();
                    startFragment(new NwarhaFragment());

                    // Toast.makeText(HomeActivity.this, "one", Toast.LENGTH_SHORT).show();
                } else if (cp == 2) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(why);
                    searchVisibility();
                    startFragment(new WhyFragment());

                    //Toast.makeText(HomeActivity.this, "two", Toast.LENGTH_SHORT).show();
                } else if (cp == 3) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(hagesBaldy);
                    searchVisibility();
                    startFragment(new HagsBaladyFragment());

                    //Toast.makeText(HomeActivity.this, "three", Toast.LENGTH_SHORT).show();
                } else if (cp == 4) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, SecondHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    //Toast.makeText(HomeActivity.this, "four", Toast.LENGTH_SHORT).show();
                } else if (cp == 5) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, SecondHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    // Toast.makeText(HomeActivity.this, "five", Toast.LENGTH_SHORT).show();
                } else if (cp == 6) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, SecondHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    //Toast.makeText(HomeActivity.this, "six", Toast.LENGTH_SHORT).show();
                } else if (cp == 7) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, ThirdHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    //Toast.makeText(HomeActivity.this, "seven", Toast.LENGTH_SHORT).show();
                } else if (cp == 8) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, ThirdHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    //Toast.makeText(HomeActivity.this, "eight", Toast.LENGTH_SHORT).show();
                } else if (cp == 9) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(HomeActivity.this, ThirdHomeActivity.class);
                    intent.putExtra("id", cp);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    //Toast.makeText(HomeActivity.this, "nine", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }

    public void getFbData() {
        if (AccessToken.getCurrentAccessToken ( ) != null) {
            GraphRequest request = GraphRequest.newMeRequest (
                    AccessToken.getCurrentAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                String first_name = object.getString ( "first_name" );
                                String last_name = object.getString ( "last_name" );
                                String id = object.getString ( "id" );
                                String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                                Picasso.get ( ).load ( image_url ).transform ( new ImageConverter( ) ).into ( userImage );
                                userName.setText ( first_name + last_name );
                                Log.d ( "user ", first_name + last_name );
                                Log.d ( "image  ", image_url );


                                //todo check if there is an email
                                if (object.has ( "email" )) {
                                    String email = object.getString ( "email" );
                                    Log.d ( "object.toString()", email );
                                    //use UserData class with that email
                                } else {
                                    // create a dialog to get valid mail!!
                                }

                            } catch (JSONException e) {
                                e.printStackTrace ( );
                            }

                        }
                    } );

            Bundle parameters = new Bundle ( );
            parameters.putString ( "fields", "first_name,last_name,email,id" );
            request.setParameters ( parameters );
            request.executeAsync ( );
        }else {
            userName.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        shimmerLayout.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        shimmerLayout.stopShimmerAnimation ();
        super.onPause();
    }

    public void postData(AccessToken accessToken){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, postURL+accessToken, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("postResponse", String.valueOf(response));


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("postError", String.valueOf(error));

            }
        });
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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
        RequestQueueSingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
