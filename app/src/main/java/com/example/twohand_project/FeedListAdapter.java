package com.example.twohand_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twohand_project.Model.Post;

import java.util.List;

class FeedViewHolder extends RecyclerView.ViewHolder{

    TextView owner;
    ImageView ownerImg;
    TextView location;
    TextView price;
    TextView description;
    ImageView productImg;

    public FeedViewHolder(@NonNull View itemView,OnItemClickListener listener) {
        super(itemView);
        this.owner=itemView.findViewById(R.id.Username);
        this.ownerImg=itemView.findViewById(R.id.imgUser);
        this.description=itemView.findViewById(R.id.Description);
        this.location=itemView.findViewById(R.id.Location);
        this.price=itemView.findViewById(R.id.Price);
        this.productImg=itemView.findViewById(R.id.imgPost);

        itemView.setOnClickListener((view)->{
            listener.onClick(getAdapterPosition());
        });

    }

    public void bind(Post post) {
        this.owner.setText(post.owner);
        //this.ownerImg.
        this.description.setText(post.description);
        this.location.setText(post.location);
        this.price.setText(post.price);
        //this.productImg=itemView.findViewById(R.id.imgPost);
    }
}

interface OnItemClickListener{
    void onClick(int pos);
}

public class FeedListAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    List<Post> data;
    LayoutInflater inflater;

    OnItemClickListener listener;

    void SetItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    public FeedListAdapter(List<Post> data, LayoutInflater inflater) {
        this.data = data;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.post_list_row,parent,false);
        return new FeedViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Post post=data.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
