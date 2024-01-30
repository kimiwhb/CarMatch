package com.awang.carmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class DealerList extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView rv;
    List<Dealer> dealerList;
    DealerAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_list);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        rv = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(DealerList.this, 1);
        rv.setLayoutManager(gridLayoutManager);
        dealerList = new ArrayList<>();
        adapter = new DealerAdapter(DealerList.this, dealerList);
        rv.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("dealership");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dealerList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    try {
                        Dealer dealer = itemSnapshot.getValue(Dealer.class);
                        if (dealer != null) {
                            dealer.setId(itemSnapshot.getKey());
                            dealerList.add(dealer);
                        } else {
                            // Log or handle the case where conversion to CarClass is unsuccessful
                            Log.e("DealerList", "Failed to convert to Dealer: " + itemSnapshot.toString());
                        }
                    } catch (DatabaseException e) {
                        // Log or handle the exception
                        Log.e("DealerList", "Exception: " + e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle the error
                Log.e("DealerList", "DatabaseError: " + error.getMessage());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageDealership.class);
                startActivity(intent);
                finish();
            }
        });
    }
}