package com.example.twohand_project;

import android.app.AlertDialog;
import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        binding.gallery.setOnClickListener(new View.OnClickListener() {
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
        binding.locationSpinner.setAdapter(SpinnersAdapters.fakeAdapter(getContext()));
        SpinnersAdapters.setLocationSpinner(null,getContext(),(adapter)->binding.locationSpinner.setAdapter(adapter));

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=binding.emailEt.getText().toString();
                username= binding.UsernameEt.getText().toString();
                number=binding.numberEt.getText().toString();
                password=binding.passwordEt.getText().toString();

                if (validateInput()) {
                    Model.instance().isUsernameTaken(username,(usernameIsTaken)->{
                        if (usernameIsTaken) {
                            makeAToast("Username is Already Taken");
                        }else {
                            Model.instance().isEmailTaken(email,(emailIsTaken)->{
                                if (emailIsTaken) {
                                    makeAToast("Email Is Already Taken");


                                } else {
                                    if (isPhotoSelected){
                                        binding.avatar.setDrawingCacheEnabled(true);
                                        binding.avatar.buildDrawingCache();
                                        Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                                        String id= UUID.randomUUID().toString();
                                        Model.instance().uploadImage(id,bitmap,(url)->{
                                            User newUser=new User(username,email,url,location,number.toString(), new ArrayList<>());
                                            Model.instance().register(newUser,password,(unused)->{
                                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();

                                            });
                                        });
                                    }
                                    else{
                                        User newUser=new User(username,email,"",location,number.toString(), new ArrayList<>());
                                        Model.instance().register(newUser,password,(unused)->{
                                            Intent intent=new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();

                                        });
                                    }

                                }
                            });
                        }

                    });
                }
            }
        });

        return view;
    }
    public boolean isEmailvalid(String email){
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void makeAToast(String text){
        new AlertDialog.Builder(getContext())
                .setTitle("Invalid Input")
                .setMessage(text)
                .setPositiveButton("Ok", (dialog,which)->{
                })
                .create().show();
    }

    public boolean validateInput(){


        if (!isEmailvalid(email)){
            makeAToast("Please enter a valid email");
            return false;
        }
        else if (Objects.equals(username, "")) {
            makeAToast("Please enter an username");
            return false;
        } else if (number.length()!=10 && !number.matches("\\d+") ) {
            makeAToast("Please enter a valid number");
            return false;
        } else if (Objects.equals(location, "Location")) {
            makeAToast("Please choose a location");
            return false;
        } else if (password.length()<6) {
            makeAToast("Password must contains at least 6 characters");
            return false;
        }


        return true;
        }

    }
