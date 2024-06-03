package com.example.twohand_project.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class ProfileViewHolder extends RecyclerView.ViewHolder{
    ImageView postImg;
    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        this.postImg=itemView.findViewById(R.id.gridImage);
    }

    public void bind(Post post, OnItemClickListener listener) {
        Picasso.get().load(post.postImg).into(postImg);
        postImg.setOnClickListener(v->listener.onClick(getAdapterPosition()));
    }
}

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    List<Post> data;
    LayoutInflater inflater;

    OnItemClickListener listener;
    Context context;
    User user;

    public void SetItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    public ProfileListAdapter(List<Post> data, LayoutInflater inflater) {
        this.data = data;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.profile_post_list_row,parent,false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Post post=data.get(position);
        holder.bind(post,listener);

    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }



    public void setData(List<Post> newData) {
        this.data=newData;
        notifyDataSetChanged();
    }
}

