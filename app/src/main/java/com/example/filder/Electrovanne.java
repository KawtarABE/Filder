package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.filder.databinding.ActivityAllAlertsBinding;
import com.example.filder.databinding.ActivityElectrovanneBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Electrovanne extends AppCompatActivity {
    private ImageView menu;
    private PieChart pieChart;
    private DrawerLayout main_page;
    private String email;
    private ActivityElectrovanneBinding binding;
    private Spinner field;
    private float waterLevel = 0f;
    private Button turn_on;
    private Button turn_off;
    private String selectedFieldNumber;
    private String id_field;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrovanne);

        binding = ActivityElectrovanneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);
        turn_on = findViewById(R.id.turn_on);
        turn_off = findViewById(R.id.turn_off);



        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        field = findViewById(R.id.field);
        PieChart pieChart = findViewById(R.id.pieChart);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fields");
        Query query = ref.orderByChild("manager").equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> fieldNumbers = new ArrayList<>();
                fieldNumbers.add("Choose field of work");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fieldNumber = snapshot.child("numero").getValue(String.class);
                    fieldNumbers.add(fieldNumber);
                }

                Spinner spinner = findViewById(R.id.field);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, fieldNumbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        Spinner fieldSpinner = findViewById(R.id.field);
        fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFieldNumber = parent.getItemAtPosition(position).toString();
                selectedPosition = position;

                DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("Fields");
                Query query0 = ref0.orderByChild("numero").equalTo(selectedFieldNumber);

                query0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String value = ds.child("UV").getValue(String.class);
                            id_field = ds.child("id").getValue(String.class);
                            DatabaseReference electrovanneRefer = FirebaseDatabase.getInstance().getReference().child("Fields").child(id_field).child("electrovanne");

                            electrovanneRefer.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        int electrovanneValue = dataSnapshot.getValue(Integer.class);

                                        if (electrovanneValue == 0) {
                                            turn_on.setVisibility(View.VISIBLE);
                                        } else if (electrovanneValue == 1) {
                                            turn_off.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle potential database error
                                }
                            });
                            if (value != null) {
                                try {
                                    waterLevel = Float.parseFloat(value);
                                    updatePieChart((waterLevel*100)/500);
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, "Invalid UV value: " + value, e);
                                }
                            } else {
                                Log.e(TAG, "UV value is null");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "DatabaseError: " + error.getMessage());
                    }
                });

                selectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected in the spinner
            }
        });

        // Assuming you have a reference to your Firebase database
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Fields");


        turn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ref.child(id_field).child("electrovanne").setValue(1);
                turn_on.setVisibility(View.GONE);
                turn_off.setVisibility(View.VISIBLE);
                fieldSpinner.setSelection(selectedPosition);
            }
        });

// You can add a similar click listener for the turn_off button to set the value to 0
        turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the value of electrovanne to 0 in Firebase
                Ref.child(id_field).child("electrovanne").setValue(0);

                // Hide turn_off button and show turn_on button
                turn_off.setVisibility(View.GONE);
                turn_on.setVisibility(View.VISIBLE);
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
                        Intent intent_8 = new Intent(getApplicationContext(), Received_alerts.class);
                        startActivity(intent_8);
                        finish();
                        break;
                    case R.id.nav_electrovanne:
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.main_page);
                drawerLayout.closeDrawers();

                return true;
            }
        });
    }
    private void updatePieChart(float waterLevel) {
        // Create a list of pie entries
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(waterLevel, "Water Level"));
        pieEntries.add(new PieEntry(100 - waterLevel, "Water Need"));

        // Create a pie dataset
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

        int[] colors = new int[] {
                Color.parseColor("#068152"),
                Color.parseColor("#e5e5e5")
        };
        pieDataSet.setColors(colors);

        // Create a pie data object
        PieData pieData = new PieData(pieDataSet);

        // Get a reference to the PieChart view
        PieChart pieChart = findViewById(R.id.pieChart);

        // Apply styling to the pie chart
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.getDescription().setEnabled(false);


        // Set the pie data to the chart
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

}
