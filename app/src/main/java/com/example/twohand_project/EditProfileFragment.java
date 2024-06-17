package com.example.twohand_project;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.databinding.FragmentEditProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class EditProfileFragment extends Fragment {

    FragmentEditProfileBinding binding;
    String location;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    boolean photoSelected =false;
    UserViewModel userViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);

        cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    binding.avatar.setImageBitmap(o);
                    photoSelected =true;
                }
            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null) {
                    binding.avatar.setImageURI(o);
                    photoSelected =true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentEditProfileBinding.inflate(inflater,container,false);
        View v=binding.getRoot();

        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraAppLauncher.launch(null);
            }
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAppLauncher.launch("image/*");
            }
        });

         userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{

            binding.usernameTv.setText(user.username);
             if(!user.userImg.equals(""))
                 Picasso.get().load(user.userImg).into(binding.avatar);
             else{
                 binding.avatar.setImageResource(R.drawable.avatar);
             }
            binding.numberEt.setText(user.number);
            SpinnersAdapters.setLocationSpinner(user.location,getContext(),adapter->binding.locationSpinner.setAdapter(adapter));
            binding.locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    location=parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            binding.EditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number=binding.numberEt.getText().toString();

                    if (number.matches("\\d+")) {
                        if (photoSelected) {
                            binding.avatar.setDrawingCacheEnabled(true);
                            binding.avatar.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                            String id = UUID.randomUUID().toString();
                            Model.instance().uploadImage(id, bitmap, (url) -> {
                                User updatedUser = new User(user.username, user.email, url, location, binding.numberEt.getText().toString(), user.favorites);
                                updateUserAndHisPosts(v, updatedUser);
                            });


                        } else {
                            User updatedUser = new User(user.username, user.email, user.userImg, location, number, user.favorites);
                            updateUserAndHisPosts(v, updatedUser);

                        }
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Invalid Input")
                                .setMessage("Please enter a valid number")
                                .setPositiveButton("Ok", (dialog,which)->{
                                })
                                .create().show();
                    }

                }
            });

        });





        return v;
    }
    public void updateUserAndHisPosts(View v,User updatedUser){
        Model.instance().updateUserAndHisPosts(updatedUser, unused ->{
                    Navigation.findNavController(v).popBackStack();
                }
        );
    }
}