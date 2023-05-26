package com.example.filder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filder.databinding.ActivityAddFieldBinding;
import com.example.filder.databinding.ActivityAllFieldsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddField extends AppCompatActivity {

    private ActivityAddFieldBinding binding;
    private String email;
    private Button back;
    private Button button_add;
    private EditText surface;
    private EditText location;
    private EditText owner;
    private EditText numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        binding = ActivityAddFieldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        back = findViewById(R.id.retour);
        button_add = findViewById(R.id.button1);

        surface = findViewById(R.id.surface);
        location = findViewById(R.id.localisation);
        owner = findViewById(R.id.owner);
        numero = findViewById(R.id.numero);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), All_fields.class);
                startActivity(intent);
                finish();
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addField();
            }
        });

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user", "");
        binding.subtitle.setText(email);


    }
    private void addField() {
        String surface_1, location_1, owner_1, number_1;
        List<String> history_temp, history_hum, history_uv;
        history_temp = new ArrayList<>();
        history_hum = new ArrayList<>();
        history_uv = new ArrayList<>();

        surface_1 = String.valueOf(surface.getText());
        location_1 = String.valueOf(location.getText());
        owner_1 = String.valueOf(owner.getText());
        number_1 = String.valueOf(numero.getText());

        HashMap<String, Object> hashmap = new HashMap<>();
        String id = FirebaseDatabase.getInstance().getReference("Fields").push().getKey();
        hashmap.put("id",""+id);
        hashmap.put("surface",""+surface_1);
        hashmap.put("location",""+location_1);
        hashmap.put("owner",""+owner_1);
        hashmap.put("numero",""+number_1);
        hashmap.put("Temperature","0");
        hashmap.put("UV","0");
        hashmap.put("Humidity","0");
        hashmap.put("manager",""+email);
        hashmap.put("history_temp",""+history_temp);
        hashmap.put("history_hum",""+history_hum);
        hashmap.put("history_uv",""+history_uv);
        hashmap.put("electrovanne",0);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Fields");
        ref.child(""+id).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddField.this,"Field successfully added...", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddField.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

