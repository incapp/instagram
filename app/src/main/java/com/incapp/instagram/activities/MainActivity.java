package com.incapp.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.incapp.instagram.R;
import com.incapp.instagram.fragments.HomeFragment;
import com.incapp.instagram.fragments.ProfileFragment;
import com.incapp.instagram.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_container,
                                    new ProfileFragment(),
                                    ProfileFragment.class.getSimpleName()
                            )
                            .commit();
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_container,
                                    new SearchFragment(),
                                    SearchFragment.class.getSimpleName()
                            )
                            .commit();
                    return true;
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_container,
                                    new HomeFragment(),
                                    HomeFragment.class.getSimpleName()
                            )
                            .commit();
                    return true;
                case R.id.navigation_add:
                    Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                    startActivity(intent);
                    return false;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        new HomeFragment(),
                        HomeFragment.class.getSimpleName()
                )
                .commit();
    }
}
