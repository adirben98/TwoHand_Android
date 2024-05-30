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
import android.widget.Toast;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.databinding.FragmentRegisterBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    Boolean isPhotoSelected=false;


    String email;
    String username;
    String number;
    String location="";
    String password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o!= null) {
                    binding.avatar.setImageBitmap(o);
                    isPhotoSelected=true;
                }

            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o!=null){
                    binding.avatar.setImageURI(o);
                    isPhotoSelected=true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRegisterBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraAppLauncher.launch(null);
            }
        });
        binding.galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAppLauncher.launch("image/*");
            }
        });
        binding.locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.locationSpinner.setAdapter(SpinnersAdapters.setLocationSpinner(getContext()));

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=binding.emailEt.getText().toString();
                username= binding.UsernameEt.getText().toString();
                number=binding.numberEt.getText().toString();
                binding.locationSpinner.setAdapter(SpinnersAdapters.setLocationSpinner(getContext()));
                password=binding.passwordEt.getText().toString();

                if (validateInput()) {
                    Model.instance().isEmailTaken(email,(emailIsTaken)->{
                        if (emailIsTaken) {
                            makeAToast("Email Is Already Taken");

                        } else {
                            Model.instance().isUsernameTaken(username,(usernameIsTaken)->{
                                if (usernameIsTaken) {
                                    makeAToast("Username is Already Taken");
                                } else {
                                    binding.avatar.setDrawingCacheEnabled(true);
                                    binding.avatar.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                                    String id= UUID.randomUUID().toString();
                                    Model.instance().uploadImage(id,bitmap,(url)->{
                                        User newUser=new User(username,email,id,location,number,new ArrayList<>(),new ArrayList<>());
                                        Model.instance().register(newUser,password,(unused)->{
                                            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_feedListFragment);

                                        });
                                    });
                                }
                            });
                        }

                    });
                }
            }
        });

        return view;
    }
    public void makeAToast(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

    }

    public boolean validateInput(){

        if (Objects.equals(email, "")) {
            makeAToast("Please enter an email");
            return false;
        }
        else if (Objects.equals(username, "")) {
            makeAToast("Please enter an username");
            return false;
        } else if (Objects.equals(number, "")) {
            makeAToast("Please enter a number");
            return false;
        } else if (Objects.equals(location, "Choose Location")) {
            makeAToast("Please choose a location");
            return false;
        } else if (Objects.equals(password, "")) {
            makeAToast("Please enter a password");
            return false;
        }


        return true;
        }

    }
