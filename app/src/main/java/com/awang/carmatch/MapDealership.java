package com.awang.carmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDealership extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapDealership";

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dealership);

        // Get the latitude and longitude values from the intent as strings
        String latitudeString = getIntent().getStringExtra("Latitude");
        String longitudeString = getIntent().getStringExtra("Longitude");

        // Convert the string values to double values
        if (latitudeString != null && !latitudeString.isEmpty()) {
            mLatitude = Double.parseDouble(latitudeString);
        } else {
            // Handle the error case where the latitude value is missing or invalid.
        }

        if (longitudeString != null && !longitudeString.isEmpty()) {
            mLongitude = Double.parseDouble(longitudeString);
        } else {
            // Handle the error case where the longitude value is missing or invalid.
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or shapes, add listeners for click events, and move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install it
     * inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String dealerName = getIntent().getStringExtra("Dealer Name");

        // Add a marker for the dealership location and display the dealership name
        LatLng dealershipLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(dealershipLocation).title(dealerName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dealershipLocation, 15));
    }
}