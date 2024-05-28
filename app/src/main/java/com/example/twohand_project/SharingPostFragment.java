package com.example.twohand_project;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentSharingPostBinding;

import java.util.Objects;
import java.util.UUID;


public class SharingPostFragment extends Fragment {
    String clothKind;
    String color;
    String description;
    String price;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    FragmentSharingPostBinding binding;
    Boolean isPhotoSelected=false;
    public SharingPostFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o!= null) {
                    binding.postImage.setImageBitmap(o);
                    isPhotoSelected=true;
                }

            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o!=null){
                    binding.postImage.setImageURI(o);
                    isPhotoSelected=true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSharingPostBinding.inflate(inflater,container,false);
        View view=binding.getRoot();



        ImageButton cameraBtn=binding.cameraBtn;
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraAppLauncher.launch(null);

            }
        });

        ImageButton galleryBtn=binding.galleryBtn;
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAppLauncher.launch("image/*");
            }
        });
        Spinner clothKindSpinner=binding.clothKind;
        clothKindSpinner.setAdapter(SpinnersAdapters.setClothKindsSpinner(getContext()));
        clothKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clothKind=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner colorSpinner=binding.colorSpinner;
        colorSpinner.setAdapter(SpinnersAdapters.setColorsSpinner(getContext()));
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button shareBtn=binding.shareBtn;
        shareBtn.setOnClickListener(v -> {
            price=binding.priceEt.getText().toString();
            if (Objects.equals(price, "")) {
                price="---";
            }
            description=binding.descriptionEt.getText().toString();
            if (validateInput()){
                binding.postImage.setDrawingCacheEnabled(true);
                binding.postImage.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.postImage.getDrawable()).getBitmap();
                String id= UUID.randomUUID().toString();
                Model.instance().getLoggedUser((user)->{
                    Post post=new Post(id, user.username,user.userImg,user.location,clothKind,color,price,description, user.number, "",false);
                    Model.instance().uploadImage(id, bitmap, url->{
                        if (url != null){
                            post.setImageUrl(url);
                        }
                        Model.instance().addPost(post,(unused)->{
                            Navigation.findNavController(v).navigate(R.id.action_sharingPostFragment_to_feedListFragment);
                        });
                    });
                });



            }
        });
        return view;
    }
    public void makeAToast(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

    }

    public boolean validateInput(){
       if (Objects.equals(description, "")) {
            makeAToast("Please write a description");
            return false;
        }

        else if (!isPhotoSelected){
            makeAToast("Please choose a photo");
            return false;
        }

        return true;
    }
}