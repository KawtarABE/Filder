package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.filder.databinding.ActivityAddAlertBinding;
import com.example.filder.databinding.ActivityAddFieldBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddAlert extends AppCompatActivity {

    private ActivityAddAlertBinding binding;
    private String email;
    private Button back;
    private Button button_add;
    private EditText context;
    private EditText text;
    private Spinner field;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        binding = ActivityAddAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        back = findViewById(R.id.retour);
        button_add = findViewById(R.id.button1);

        context = findViewById(R.id.alert_context);
        text = findViewById(R.id.alert_text);


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
                addAlert();
            }
        });

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

        field = findViewById(R.id.field);

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user", "");
        binding.subtitle.setText(email);


    }
    private void addAlert() {
        String context_1, text_1, field_1;


        context_1 = String.valueOf(context.getText());
        text_1 = String.valueOf(text.getText());
        field_1 = field.getSelectedItem().toString();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(date);


        HashMap<String, Object> hashmap = new HashMap<>();
        String id = FirebaseDatabase.getInstance().getReference("Fields").push().getKey();
        hashmap.put("id",""+id);
        hashmap.put("context",""+context_1);
        hashmap.put("text",""+text_1);
        hashmap.put("sender","couthonmallory@gmail.com");
        hashmap.put("field",""+field_1);
        hashmap.put("date",formattedDate);
        hashmap.put("open",false);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Alerts");
        ref.child(""+id).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddAlert.this,"Field successfully added...", Toast.LENGTH_SHORT).show();
                        Intent intent_3 = new Intent(getApplicationContext(), AllAlerts.class);
                        startActivity(intent_3);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAlert.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
