package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animal_helpers.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
        setContentView(binding.getRoot());

        init();
        SettingListener();

        bottomNavigationView.setSelectedItemId(R.id.tab_home);



    }
    private void init() {
//        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_search: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new FavoriteFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_home: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new HomeFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_chat: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new ChatFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_account: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new AccountFragment())
                            .commit();
                    return true;
                }
            }

            return false;
        }
    }




}