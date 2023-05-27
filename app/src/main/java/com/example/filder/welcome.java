package com.example.filder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class welcome extends AppCompatActivity {

    private Button start_button;

    @Override
    public void onBackPressed() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isUserLoggedIn", false);
        if (isLoggedIn) {
            // Handle back navigation within the app
            super.onBackPressed();
        } else {
            // Handle back navigation when logged out
            moveTaskToBack(true);
            finish();;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        start_button = findViewById(R.id.button_start);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}