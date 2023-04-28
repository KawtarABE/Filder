package com.example.filder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filder.databinding.ActivityUpdateFieldBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateField extends AppCompatActivity {

    private ActivityUpdateFieldBinding binding;
    private String email;
    private Button back;
    private Button button_update;
    private EditText number;
    private EditText owner;
    private EditText location;
    private EditText surface;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_field);

        binding = ActivityUpdateFieldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user", "");
        binding.subtitle.setText(email);

        number = findViewById(R.id.numero);
        owner = findViewById(R.id.owner);
        location = findViewById(R.id.localisation);
        surface = findViewById(R.id.surface);

        back = findViewById(R.id.retour);
        button_update = findViewById(R.id.button1);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            number.setText(bundle.getString("numero"));
            owner.setText(bundle.getString("owner"));
            location.setText(bundle.getString("location"));
            surface.setText(bundle.getString("surface"));
            id = bundle.getString("id");

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), All_fields.class);
                    startActivity(intent);
                    finish();
                }
            });

            button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number1 = number.getText().toString();
                    String owner1 = owner.getText().toString();
                    String location1 = location.getText().toString();
                    String surface1 = surface.getText().toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fields");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));

                    // Update the contact data in Firebase Realtime Database
                    contactRef.child("numero").setValue(number1);
                    contactRef.child("location").setValue(location1);
                    contactRef.child("owner").setValue(owner1);
                    contactRef.child("surface").setValue(surface1);

                    // Show a success message to the user
                    Toast.makeText(UpdateField.this, "Field updated successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}