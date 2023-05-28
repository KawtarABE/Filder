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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.filder.databinding.ActivityReceivedAlertsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Received_alerts extends AppCompatActivity {
    private ImageView menu;
    private DrawerLayout main_page;
    private String email;
    private Button button_add;
    private SearchView search;
    private ActivityReceivedAlertsBinding binding;
    private ArrayList<ModelAlert> alertsArrayList;
    private AdapterAlert adapterAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_alerts);
        binding = ActivityReceivedAlertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        search = findViewById(R.id.search);

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        loadAlerts();

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
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_fields:
                        Intent intent_7 = new Intent(getApplicationContext(), All_fields.class);
                        startActivity(intent_7);
                        finish();
                        break;

                    case R.id.nav_workers:
                        Intent intent_2 = new Intent(getApplicationContext(), Allworkers.class);
                        startActivity(intent_2);
                        finish();
                        break;
                    case R.id.nav_alertes:
                        Intent intent_9 = new Intent(getApplicationContext(), AllAlerts.class);
                        startActivity(intent_9);
                        finish();
                        break;
                    case R.id.received_alertes:
                        break;
                    case R.id.nav_electrovanne:
                        Intent intent_4 = new Intent(getApplicationContext(), Electrovanne.class);
                        startActivity(intent_4);
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fields");
        Query query = ref.orderByChild("manager").equalTo(email);
        List<String> fieldNumbers = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fieldNumber = snapshot.child("numero").getValue(String.class);
                    fieldNumbers.add(fieldNumber);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        alertsArrayList = new ArrayList<>();
        DatabaseReference ref_1 = FirebaseDatabase.getInstance().getReference("Alerts");
        ref_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertsArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelAlert model = ds.getValue(ModelAlert.class);
                    String fieldNumber = model.getField();

                    // Check if the fieldNumber is in the list
                    if (fieldNumbers.contains(fieldNumber) && !model.getSender().equals(email)) {
                        alertsArrayList.add(model);
                    }
                }
                adapterAlert = new AdapterAlert(Received_alerts.this, alertsArrayList);
                binding.alerts.setAdapter(adapterAlert);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadAlert:onCancelled", error.toException());
            }
        });

    }


}