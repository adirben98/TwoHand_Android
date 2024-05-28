package com.example.twohand_project.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    TextView sold;
    View phoneCallBtn;
    String number;

    public ViewHolder(@NonNull View itemView,OnItemClickListener listener,Context context) {
        super(itemView);
        this.owner=itemView.findViewById(R.id.Username);
        this.ownerImg=itemView.findViewById(R.id.imgUser);
        this.description=itemView.findViewById(R.id.Description);
        this.location=itemView.findViewById(R.id.Location);
        this.price=itemView.findViewById(R.id.Price);
        this.postImg=itemView.findViewById(R.id.imgPost);
        this.sold=itemView.findViewById(R.id.sold_textview);
        this.phoneCallBtn=itemView.findViewById(R.id.ringing_phone);

        this.phoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                context.startActivity(intent);
            }
        });
        this.ownerImg.setOnClickListener((view)->{
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
        if(!post.sold){
            this.sold.setVisibility(View.GONE);
        }
        this.number=post.number;

    }
}

public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Post> data;
    LayoutInflater inflater;

    OnItemClickListener listener;
    Context context;

    public void SetItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    public ListAdapter(List<Post> data, LayoutInflater inflater,Context context) {
        this.data = data;
        this.inflater = inflater;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.post_list_row,parent,false);
        return new ViewHolder(view,listener,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=data.get(position);
        holder.bind(post);

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
