package com.example.twohand_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.twohand_project.Model.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Fragment inDisplay;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(() -> {

            if (Model.instance().isUserLoggedIn()) {
                setContentView(R.layout.activity_main);

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                navController = navHostFragment.getNavController();

                Set<Integer> topLevelDestinations = new HashSet<>();
                topLevelDestinations.add(R.id.feedListFragment);
                topLevelDestinations.add(R.id.categoryFragment);
                topLevelDestinations.add(R.id.sharingPostFragment);
                topLevelDestinations.add(R.id.favoritesFragment);
                topLevelDestinations.add(R.id.profileFragment);


                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

                BottomNavigationView navView = findViewById(R.id.main_bottomNavigationView);
                NavigationUI.setupWithNavController(navView, navController);
            } else {
                Intent intent=new Intent(this,RegisterLoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, new AppBarConfiguration.Builder(navController.getGraph()).build())
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}
