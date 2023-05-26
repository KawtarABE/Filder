package com.example.filder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.filder.databinding.ActivityMainBinding;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout main_page;
    private ImageView menu;
    private String email;
    private LineChart lineChart;
    private TableLayout tableLayout;

    private ArrayList<Entry> entries_hum = new ArrayList<>();
    private ArrayList<Entry> entries_temp = new ArrayList<>();
    private ArrayList<Entry> entries_uv = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);


        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);
        // Get a reference to the TableLayout
        tableLayout = findViewById(R.id.tableLayout);
        TableRow row = new TableRow(MainActivity.this);
        TextView column1 = new TextView(MainActivity.this);
        column1.setText("Field");
        column1.setGravity(Gravity.CENTER);
        column1.setTypeface(null, Typeface.BOLD);
        row.addView(column1);
        TextView column2 = new TextView(MainActivity.this);
        column2.setGravity(Gravity.CENTER);
        column2.setText("Humidity");
        column2.setTypeface(null, Typeface.BOLD);
        row.addView(column2);
        TextView column3 = new TextView(MainActivity.this);
        column3.setGravity(Gravity.CENTER);
        column3.setText("Temperature");
        column3.setTypeface(null, Typeface.BOLD);
        row.addView(column3);
        TextView column4 = new TextView(MainActivity.this);
        column4.setText("UV");
        column4.setGravity(Gravity.CENTER);
        column4.setTypeface(null, Typeface.BOLD);
        row.addView(column4);
        row.setPadding(0, 10, 0, 20);
        tableLayout.addView(row);

        checkUser();

        // Find the LineChart view by ID
        lineChart = findViewById(R.id.line_chart);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fields");
        Query query = ref.orderByChild("manager").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing rows
                tableLayout.removeAllViews();
                entries_hum.clear();
                entries_temp.clear();
                entries_uv.clear();

                int i = 3;
                List<String> labels = new ArrayList<>();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelField model = ds.getValue(ModelField.class);
                    TableRow row = new TableRow(MainActivity.this);
                    try {
                        entries_hum.add(new Entry(i, Integer.parseInt(model.getHumidity())));
                        entries_temp.add(new Entry(i, Integer.parseInt(model.getTemperature())));
                        entries_uv.add(new Entry(i, Integer.parseInt(model.getUV())));
                        TextView column = new TextView(MainActivity.this);
                        column.setText(model.getNumero());
                        column.setGravity(Gravity.CENTER);
                        row.addView(column);
                        TextView column1 = new TextView(MainActivity.this);
                        column1.setText(model.getHumidity());
                        column1.setGravity(Gravity.CENTER);
                        row.addView(column1);
                        TextView column2 = new TextView(MainActivity.this);
                        column2.setText(model.getTemperature());
                        column2.setGravity(Gravity.CENTER);
                        row.addView(column2);
                        TextView column3 = new TextView(MainActivity.this);
                        column3.setText(model.getUV());
                        column3.setGravity(Gravity.CENTER);
                        row.addView(column3);
                        row.setPadding(0, 10, 0, 20);
                    } catch (NumberFormatException e) {
                        // Handle the exception as necessary
                    }
                    tableLayout.addView(row);
                    labels.add("Field N"+model.getNumero()); // add field name as label
                    i = i + 3;
                }
                LineDataSet dataSet = new LineDataSet(entries_hum, "Humidity of fields");
                dataSet.setColor(Color.GREEN);
                dataSet.setCircleColor(Color.GREEN);
                LineDataSet dataSet_1 = new LineDataSet(entries_temp, "Temperature of fields");
                int greenColor = Color.rgb(0,180,0);
                dataSet_1.setColor(greenColor);
                dataSet_1.setCircleColor(greenColor);
                LineDataSet dataSet_2 = new LineDataSet(entries_uv, "UV of fields");
                int greenColor_1 = Color.rgb(6, 129, 48);
                dataSet_2.setColor(greenColor_1);
                dataSet_2.setCircleColor(greenColor_1);
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSet);
                dataSets.add(dataSet_1);
                dataSets.add(dataSet_2);
                LineData lineData = new LineData(dataSets);
                lineChart.setData(lineData);
                lineChart.getDescription().setEnabled(false);
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);
                lineChart.setPinchZoom(true);
                lineChart.setDrawGridBackground(false);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(true);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setDrawAxisLine(true);

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false);

                Legend legend = lineChart.getLegend();
                legend.setForm(Legend.LegendForm.LINE);
                legend.setTextColor(Color.BLACK);

                // Refresh the chart
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                       Intent intent_3 = new Intent(getApplicationContext(), AllAlerts.class);
                       startActivity(intent_3);
                       finish();
                       break;
                    case R.id.received_alertes:
                        Intent intent_7 = new Intent(getApplicationContext(), Received_alerts.class);
                        startActivity(intent_7);
                        finish();
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