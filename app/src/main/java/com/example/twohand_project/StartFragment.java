package com.example.twohand_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.databinding.FragmentStartBinding;


public class StartFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentStartBinding binding= FragmentStartBinding.inflate(inflater,container,false);
        binding.loginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_logInFragment));
        binding.registerButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_registerFragment));

        return binding.getRoot();
    }
}