package com.example.eventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SupplierActivity extends AppCompatActivity {

    int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        userID = getIntent().getIntExtra("userID", 0);


        if (savedInstanceState == null) {
            Fragment homeFragment = new SupplierHomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("userID", userID);
            homeFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, homeFragment)
                    .commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new SupplierHomeFragment();
            }else if (item.getItemId() == R.id.nav_restock){
                selectedFragment = new SupplierRestockFragment();
            }
            else if (item.getItemId() == R.id.nav_logout) {
                logoutUser();
                return true;
            }

            if (selectedFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("userID", userID);
                selectedFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, selectedFragment)
                        .commit();
            }
            return true;
        });

    }
    private void logoutUser () {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}