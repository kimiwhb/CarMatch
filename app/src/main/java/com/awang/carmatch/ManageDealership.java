package com.awang.carmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ManageDealership extends AppCompatActivity {

    ImageView btnBack;
    CardView addDealer, viewDealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dealership);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        addDealer = (CardView) findViewById(R.id.addDealer);
        viewDealer = (CardView) findViewById(R.id.viewDealer);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDealer.class);
                startActivity(intent);
                finish();
            }
        });

        viewDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DealerList.class);
                startActivity(intent);
                finish();
            }
        });
    }
}