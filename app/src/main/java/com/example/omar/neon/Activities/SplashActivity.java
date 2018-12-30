package com.example.omar.neon.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.omar.neon.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);

//        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
//            Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
//        }else {
//
//        }


        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    progressBar.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (AccessToken.getCurrentAccessToken() != null){
                        startActivity(new Intent(SplashActivity.this , HomeActivity.class));
                        SplashActivity.this.finish();
                    }else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
            }
        });
        timer.start();
    }


}
