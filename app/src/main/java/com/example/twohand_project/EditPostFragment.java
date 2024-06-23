package com.example.twohand_project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentEditPostBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;


public class EditPostFragment extends Fragment {

    FragmentEditPostBinding binding;
    String sold;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    PhotoViewModel viewModel;
    boolean isPhotoSelected;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    viewModel.setBitmap(o);
                    binding.postImg.setImageBitmap(viewModel.getBitmap());
                    isPhotoSelected = true;
                }
            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null) {
                    viewModel.setUrl(o);
                    binding.postImg.setImageURI(viewModel.getUrl());
                    isPhotoSelected = true;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentEditPostBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        viewModel=new ViewModelProvider(this).get(PhotoViewModel.class);
        if (viewModel.getBitmap()!=null)
            binding.postImg.setImageBitmap(viewModel.getBitmap());
        if (viewModel.getUrl()!=null)
            binding.postImg.setImageURI(viewModel.getUrl());

        binding.soldSpinner.setAdapter(SpinnersAdapters.setSoldSpinner(getContext()));
        binding.soldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sold=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


        String id=EditPostFragmentArgs.fromBundle(getArguments()).getId();
        Model.instance().getPostById(id).observe(getViewLifecycleOwner(),(post)->{
            Picasso.get().load(Uri.parse(post.postImg)).into(binding.postImg);
            binding.descriptionEt.setText(post.description);
            binding.priceEt.setText(post.price);
            if (!post.sold)
                binding.soldSpinner.setSelection(1);


            binding.saveBtn.setOnClickListener(v -> {

                post.price=binding.priceEt.getText().toString();
                if (post.price.equals("---")) {
                    post.price="0";
                }
                if (post.price.matches("\\d+")) {
                    if (Objects.equals(post.price, "0"))
                        post.price = "---";
                    post.description = binding.descriptionEt.getText().toString();
                    post.sold = Objects.equals("Sold", sold);
                    if (isPhotoSelected) {
                        binding.postImg.setDrawingCacheEnabled(true);
                        binding.postImg.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) binding.postImg.getDrawable()).getBitmap();
                        Model.instance().uploadImage(id, bitmap, (url) -> {
                            post.setPostImg(url);
                            Model.instance().updatePost(post, (unused) -> {
                                Navigation.findNavController(v).popBackStack(R.id.postFragment, false);
                            });
                        });

                    } else {
                        Model.instance().updatePost(post, (unused) -> {
                            Navigation.findNavController(v).popBackStack(R.id.postFragment, false);
                        });
                    }

                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Invalid Input")
                            .setMessage("Please enter a valid numeric price")
                            .setPositiveButton("Ok", (dialog,which)->{
                            })
                            .create().show();
                }


            });

        });



        return view;
    }
}