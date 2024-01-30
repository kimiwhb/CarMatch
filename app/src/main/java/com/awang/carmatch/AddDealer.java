package com.awang.carmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddDealer extends AppCompatActivity {

    ImageView btnBack;
    EditText dealerName, dealerLat, dealerLong;
    Button btnAddDealer;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://carmatchfyp-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dealer);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnAddDealer = (Button) findViewById(R.id.btnAddDealer);
        dealerName = (EditText) findViewById(R.id.dealerName);
        dealerLat = (EditText) findViewById(R.id.dealerLat);
        dealerLong = (EditText) findViewById(R.id.dealerLong);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageDealership.class);
                startActivity(intent);
                finish();
            }
        });

        btnAddDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        if (areAllFieldsFilled()) {
            String id = dbRef.child("dealership").push().getKey();
            Dealer dealer = new Dealer(dealerName.getText().toString(), dealerLat.getText().toString(), dealerLong.getText().toString());
            dbRef.child("dealership").child(id).setValue(dealer);
            Toast.makeText(AddDealer.this, "Dealer added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DealerList.class);
            startActivity(intent);
            clearFields();
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areAllFieldsFilled() {
        return  !dealerName.getText().toString().isEmpty() &&
                !dealerLat.getText().toString().isEmpty() &&
                !dealerLong.getText().toString().isEmpty();
    }

    private void clearFields() {
        dealerName.setText("");
        dealerLat.setText("");
        dealerLong.setText("");
    }
}