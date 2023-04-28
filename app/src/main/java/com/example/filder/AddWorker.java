package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filder.databinding.ActivityAddFieldBinding;
import com.example.filder.databinding.ActivityAddWorkerBinding;
import com.example.filder.databinding.ActivityAllFieldsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddWorker extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button button_add;
    private EditText Worker_name;
    private EditText Worker_password;
    private Spinner Worker_field;
    private EditText Worker_email;
    private EditText Worker_role;
    private ImageView Worker_profil;
    private Button back;
    private Uri img_path = Uri.parse("");
    private TextView subtitle;
    private String email;
    private ActivityAddWorkerBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);

        binding = ActivityAddWorkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        button_add = findViewById(R.id.button1);

        Worker_name = findViewById(R.id.name);
        Worker_email = findViewById(R.id.email);
        Worker_profil = findViewById(R.id.img_user);
        Worker_role = findViewById(R.id.role);
        Worker_password = findViewById(R.id.password);

        back = findViewById(R.id.retour);



        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        email = prefs.getString("user","");
        binding.subtitle.setText(email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Allworkers.class);
                startActivity(intent);
                finish();
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorker();
            }
        });

        Worker_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
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

        Worker_field = findViewById(R.id.field);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null) {
            img_path = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),img_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Worker_profil.setImageBitmap(bitmap);
    }

    private void addWorker() {
        String name, field, email_worker, uri, role, password;
        name = String.valueOf(Worker_name.getText());
        email_worker = String.valueOf(Worker_email.getText()).replace(".", ",");
        role = String.valueOf(Worker_role.getText());
        field = Worker_field.getSelectedItem().toString();
        password = String.valueOf(Worker_password.getText());
        uri = UUID.randomUUID().toString();

        FirebaseStorage.getInstance().getReference("images/"+ uri).putFile(img_path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                }
            }
        });

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("name",""+name);
        hashmap.put("email",""+email_worker);
        hashmap.put("img_uri",""+uri);
        hashmap.put("manager",""+email);
        hashmap.put("password",""+password);
        hashmap.put("role",""+role);
        hashmap.put("type","Worker");
        hashmap.put("field",field);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+email_worker).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddWorker.this,"Worker successfuly added...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddWorker.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}