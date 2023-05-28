package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.filder.databinding.ActivityMain2Binding;
import com.example.filder.databinding.ActivityProfilBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profil extends AppCompatActivity {
    private ActivityProfilBinding binding;
    private EditText worker_name;
    private EditText worker_email;
    private EditText role;
    private EditText field;
    private ImageView imgUser;
    private DatabaseReference mDatabase;
    private Button button_update;
    private DrawerLayout main_page;
    private ImageView menu;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);
        String email_path = email.replace(".",",");

        String img_uri;

        worker_name = findViewById(R.id.name);
        worker_email = findViewById(R.id.email);
        role = findViewById(R.id.role);
        field = findViewById(R.id.field);
        button_update = findViewById(R.id.button1);
        imgUser = findViewById(R.id.img_user);

        menu = findViewById(R.id.menu_button);
        main_page = findViewById(R.id.main_page);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_page.openDrawer(GravityCompat.START);
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
        Query query1 = ref1.orderByChild("email").equalTo(email_path);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String name_value = childSnapshot.child("name").getValue(String.class);
                        String role_value = childSnapshot.child("role").getValue(String.class);
                        String field_value = childSnapshot.child("field").getValue(String.class);
                        String img_uri = childSnapshot.child("img_uri").getValue(String.class);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child("images/"+img_uri);

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // The download URL was successfully retrieved
                                String downloadUrl = uri.toString();

                                // Load the image with Glide
                                // Check if the activity is not destroyed before loading the image with Glide
                                if (!isFinishing()) {
                                    Glide.with(Profil.this)
                                            .load(downloadUrl)
                                            .placeholder(R.drawable.profile)
                                            .into(imgUser);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Failed to retrieve the download URL
                                // Handle the error
                            }
                        });

                        worker_name.setText(name_value);
                        worker_email.setText(email);
                        role.setText(role_value);
                        field.setText(field_value);
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

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditText fields
                String name = worker_name.getText().toString();
                String newEmail = worker_email.getText().toString().replace(".", ",");
                String roleValue = role.getText().toString();
                String fieldValue = field.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                // Get the reference of the current worker
                DatabaseReference workerRef = ref.child(email_path);

                workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the worker data from the current node
                            Object workerData = dataSnapshot.getValue();

                            // Remove the old node
                            workerRef.removeValue();

                            // Create a new DatabaseReference with the desired reference (key)
                            DatabaseReference newWorkerRef = ref.child(newEmail);

                            // Set the worker data in the new node
                            newWorkerRef.setValue(workerData);

                            // Update the entered data in the new node
                            newWorkerRef.child("name").setValue(name);
                            newWorkerRef.child("email").setValue(newEmail);
                            newWorkerRef.child("role").setValue(roleValue);
                            newWorkerRef.child("field").setValue(fieldValue);

                            // Update the email in SharedPreferences
                            SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                            editor.putString("user", newEmail.replace(",", "."));
                            editor.apply();

                            // Show a success message to the user
                            Toast.makeText(Profil.this, "Worker updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent_2 = new Intent(getApplicationContext(), Profil.class);
                            startActivity(intent_2);
                            finish();
                        } else {
                            Log.d(TAG, "User not found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });


        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent_2 = new Intent(getApplicationContext(), MainActivity2.class);
                        startActivity(intent_2);
                        finish();
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
                        break;
                    case R.id.nav_alertes:
                        Intent intent_3 = new Intent(getApplicationContext(), AllAlerts_worker.class);
                        startActivity(intent_3);
                        finish();
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
}