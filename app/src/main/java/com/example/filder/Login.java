package com.example.filder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private LinearLayout loginLayout;
    private LinearLayout logupLayout;
    private TextView signUpTextView;
    private TextView signInTextView;
    //private EditText register_name;
    private EditText register_email;
    private EditText  register_password;
    private Spinner user_type;
    private Button button_register;
    private EditText login_email;
    private EditText login_password;
    private Button button_login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
            //finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginLayout = findViewById(R.id.section1);
        logupLayout = findViewById(R.id.section2);
        signUpTextView = findViewById(R.id.signup);

        //register_name = findViewById(R.id.name);
        register_email = findViewById(R.id.email_reg);
        register_password = findViewById(R.id.password_reg);

        button_register = findViewById(R.id.button1);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        button_login = findViewById(R.id.button_login);
        user_type = findViewById(R.id.user_type);
        progressBar=findViewById(R.id.progress_bar);





        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = String.valueOf(login_email.getText());
                final String emailPath = email.replace(".", ",");
                final String password = String.valueOf(login_password.getText());

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(emailPath)){
                                final String getPassword = snapshot.child(emailPath).child("password").getValue(String.class);
                                final String userType = snapshot.child(emailPath).child("type").getValue(String.class);
                                if(getPassword.equals(password)) {
                                    Toast.makeText(Login.this, "Sign in successful",
                                            Toast.LENGTH_SHORT).show();
                                    if(userType.equals("Manager")) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                                else{
                                    Toast.makeText(Login.this, "Password incorrect",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "User doesn't exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });



        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the login layout
                loginLayout.setVisibility(View.GONE);

                // Show the logup layout
                logupLayout.setVisibility(View.VISIBLE);
            }
        });

        signInTextView = findViewById(R.id.login);

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the login layout
                logupLayout.setVisibility(View.GONE);

                // Show the logup layout
                loginLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addUser() {
        String email, password, userType;
        email = String.valueOf(register_email.getText());
        String emailPath = email.replace(".", ",");
        password = String.valueOf(register_password.getText());
        userType = user_type.getSelectedItem().toString();

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("email",""+emailPath);
        hashmap.put("password",""+password);
        hashmap.put("type",""+userType);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+emailPath).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Login.this,"User successfuly added...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}