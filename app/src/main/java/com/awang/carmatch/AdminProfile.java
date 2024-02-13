package com.awang.carmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfile extends AppCompatActivity {


    TextView profileName, profileEmail, profilePassword;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Button btnLogout;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminProfile.this, "Logged Out.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        showUserData();
    }

    public void showUserData() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        firebaseUser = firebaseAuth.getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminProfile.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String nameUser = snapshot.child("name").getValue(String.class);
                    String emailUser = snapshot.child("email").getValue(String.class);
                    String passwordUser = snapshot.child("password").getValue(String.class);

                    profileName.setText(nameUser);
                    profileEmail.setText(emailUser);
                    profilePassword.setText(passwordUser);
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminProfile.this, "Failed to retrieve user data. Check internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}