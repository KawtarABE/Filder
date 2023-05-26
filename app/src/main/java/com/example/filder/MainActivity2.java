package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.filder.databinding.ActivityMain2Binding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    private DrawerLayout main_page;
    private ImageView menu;
    private String email;
    private TextView number;
    private TextView owner;
    private TextView surface;
    private TextView location;
    private TextView temperature;
    private TextView humidity;
    private TextView uv;
    private TextView manager;
    private TextView electrovanne;
    private String field;
    private String manager_value;
    private String email_path;
    private String owner_value;
    private String surface_value;
    private String location_value;
    private String temperature_value;
    private String humidity_value;
    private String uv_value;
    private int electrovanne_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        email_path = email.replace(".",",");

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        number = findViewById(R.id.number_field);
        owner = findViewById(R.id.owner);
        surface = findViewById(R.id.surface);
        location = findViewById(R.id.location);
        manager = findViewById(R.id.manager);
        temperature = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidty);
        uv = findViewById(R.id.uv);
        electrovanne = findViewById(R.id.electrovanne);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(email_path);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    field = dataSnapshot.child("field").getValue(String.class);
                    manager_value = dataSnapshot.child("manager").getValue(String.class);
                    if (field != null) {
                        // number
                        SpannableStringBuilder builder0 = new SpannableStringBuilder();
                        builder0.append("Field of Work: ");
                        SpannableString boldText0 = new SpannableString(field);
                        boldText0.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, field.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder0.append(boldText0);
                        number.setText(builder0);

                        SpannableStringBuilder builder1 = new SpannableStringBuilder();
                        builder1.append("Manager of the field: ");
                        SpannableString boldText1 = new SpannableString(manager_value.replace("@gmail.com",""));
                        boldText1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, manager_value.replace("@gmail.com","").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder1.append(boldText1);
                        manager.setText(builder1);

                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Fields");
                        Query query1 = ref1.orderByChild("numero").equalTo(field);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        owner_value = childSnapshot.child("owner").getValue(String.class);
                                        location_value = childSnapshot.child("location").getValue(String.class);
                                        surface_value = childSnapshot.child("surface").getValue(String.class);
                                        temperature_value = childSnapshot.child("Temperature").getValue(String.class);
                                        humidity_value = childSnapshot.child("Humidity").getValue(String.class);
                                        uv_value = childSnapshot.child("UV").getValue(String.class);
                                        electrovanne_value = childSnapshot.child("electrovanne").getValue(Integer.class);

                                        if (owner_value != null) {
                                            // Update the UI with the retrieved values
                                            SpannableStringBuilder builder0 = new SpannableStringBuilder();
                                            builder0.append("Owner of Field: ");
                                            SpannableString boldText0 = new SpannableString(owner_value);
                                            boldText0.setSpan(new StyleSpan(Typeface.BOLD), 0, owner_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder0.append(boldText0);
                                            owner.setText(builder0);

                                            SpannableStringBuilder builder1 = new SpannableStringBuilder();
                                            builder1.append("Surface of the field: ");
                                            SpannableString boldText1 = new SpannableString(surface_value);
                                            boldText1.setSpan(new StyleSpan(Typeface.BOLD), 0, surface_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder1.append(boldText1);
                                            surface.setText(builder1);

                                            SpannableStringBuilder builder2 = new SpannableStringBuilder();
                                            builder2.append("Location of the field: ");
                                            SpannableString boldText2 = new SpannableString(location_value);
                                            boldText2.setSpan(new StyleSpan(Typeface.BOLD), 0, location_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder2.append(boldText2);
                                            location.setText(builder2);

                                            SpannableStringBuilder builder3 = new SpannableStringBuilder();
                                            builder3.append("Actual Temperature: ");
                                            SpannableString boldText3 = new SpannableString(temperature_value);
                                            boldText3.setSpan(new StyleSpan(Typeface.BOLD), 0, temperature_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder3.append(boldText3);
                                            builder3.append(" C°");
                                            temperature.setText(builder3);

                                            SpannableStringBuilder builder4 = new SpannableStringBuilder();
                                            builder4.append("Actual Humidity: ");
                                            SpannableString boldText4 = new SpannableString(humidity_value);
                                            boldText4.setSpan(new StyleSpan(Typeface.BOLD), 0, humidity_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder4.append(boldText4);
                                            builder4.append(" F");
                                            humidity.setText(builder4);

                                            SpannableStringBuilder builder5 = new SpannableStringBuilder();
                                            builder5.append("Water Level: ");
                                            SpannableString boldText5 = new SpannableString(uv_value);
                                            boldText5.setSpan(new StyleSpan(Typeface.BOLD), 0, uv_value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder5.append(boldText5);
                                            builder5.append(" cm");
                                            uv.setText(builder5);

                                            SpannableStringBuilder builder6 = new SpannableStringBuilder();
                                            builder6.append("Electrovanne State: ");
                                            if(electrovanne_value == 0) {
                                                SpannableString boldText6 = new SpannableString("Turned Off");
                                                boldText6.setSpan(new StyleSpan(Typeface.BOLD), 0, "Turned Off".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                builder6.append(boldText6);
                                                electrovanne.setText(builder6);
                                            } else {
                                                SpannableString boldText6 = new SpannableString("Turned On");
                                                boldText6.setSpan(new StyleSpan(Typeface.BOLD), 0, "Turned On".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                builder6.append(boldText6);
                                                electrovanne.setText(builder6);
                                            }


                                            // Update other UI elements with the retrieved values

                                        } else {
                                            Log.d(TAG, "Field value is null");
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "User not found");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });


                    } else {
                        Log.d(TAG, "Field value is null");
                    }
                } else {
                    Log.d(TAG, "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error retrieving data: " + databaseError.getMessage());
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
                    case R.id.nav_worker:
                        Intent intent_2 = new Intent(getApplicationContext(), Profil.class);
                        startActivity(intent_2);
                        finish();
                        break;
                    case R.id.nav_alertes:
                       Intent intent_3 = new Intent(getApplicationContext(), AllAlerts_worker.class);
                       startActivity(intent_3);
                       finish();
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