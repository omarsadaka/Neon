package com.example.omar.neon.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.omar.neon.Adapter.MyExpandapleListAdapter;
import com.example.omar.neon.Fragments.HydrogenyaFragment;
import com.example.omar.neon.Fragments.MakalatFragment;
import com.example.omar.neon.Fragments.MalafFragment;
import com.example.omar.neon.Fragments.MenawaaFragment;
import com.example.omar.neon.Fragments.MosharkaFragment;
import com.example.omar.neon.Fragments.WhyFragment;
import com.example.omar.neon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThirdHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView openDrawer , search ,back , neon , closeDrawer;
    public EditText searchView;
    private Button goSearch;
    private DrawerLayout drawerLayout;
    private Button menawaa , makalat , mosharka;
    ExpandableListView expandableListView;
    String heading_item [];
    String l1 [];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_home);

        createView();
        createClicks();
        int id =  getIntent().getExtras().getInt("id");
        if (id == 7){
            showClickedItem(menawaa);
            startFragment(new MenawaaFragment());
        }else if (id == 8){
            showClickedItem(makalat);
            startFragment(new MakalatFragment());
        }else if (id == 9){
            showClickedItem(mosharka);
            startFragment(new MosharkaFragment());
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
        menawaa = findViewById(R.id.menawaa);
        makalat = findViewById(R.id.makalat);
        mosharka = findViewById(R.id.mosharka);
        expandableListView = findViewById(R.id.exp_list);
        goSearch = findViewById(R.id.search2);
        closeDrawer = findViewById(R.id.closeDrawer);
    }
    public void createClicks(){
        openDrawer.setOnClickListener(this);
        menawaa.setOnClickListener(this);
        makalat.setOnClickListener(this);
        mosharka.setOnClickListener(this);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        goSearch.setOnClickListener(this);
        closeDrawer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.openDrawer:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menawaa:
                showClickedItem(menawaa);
                searchVisibility();
                startFragment(new MenawaaFragment());
                break;
            case R.id.makalat:
                showClickedItem(makalat);
                searchVisibility();
                startFragment(new MakalatFragment());
                break;
            case R.id.mosharka:
                showClickedItem(mosharka);
                searchVisibility();
                startFragment( new MosharkaFragment());
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
                Intent intent = new Intent(ThirdHomeActivity.this , SearchActivity.class);
                intent.putExtra("searchKay" ,searchView.getText().toString() );
                startActivity(intent);
                break;
            case R.id.closeDrawer:
                drawerLayout.closeDrawer(GravityCompat.START);
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
        menawaa.setBackground(null);
        menawaa.setTextColor(getResources().getColor(R.color.colorBlack));
        makalat.setBackground(null);
        makalat.setTextColor(getResources().getColor(R.color.colorBlack));
        mosharka.setBackground(null);
        mosharka.setTextColor(getResources().getColor(R.color.colorBlack));
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
                    Intent intent = new Intent(ThirdHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(ThirdHomeActivity.this, "zeroo", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 1){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "one", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 2){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "two", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 3){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , HomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "three", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 4){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , SecondHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "four", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 5){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , SecondHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "five", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 6){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(ThirdHomeActivity.this , SecondHomeActivity.class);
                    intent.putExtra("id" , cp);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(SecondHomeActivity.this, "six", Toast.LENGTH_SHORT).show();
                }
                else if (cp == 7){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(menawaa);
                    searchVisibility();
                    startFragment(new MenawaaFragment());

                }
                else if (cp == 8){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(makalat);
                    searchVisibility();
                    startFragment(new MakalatFragment());

                }
                else if (cp == 9){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    showClickedItem(mosharka);
                    searchVisibility();
                    startFragment(new MosharkaFragment());
                }

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ThirdHomeActivity.this.finish();
    }
}
