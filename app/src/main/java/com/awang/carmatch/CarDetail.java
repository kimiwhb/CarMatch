package com.awang.carmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarDetail extends AppCompatActivity {

    ImageView imgCar, btnBack;
    TextView carModel, carBrand, carCat, carPrice;
    Button editButton, deleteButton, findDealership;
    String key = "";
    String urlImage = "";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        carModel = findViewById(R.id.carModel);
        carBrand = findViewById(R.id.carBrand);
        carCat = findViewById(R.id.carCat);
        carPrice = findViewById(R.id.carPrice);
        imgCar = findViewById(R.id.imgCar);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        editButton = (Button) findViewById(R.id.editButton);
        findDealership = (Button) findViewById(R.id.findDealership);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            carModel.setText(bundle.getString("Car Model"));
            carBrand.setText(bundle.getString("Car Brand"));
            carCat.setText(bundle.getString("Car Cat"));
            carPrice.setText(bundle.getString("Car Price"));

            key = bundle.getString("Key");
            urlImage = bundle.getString("Car Image");
            Glide.with(this).load(bundle.getString("Car Image")).into(imgCar);
        }

        if (user != null) {
            String adminEmail = user.getEmail();
            if ("admin@carmatch.com".equals(adminEmail)) {
                // For Admin
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                findDealership.setVisibility(View.GONE);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), CarList.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                // For General Users
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                findDealership.setVisibility(View.VISIBLE);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("cars");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(urlImage);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(CarDetail.this, "Car has been deleted.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CarList.class));
                        finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetail.this, UpdateCar.class)
                        .putExtra("Car Model", carModel.getText().toString())
                        .putExtra("Car Brand", carBrand.getText().toString())
                        .putExtra("Car Cat", carCat.getText().toString())
                        .putExtra("Car Price", carPrice.getText().toString())
                        .putExtra("Car Image", urlImage)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });

        findDealership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetail.this, DealerMap.class)
                        .putExtra("Car Brand", carBrand.getText().toString())
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }
}