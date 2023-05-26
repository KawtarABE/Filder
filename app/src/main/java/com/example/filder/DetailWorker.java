package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.filder.databinding.ActivityAllFieldsBinding;
import com.example.filder.databinding.ActivityDetailWorkerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailWorker extends AppCompatActivity {

    private EditText worker_name;
    private EditText worker_email;
    private EditText role;
    private EditText field;
    private ImageView imgUser;
    private DatabaseReference mDatabase;
    private Button button_update;
    private Button button_back;
    private TextView subtitle;
    private ActivityDetailWorkerBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_worker);

        binding = ActivityDetailWorkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String img_uri;

        worker_name = findViewById(R.id.name);
        worker_email = findViewById(R.id.email);
        role = findViewById(R.id.role);
        field = findViewById(R.id.field);
        button_update = findViewById(R.id.button1);
        button_back = findViewById(R.id.retour);
        imgUser = findViewById(R.id.img_user);
        subtitle = findViewById(R.id.subtitle);

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worker_name.setText(bundle.getString("name"));
            worker_email.setText(bundle.getString("email_Worker").replace(",","."));
            role.setText(bundle.getString("role"));
            field.setText(bundle.getString("field"));
            img_uri = bundle.getString("img_uri");


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/"+img_uri);

            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // The download URL was successfully retrieved
                    String downloadUrl = uri.toString();

                    // Load the image with Glide
                    Glide.with(DetailWorker.this)
                            .load(downloadUrl)
                            .placeholder(R.drawable.profile)
                            .into(imgUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Failed to retrieve the download URL
                    // Handle the error
                }
            });


            /*button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the values from the EditText fields
                    String name = worker_name.getText().toString();
                    String email_1 = worker_email.getText().toString().replace(".",",");
                    String role_1 = role.getText().toString();
                    String field_1 = field.getText().toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    DatabaseReference workerRef = ref.child(bundle.getString("email_Worker"));

                    // Update the worker data in Firebase Realtime Database
                    workerRef.child("name").setValue(name);
                    workerRef.child("email").setValue(email_1);
                    workerRef.child("role").setValue(role_1);
                    workerRef.child("field").setValue(field_1);

                    // Show a success message to the user
                    Toast.makeText(DetailWorker.this, "worker updated successfully", Toast.LENGTH_SHORT).show();

                }
            });*/


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
                    DatabaseReference workerRef = ref.child(bundle.getString("email_Worker").replace(".",","));

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


                                // Show a success message to the user
                                Toast.makeText(DetailWorker.this, "Worker updated successfully", Toast.LENGTH_SHORT).show();

                                Intent intent_2 = new Intent(getApplicationContext(), Allworkers.class);
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



            button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Allworkers.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}