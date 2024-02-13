package com.awang.carmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DealerMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DealerMap";

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private LatLng dealershipLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dealership);

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

        String selectedCarBrand = getIntent().getStringExtra("Car Brand"); // Assuming you pass the selected car brand from the previous activity

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("dealership");

        Query query = databaseReference.orderByChild("carBrand").equalTo(selectedCarBrand);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Dealer dealer = childSnapshot.getValue(Dealer.class);

                    String fbLat = dealer.getLatitude();
                    String fbLong = dealer.getLongitude();

                    // Convert the string values to double values
                    if (fbLat != null && !fbLat.isEmpty()) {
                        mLatitude = Double.parseDouble(fbLat);
                    } else {
                        // Handle the error case where the latitude value is missing or invalid.
                    }

                    if (fbLong != null && !fbLong.isEmpty()) {
                        mLongitude = Double.parseDouble(fbLong);
                        dealershipLocation = new LatLng(mLatitude, mLongitude);
                        mMap.addMarker(new MarkerOptions().position(dealershipLocation).title(dealer.getName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dealershipLocation, 15));
                    } else {
                        // Handle the error case where the longitude value is missing or invalid.
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching dealerships", databaseError.toException());
            }
        });
    }
}