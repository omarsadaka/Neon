package com.example.omar.neon.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.neon.Adapter.MyExpandapleListAdapter;
import com.example.omar.neon.Data.ImageConverter;
import com.example.omar.neon.Fragments.HagsBaladyFragment;
import com.example.omar.neon.Fragments.HayYorzakFragment;
import com.example.omar.neon.Fragments.HydrogenyaFragment;
import com.example.omar.neon.Fragments.MakalatFragment;
import com.example.omar.neon.Fragments.MalafFragment;
import com.example.omar.neon.Fragments.MenawaaFragment;
import com.example.omar.neon.Fragments.MosharkaFragment;
import com.example.omar.neon.Fragments.NashraFragment;
import com.example.omar.neon.Fragments.NwarhaFragment;
import com.example.omar.neon.Fragments.WhyFragment;
import com.example.omar.neon.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SecondHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView openDrawer , search ,back , neon , closeDrawer;
    private Button goSearch;
    public EditText searchView;
    private ImageView userImage;
    private TextView userName;
    private LinearLayout signOut , home , aboutUs;
    private DrawerLayout drawerLayout;
    private Button hayYorzak , malaf , hydrogyna;
    ExpandableListView expandableListView;
    String heading_item [];
    String l1 [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_home);
        createView();
        createClicks();
        getFbData();
        int id =  getIntent().getExtras().getInt("id");
        if (id == 4){
            showClickedItem(hayYorzak);
            startFragment(new HayYorzakFragment());
        }else if (id == 5){
            showClickedItem(malaf);
            startFragment(new MalafFragment());
        }else if (id == 6){
            showClickedItem(hydrogyna);
            startFragment(new HydrogenyaFragment());
        }


         expandableList();


    }
    public void createView(){
        openDrawer = findViewById(R.id.openDrawer);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
        neon = findViewById(R.id.neon);
        searchView = findViewById(R.id.search_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        hayYorzak = findViewById(R.id.hay_yorzak);
        malaf = findViewById(R.id.malaf);
        hydrogyna = findViewById(R.id.hydroginya);
        expandableListView = findViewById(R.id.exp_list);
        goSearch = findViewById(R.id.search2);
        closeDrawer = findViewById(R.id.closeDrawer);
        signOut = findViewById(R.id.sign_out);
        home = findViewById(R.id.home_drawer);
        aboutUs = findViewById(R.id.aboutUs_drawer);
        userImage = findViewById(R.id.imageDrawer);
        userName = findViewById(R.id.userName);
    }
    public void createClicks(){
        openDrawer.setOnClickListener(this);
        hayYorzak.setOnClickListener(this);
        malaf.setOnClickListener(this);
        hydrogyna.setOnClickListener(this);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        goSearch.setOnClickListener(this);
        closeDrawer.setOnClickListener(this);
        signOut.setOnClickListener(this);
        home.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

                case R.id.openDrawer:
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.hay_yorzak:
                    showClickedItem(hayYorzak);
                    searchVisibility();
                    startFragment(new WhyFragment());
                    break;
                case R.id.malaf:
                    showClickedItem(malaf);
                    searchVisibility();
                    startFragment(new MalafFragment());
                    break;
                case R.id.hydroginya:
                    showClickedItem(hydrogyna);
                    searchVisibility();
                    startFragment( new HydrogenyaFragment());
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
                Intent intent = new Intent(SecondHomeActivity.this , SearchActivity.class);
                intent.putExtra("searchKay" ,searchView.getText().toString() );
                startActivity(intent);
                break;
            case R.id.sign_out:
                LoginManager.getInstance().logOut();
                Toast.makeText(this, "LogOut", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(SecondHomeActivity.this , MainActivity.class));
                SecondHomeActivity.this.finish();
                break;
            case R.id.closeDrawer:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.home_drawer:
               startActivity(new Intent(SecondHomeActivity.this , HomeActivity.class));
               SecondHomeActivity.this.finish();
                break;
            case R.id.aboutUs_drawer:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(SecondHomeActivity.this , AboutActivity.class));
                break;
        }

    }

    private void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    private void showClickedItem(Button clickedItem) {
        hayYorzak.setBackground(null);
        hayYorzak.setTextColor(getResources().getColor(R.color.colorBlack));
        malaf.setBackground(null);
        malaf.setTextColor(getResources().getColor(R.color.colorBlack));
        hydrogyna.setBackground(null);
        hydrogyna.setTextColor(getResources().getColor(R.color.colorBlack));
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
        HashMap<String,List<String>> childList = new HashMap<String,List<String>>();
        heading_item  = getResources().getStringArray(R.array.Array_tittle);
        l1  = getResources().getStringArray(R.array.Home_item);

        for (String title:heading_item){
            Heading.add(title);
        }
        for (String title:l1){
            L1.add(title);
        }

        childList.put(Heading.get(0),L1);
        final MyExpandapleListAdapter myAdapter =new MyExpandapleListAdapter(Heading,childList,this);
        expandableListView.setAdapter( myAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int cp = (int) MyExpandapleListAdapter.getposition(groupPosition, childPosition);
                if (cp == 0){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    SecondHomeActivity.this.finish();
                    //Toast.makeText(SecondHomeActivity.this, "zeroo", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 1){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    SecondHomeActivity.this.finish();
                    //Toast.makeText(SecondHomeActivity.this, "one", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 2){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    SecondHomeActivity.this.finish();
                    //Toast.makeText(SecondHomeActivity.this, "two", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 3){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    SecondHomeActivity.this.finish();
                    //Toast.makeText(SecondHomeActivity.this, "three", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 4){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(hayYorzak);
                    searchVisibility();
                    startFragment(new WhyFragment());

                    //Toast.makeText(SecondHomeActivity.this, "four", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 5){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(malaf);
                    searchVisibility();
                    startFragment(new MalafFragment());
                    //Toast.makeText(SecondHomeActivity.this, "five", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 6){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(hydrogyna);
                    searchVisibility();
                    startFragment(new HydrogenyaFragment());
                    //Toast.makeText(SecondHomeActivity.this, "six", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 7){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , ThirdHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    //Toast.makeText(SecondHomeActivity.this, "seven", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 8){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , ThirdHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    //Toast.makeText(SecondHomeActivity.this, "eight", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 9){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SecondHomeActivity.this , ThirdHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    //Toast.makeText(SecondHomeActivity.this, "nine", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SecondHomeActivity.this.finish();
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
}
