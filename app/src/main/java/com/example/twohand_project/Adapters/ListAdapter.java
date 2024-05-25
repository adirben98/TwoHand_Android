package com.example.twohand_project.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twohand_project.Model.Post;
import com.example.twohand_project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class ViewHolder extends RecyclerView.ViewHolder{

    TextView owner;
    ImageView ownerImg;
    TextView location;
    TextView price;
    TextView description;
    ImageView postImg;

    public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
        super(itemView);
        this.owner=itemView.findViewById(R.id.Username);
        this.ownerImg=itemView.findViewById(R.id.imgUser);
        this.description=itemView.findViewById(R.id.Description);
        this.location=itemView.findViewById(R.id.Location);
        this.price=itemView.findViewById(R.id.Price);
        this.postImg=itemView.findViewById(R.id.imgPost);

        itemView.setOnClickListener((view)->{
            listener.onClick(getAdapterPosition());
        });

    }

    public void bind(Post post) {
        this.owner.setText(post.owner);
        Picasso.get().load(post.ownerImg).into(this.ownerImg);
        this.description.setText(post.description);
        this.location.setText(post.location);
        this.price.setText(post.price);
        Picasso.get().load(post.postImg).into(this.postImg);

    }
}

public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Post> data;
    LayoutInflater inflater;

    OnItemClickListener listener;

    public void SetItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    public ListAdapter(List<Post> data, LayoutInflater inflater) {
        this.data = data;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.post_list_row,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=data.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void setData(List<Post> newData) {
        this.data=newData;
        notifyDataSetChanged();
    }
}
