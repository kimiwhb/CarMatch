package com.awang.carmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText name, phone, email, password, conPassword, budget;
    TextView loginNowBtn;
    Button registerBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        budget = findViewById(R.id.budget);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.conPassword);
        loginNowBtn = findViewById(R.id.loginNowBtn);
        registerBtn = findViewById(R.id.registerBtn);

        /* registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String nameTxt = name.getText().toString();
                String budgetTxt = budget.getText().toString();
                String phoneTxt = phone.getText().toString();
                String emailTxt = email.getText().toString();
                String PasswordTxt = password.getText().toString();
                // String uniqueId = reference.push().getKey();

                mAuth.createUserWithEmailAndPassword(emailTxt, PasswordTxt)
                        .addOnCompleteListener(Register.this, task -> {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uniqueId = user.getUid(); // Get the user's unique ID

                                // Save the user data in the Realtime Database
                                HelperClass helperClass = new HelperClass(nameTxt, emailTxt, phoneTxt, budgetTxt, PasswordTxt);
                                reference.child(uniqueId).setValue(helperClass);

                                Toast.makeText(Register.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Registration failed. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                            }

                        });

            }
        }); */

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String nameTxt = name.getText().toString();
                String budgetTxt = budget.getText().toString();
                String phoneTxt = phone.getText().toString();
                String emailTxt = email.getText().toString();
                String PasswordTxt = password.getText().toString();
                String conPasswordTxt = conPassword.getText().toString(); // Add this line to get the value of conPassword

                if (!PasswordTxt.equals(conPasswordTxt)) { // Add this condition to check if passwords match
                    Toast.makeText(Register.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailTxt, PasswordTxt)
                        .addOnCompleteListener(Register.this, task -> {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uniqueId = user.getUid(); // Get the user's unique ID

                                // Save the user data in the Realtime Database
                                HelperClass helperClass = new HelperClass(nameTxt, emailTxt, phoneTxt, budgetTxt, PasswordTxt);
                                reference.child(uniqueId).setValue(helperClass);

                                Toast.makeText(Register.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Registration failed. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                            }

                        });

            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}