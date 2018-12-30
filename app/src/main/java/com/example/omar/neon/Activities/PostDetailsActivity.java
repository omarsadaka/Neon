package com.example.omar.neon.Activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omar.neon.Models.Posts;
import com.example.omar.neon.R;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {
    private ImageView back , poster;
    private TextView title , content;
    private Posts posts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        createView();
        posts = (Posts) getIntent().getSerializableExtra("data");
        Picasso.get().load(posts.getImage()).into(poster);
        title.setText(Html.fromHtml(posts.getTitle(), Html.FROM_HTML_MODE_COMPACT));
        content.setText(Html.fromHtml(posts.getContent(), Html.FROM_HTML_MODE_COMPACT));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void createView(){
        back = findViewById(R.id.back);
        poster = findViewById(R.id.post_detail_img);
        title = findViewById(R.id.post_detail_title);
        content = findViewById(R.id.post_detail_content);

    }
}
