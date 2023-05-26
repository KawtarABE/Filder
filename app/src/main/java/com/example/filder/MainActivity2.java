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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Fields");
        Query query1 = ref1.orderByChild("numero").equalTo(field);
        query1.addListenerForSingleValueEvent();



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
                    case R.id.nav_workers:
                        //Intent intent_2 = new Intent(getApplicationContext(), Profil.class);
                        //startActivity(intent_2);
                        //finish();
                        break;
                    case R.id.nav_alertes:
                       // Intent intent_3 = new Intent(getApplicationContext(), AllAlerts_worker.class);
                       // startActivity(intent_3);
                        //finish();
                        break;
                    case R.id.received_alertes:
                        //Intent intent_7 = new Intent(getApplicationContext(), Received_alerts_worker.class);
                        //startActivity(intent_7);
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