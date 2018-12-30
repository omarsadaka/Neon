package com.example.omar.neon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omar.neon.Activities.PostDetailsActivity;
import com.example.omar.neon.Models.Posts;
import com.example.omar.neon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar on 12/23/2018.
 */

public class GetPostsAdapter extends RecyclerView.Adapter<GetPostsAdapter.viewHolder> {

    private Context context;
    private List<Posts> postsList;


    public GetPostsAdapter(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;

    }

    @NonNull
    @Override
    public GetPostsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.getposts_item_row , parent , false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull GetPostsAdapter.viewHolder holder, int position) {
        Posts posts = postsList.get(position);
        holder.title.setText(Html.fromHtml(posts.getTitle(), Html.FROM_HTML_MODE_COMPACT));
        holder.content.setText(Html.fromHtml(posts.getContent(), Html.FROM_HTML_MODE_COMPACT));
        Picasso.get().load(posts.getImage()).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView content;

        public viewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.post_title);
            content = itemView.findViewById(R.id.post_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Posts post = postsList.get(getAdapterPosition());
                    Intent intent = new Intent(context , PostDetailsActivity.class);
                    intent.putExtra("data" , post);
                    context.startActivity(intent);
                }
            });
        }
    }
}
