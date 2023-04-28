package com.example.filder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.filder.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout main_page;
    private ImageView menu;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        checkUser();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_page.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;

                    case R.id.nav_logout:
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                        editor.putBoolean("isUserLoggedIn", false);
                        editor.putString("email",null);
                        editor.putString("type",null);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_fields:
                        Intent intent_1 = new Intent(getApplicationContext(), All_fields.class);
                        startActivity(intent_1);
                        finish();
                        break;
                    case R.id.nav_workers:
                        Intent intent_2 = new Intent(getApplicationContext(), Allworkers.class);
                        startActivity(intent_2);
                        finish();
                        break;
                    case R.id.nav_alertes:
                        //Intent intent_3 = new Intent(getApplicationContext(), Alerts.class);
                        //startActivity(intent_3);
                        //finish();
                        break;
                    case R.id.nav_electrovanne:
                        //Intent intent_4 = new Intent(getApplicationContext(), Electrovanne.class);
                        //startActivity(intent_4);
                        //finish();
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.main_page);
                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    private void checkUser() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isUserLoggedIn", false);
        if (isLoggedIn) {
            email = prefs.getString("user","");
            binding.subtitle.setText(email);
        }
        else {
            startActivity(new Intent(this, Login.class));
            finish();
        }

    }
}