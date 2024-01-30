package com.awang.carmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarList extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView recyclerView;
    List<CarClass> carList;
    CarAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(CarList.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        carList = new ArrayList<>();
        adapter = new CarAdapter(CarList.this, carList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cars");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    try {
                        CarClass carClass = itemSnapshot.getValue(CarClass.class);
                        if (carClass != null) {
                            carClass.setKey(itemSnapshot.getKey());
                            carList.add(carClass);
                        } else {
                            // Log or handle the case where conversion to CarClass is unsuccessful
                            Log.e("CarList", "Failed to convert to CarClass: " + itemSnapshot.toString());
                        }
                    } catch (DatabaseException e) {
                        // Log or handle the exception
                        Log.e("CarList", "Exception: " + e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle the error
                Log.e("CarList", "DatabaseError: " + error.getMessage());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageCar.class);
                startActivity(intent);
                finish();
            }
        });
    }
}