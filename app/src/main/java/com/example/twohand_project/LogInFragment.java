package com.example.twohand_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.databinding.FragmentLogInBinding;


public class LogInFragment extends Fragment {
FragmentLogInBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLogInBinding.inflate(inflater,container,false);
        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=binding.Username.getText().toString();
                String password=binding.PasswordEt.getText().toString();
                Model.instance().logIn(username,password,(isSuccessful)->{
                    if (isSuccessful) {
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                    else{
                        Toast.makeText(getContext(), "Username or Password are Incorrect!", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });
        return binding.getRoot();
    }
}