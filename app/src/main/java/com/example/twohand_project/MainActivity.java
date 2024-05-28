package com.example.twohand_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Fragment inDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FeedListFragment feedFragment = new FeedListFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        SharingPostFragment sharePostFragment = new SharingPostFragment();
        FavoritesFragment favouriteFragment = new FavoritesFragment();
        ProfileFragment profileFragment = new ProfileFragment();


        findViewById(R.id.home_btn).setOnClickListener((view)->{displayFragment(feedFragment);});
        findViewById(R.id.search_button).setOnClickListener((view)->{displayFragment(categoryFragment);});
        findViewById(R.id.add_btn).setOnClickListener((view)->{displayFragment(sharePostFragment);});
        findViewById(R.id.favourite_btn).setOnClickListener((view)->{displayFragment(favouriteFragment);});
        findViewById(R.id.profile_button).setOnClickListener((view)->{displayFragment(profileFragment);});


    }

    private void displayFragment(Fragment feedFragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction trans= manager.beginTransaction();
        if (inDisplay != feedFragment) {
            trans.add(R.id.main_container, feedFragment);
            trans.addToBackStack("TAG");
        }
        if(inDisplay!=null)trans.remove(inDisplay);
        inDisplay=feedFragment;
        trans.commit();
    }


}