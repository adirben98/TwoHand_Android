package com.example.twohand_project.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.R;
import com.example.twohand_project.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ViewHolder extends RecyclerView.ViewHolder{

    TextView owner;
    ImageView ownerImg;
    TextView location;
    TextView price;
    TextView description;
    ImageView postImg;
    TextView sold;
    View phoneCallBtn;
    ImageButton addToFavoriteBtn;
    String number;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.owner=itemView.findViewById(R.id.Username);
        this.ownerImg=itemView.findViewById(R.id.imgUser);
        this.description=itemView.findViewById(R.id.Description);
        this.location=itemView.findViewById(R.id.Location);
        this.price=itemView.findViewById(R.id.Price);
        this.postImg=itemView.findViewById(R.id.imgPost);
        this.sold=itemView.findViewById(R.id.sold_textview);
        this.phoneCallBtn=itemView.findViewById(R.id.ringing_phone);
        this.addToFavoriteBtn=itemView.findViewById(R.id.likeBtn);
    }

    public void bind(User user,Post post,OnItemClickListener onPhotoClickListener,OnItemClickListener onUsernameClickListener,Context context) {
        this.owner.setText(post.owner);
        if (Objects.equals(post.ownerImg, "")) {
            this.ownerImg.setImageResource(R.drawable.avatar);
        }
        else{
            Picasso.get().load(post.ownerImg).into(this.ownerImg);

        }
        this.description.setText(post.description);
        this.location.setText(post.location);
        this.price.setText(post.price+"$");

        Picasso.get().load(post.postImg).into(this.postImg);
        if(!post.sold){
            this.sold.setVisibility(View.GONE);
        }
        else{
            this.sold.setVisibility(View.VISIBLE);

        }
        this.number=post.number;
        if (user.favorites.contains(post.id)){
            this.addToFavoriteBtn.setImageResource(R.drawable.red_heart);
        }

        this.addToFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> mutableFavorites = new ArrayList<>(user.getFavorites());

                if (!user.favorites.contains(post.id)) {
                    mutableFavorites.add(post.id);
                    user.setFavorites(mutableFavorites);

                    Model.instance().updateFavorites(user);
                    addToFavoriteBtn.setImageResource(R.drawable.red_heart);
                }
                else{
                    mutableFavorites.remove(post.id);
                    user.setFavorites(mutableFavorites);

                    Model.instance().updateFavorites(user);
                    addToFavoriteBtn.setImageResource(R.drawable.heart);
                }


            }
        });
        this.phoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                context.startActivity(intent);
            }
        });
        this.postImg.setOnClickListener((view)->{
            onPhotoClickListener.onClick(getAdapterPosition());
        });
        this.owner.setOnClickListener(view->onUsernameClickListener.onClick(getAdapterPosition()));


    }
}

public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Post> data;
    LayoutInflater inflater;

    OnItemClickListener onPhotoClickListener;
    OnItemClickListener onUsernameClickListener;

    Context context;
    User user;

    public void SetOnPhotoClickListener(OnItemClickListener onPhotoClickListener){
        this.onPhotoClickListener=onPhotoClickListener;
    }
    public void SetOnUsernameClickListener(OnItemClickListener onUsernameClickListener){
        this.onUsernameClickListener=onUsernameClickListener;
    }


    public ListAdapter(User user,List<Post> data, LayoutInflater inflater,Context context) {
        this.user=user;
        this.data = data;
        this.inflater = inflater;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.post_list_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=data.get(position);
        holder.bind(user,post,onPhotoClickListener,onUsernameClickListener,context);

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

    public void setUser(User user) {
        this.user=user;
    }
}
