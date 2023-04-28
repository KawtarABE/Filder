package com.example.filder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.filder.databinding.ActivityAllworkersBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Allworkers extends AppCompatActivity {
    private Button button_add;
    private ActivityAllworkersBinding binding;
    private SearchView search;
    private ImageView menu;
    private String email;
    private ArrayList<ModelWorker> workersArrayList;
    private AdapterWorker adapterWorker;
    private DrawerLayout main_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allworkers);

        binding = ActivityAllworkersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);
        search = findViewById(R.id.search);
        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        loadWorkers();

        button_add = findViewById(R.id.add);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddWorker.class);
                startActivity(intent);
                finish();
            }
        });

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
                        Intent intent_1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_1);
                        finish();
                        break;

                    case R.id.nav_logout:
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                        editor.putBoolean("isUserLoggedIn", false);
                        editor.putString("user",null);
                        editor.putString("type",null);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_fields:
                        Intent intent_2 = new Intent(getApplicationContext(), All_fields.class);
                        startActivity(intent_2);
                        finish();
                        break;

                    case R.id.nav_workers:
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

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ModelWorker> filteredWorkers = new ArrayList<>();
                for (ModelWorker worker : workersArrayList) {
                    if (worker.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredWorkers.add(worker);
                    }
                }
                adapterWorker.filterList(filteredWorkers);
                return true;
            }
        });
    }

    private void loadWorkers() {
        workersArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("manager").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workersArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelWorker model = ds.getValue(ModelWorker.class);
                    workersArrayList.add(model);
                }


                adapterWorker = new AdapterWorker(Allworkers.this, workersArrayList);
                binding.fields.setAdapter(adapterWorker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}