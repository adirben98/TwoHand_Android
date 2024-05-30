package com.example.twohand_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
Fragment inDisplay;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FeedListFragment feedFragment = new FeedListFragment();
//        CategoryFragment categoryFragment = new CategoryFragment();
//        SharingPostFragment sharePostFragment = new SharingPostFragment();
//        FavoritesFragment favouriteFragment = new FavoritesFragment();
//        ProfileFragment profileFragment = new ProfileFragment();


//        findViewById(R.id.home_btn).setOnClickListener((view)->{displayFragment(feedFragment);});
//        findViewById(R.id.search_button).setOnClickListener((view)->{displayFragment(categoryFragment);});
//        findViewById(R.id.add_btn).setOnClickListener((view)->{displayFragment(sharePostFragment);});
//        findViewById(R.id.favourite_btn).setOnClickListener((view)->{displayFragment(favouriteFragment);});
//        findViewById(R.id.profile_button).setOnClickListener((view)->{displayFragment(profileFragment);});



       NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_NavHost);
       navController= navHostFragment.getNavController();
        //NavigationUI.setupActionBarWithNavController(this,navController);

      BottomNavigationView navView = findViewById(R.id.main_bottomNavigationView);
      NavigationUI.setupWithNavController(navView,navController);
    }

//    private void displayFragment(Fragment fragment) {
//        FragmentManager manager=getSupportFragmentManager();
//        FragmentTransaction trans= manager.beginTransaction();
//        if (inDisplay != fragment) {
//            trans.add(R.id.main_container, fragment);
//            trans.addToBackStack(null);
//        }
//        if(inDisplay!=null)trans.remove(inDisplay);
//        inDisplay=fragment;
//        trans.commit();
//    }


}