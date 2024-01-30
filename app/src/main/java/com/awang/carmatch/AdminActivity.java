package com.awang.carmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AdminActivity extends AppCompatActivity {

    CardView manageCar;
    CardView manageDealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageCar = (CardView) findViewById(R.id.manageCar);
        manageDealer = (CardView) findViewById(R.id.manageDealer);

        manageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageCar.class);
                startActivity(intent);
                finish();
            }
        });

        manageDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageDealership.class);
                startActivity(intent);
                finish();
            }
        });
    }
}