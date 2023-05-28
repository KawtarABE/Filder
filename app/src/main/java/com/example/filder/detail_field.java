package com.example.filder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filder.databinding.ActivityDetailFieldBinding;
import com.google.android.material.navigation.NavigationView;

public class detail_field extends AppCompatActivity {
    private ActivityDetailFieldBinding binding;
    private DrawerLayout main_page;
    private ImageView menu;
    private String email;
    private TextView number_title;
    private TextView number;
    private TextView owner;
    private TextView surface;
    private TextView location;
    private TextView temperature;
    private TextView humidity;
    private TextView uv;
    private Button back;
    private ImageView edit;
    private String owner1;
    private String surface1;
    private String location1;
    private String temperature1;
    private String hum;
    private String uv_1;
    private String numero;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_field);

        binding = ActivityDetailFieldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);
        number_title = findViewById(R.id.number);
        number = findViewById(R.id.number_field);
        owner = findViewById(R.id.owner);
        surface = findViewById(R.id.surface);
        location = findViewById(R.id.location);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        uv = findViewById(R.id.uv);
        back = findViewById(R.id.retour);
        edit = findViewById(R.id.edit);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //number_title.setText("Field "+bundle.getString("numero"));

            owner1 = bundle.getString("owner");
            SpannableStringBuilder builder5 = new SpannableStringBuilder();
            builder5.append("Owner: ");
            SpannableString boldText5 = new SpannableString(owner1);
            boldText5.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, owner1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder5.append(boldText5);
            owner.setText(builder5);

            surface1 = bundle.getString("surface");
            SpannableStringBuilder builder4 = new SpannableStringBuilder();
            builder4.append("Surface: ");
            SpannableString boldText4 = new SpannableString(surface1);
            boldText4.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, surface1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder4.append(boldText4);
            builder4.append(" m²");
            surface.setText(builder4);

            location1 = bundle.getString("location");
            SpannableStringBuilder builder3 = new SpannableStringBuilder();
            builder3.append("Location: ");
            SpannableString boldText3 = new SpannableString(location1);
            boldText3.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, location1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder3.append(boldText3);
            location.setText(builder3);

            temperature1 = bundle.getString("temperature");
            SpannableStringBuilder builder2 = new SpannableStringBuilder();
            builder2.append("Temperature: ");
            SpannableString boldText2 = new SpannableString(temperature1);
            boldText2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, temperature1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder2.append(boldText2);
            builder2.append(" C°");
            temperature.setText(builder2);

            hum = bundle.getString("humidity");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("Humidity: ");
            SpannableString boldText = new SpannableString(hum);
            boldText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, hum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(boldText);
            builder.append(" %");
            humidity.setText(builder);

            uv_1 = bundle.getString("UV");
            SpannableStringBuilder builder0 = new SpannableStringBuilder();
            builder0.append("Water Level: ");
            SpannableString boldText0 = new SpannableString(uv_1);
            boldText0.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, uv_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder0.append(boldText0);
            builder0.append(" mm");
            uv.setText(builder0);

            numero = bundle.getString("numero");
            SpannableStringBuilder builder1 = new SpannableStringBuilder();
            builder1.append("Number: ");
            SpannableString boldText1 = new SpannableString(numero);
            boldText1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, numero.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder1.append(boldText1);
            number.setText(builder1);

            id = bundle.getString("id");

        }

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        /*menu.setOnClickListener(new View.OnClickListener() {
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
                        //Intent intent_2 = new Intent(getApplicationContext(), All_workers.class);
                        //startActivity(intent_2);
                        //finish();
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
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), All_fields.class);
                startActivity(intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateField.class);
                intent.putExtra("id", id);
                intent.putExtra("numero", numero);
                intent.putExtra("location", location1);
                intent.putExtra("surface", surface1);
                intent.putExtra("owner", owner1);
                startActivity(intent);
                finish();
            }
        });
    }

}