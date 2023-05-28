package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.filder.databinding.ActivityAllAlertsWorkerBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllAlerts_worker extends AppCompatActivity {
    private ImageView menu;
    private DrawerLayout main_page;
    private String email;
    private Button button_add;
    private SearchView search;
    private ActivityAllAlertsWorkerBinding binding;
    private ArrayList<ModelAlert> alertsArrayList;
    private AdapterAlert adapterAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_alerts_worker);

        binding = ActivityAllAlertsWorkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        search = findViewById(R.id.search);

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        loadAlerts();

        button_add = findViewById(R.id.add);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAlert_worker.class);
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
                        Intent intent_2 = new Intent(getApplicationContext(), MainActivity2.class);
                        startActivity(intent_2);
                        finish();
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
                    case R.id.nav_worker:
                        Intent intent_3 = new Intent(getApplicationContext(), Profil.class);
                        startActivity(intent_3);
                        finish();
                        break;
                    case R.id.nav_alertes:
                        break;
                    case R.id.received_alertes:
                        Intent intent_7 = new Intent(getApplicationContext(), Received_alerts_worker.class);
                        startActivity(intent_7);
                        finish();
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
                ArrayList<ModelAlert> filteredAlerts = new ArrayList<>();
                for (ModelAlert alert : alertsArrayList) {
                    if (alert.getContext().toLowerCase().contains(newText.toLowerCase()) ||
                            alert.getText().toLowerCase().contains(newText.toLowerCase())) {
                        filteredAlerts.add(alert);
                    }
                }

                adapterAlert.filterList(filteredAlerts);
                return true;
            }
        });
    }



    private void loadAlerts() {
        alertsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Alerts");
        Query query = ref.orderByChild("sender").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertsArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelAlert model = ds.getValue(ModelAlert.class);
                    alertsArrayList.add(model);

                }


                adapterAlert = new AdapterAlert(AllAlerts_worker.this, alertsArrayList);
                binding.alerts.setAdapter(adapterAlert);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadAlert:onCancelled", error.toException());
            }
        });
    }

}