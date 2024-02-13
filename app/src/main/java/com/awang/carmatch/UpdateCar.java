package com.awang.carmatch;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class UpdateCar extends AppCompatActivity {

    Button updateButton;
    TextView carModel, carBrand, carPrice, carCategory;
    ImageView imgCar, btnBack;
    Uri uri;
    // String imageUrl;
    String key, oldImageURL;
    DatabaseReference databaseReference;
    // StorageReference storageReference;
    String model, brand, price, category;
    Spinner spinnerBrand, spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        updateButton = (Button) findViewById(R.id.updateButton);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        carModel = (TextView) findViewById(R.id.carModel);
        // carBrand = (TextView) findViewById(R.id.carBrand);
        carPrice = (TextView) findViewById(R.id.carPrice);
        // carCategory = (TextView) findViewById(R.id.carCat);
        imgCar = (ImageView) findViewById(R.id.imgCar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CarList.class);
                startActivity(intent);
                finish();
            }
        });

        spinnerBrand = findViewById(R.id.carBrandSpinner);
        // Assuming you have an array of car brands defined in strings.xml
        String[] carBrandsArray = getResources().getStringArray(R.array.car_brands);

        // Adapter for the spinner
        ArrayAdapter<CharSequence> carBrandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carBrandsArray);
        carBrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(carBrandAdapter);

        spinnerCat = findViewById(R.id.carCatSpinner);
        // Assuming you have an array of car brands defined in strings.xml
        String[] carCatsArray = getResources().getStringArray(R.array.car_cats);

        // Adapter for the spinner
        ArrayAdapter<CharSequence> carCatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carCatsArray);
        carCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(carCatAdapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            imgCar.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateCar.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(this).load(bundle.getString("Car Image")).into(imgCar);
            carModel.setText(bundle.getString("Car Model"));

            // carBrand.setText(bundle.getString("Car Brand"));
            String carBrandFromBundle = bundle.getString("Car Brand");
            // Finding the position of the car brand in the spinner's data
            int positionBrand = -1;
            for (int i = 0; i < carBrandsArray.length; i++) {
                if (carBrandsArray[i].equals(carBrandFromBundle)) {
                    positionBrand = i;
                    break;
                }
            }

            // Setting the selection of the spinner to the found position
            if (positionBrand != -1) {
                spinnerBrand.setSelection(positionBrand);
            }

            carPrice.setText(bundle.getString("Car Price"));

            // carCategory.setText(bundle.getString("Car Cat"));
            String carCatFromBundle = bundle.getString("Car Cat");
            // Finding the position of the car brand in the spinner's data
            int positionCat = -1;
            for (int i = 0; i < carCatsArray.length; i++) {
                if (carCatsArray[i].equals(carCatFromBundle)) {
                    positionCat = i;
                    break;
                }
            }

            // Setting the selection of the spinner to the found position
            if (positionCat != -1) {
                spinnerCat.setSelection(positionCat);
            }

            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Car Image");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cars");

        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                updateData();
                // Intent intent = new Intent(UpdateCar.this, CarList.class);
                // startActivity(intent);
            }
        });
    }

    private void handleUpdateResult(boolean success) {
        if (success) {
            // If update is successful, start CarList activity
            Intent intent = new Intent(UpdateCar.this, CarList.class);
            startActivity(intent);
        } else {
            // If update fails, show error message and stay on UpdateCar activity
            Toast.makeText(UpdateCar.this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /* public void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("car images").child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateCar.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No new image selected, update with existing URL
            imageUrl = oldImageURL;
            updateData();
        }
    } */

    public void updateData() {
        model = carModel.getText().toString().trim();
        // brand = carBrand.getText().toString().trim();
        price = carPrice.getText().toString().trim();
        // category = carCategory.getText().toString().trim();

        // Check if any of the fields are empty
        if (model.isEmpty() || price.isEmpty() || spinnerBrand.getSelectedItemPosition() == 0 || spinnerCat.getSelectedItemPosition() == 0) {
            Toast.makeText(UpdateCar.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
            return; // Exit the method early if any field is empty
        }

        DatabaseReference carRef = FirebaseDatabase.getInstance().getReference().child("cars").child(key);

        carRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CarClass existingCar = dataSnapshot.getValue(CarClass.class);

                    // Update only the fields that have changed
                    if (!model.equals(existingCar.getCarModel())) {
                        existingCar.setCarModel(model);
                    }
                    // Update car brand based on the selected value from spinner
                    String selectedBrand = spinnerBrand.getSelectedItem().toString();
                    if (!selectedBrand.equals(existingCar.getCarBrand())) {
                        existingCar.setCarBrand(selectedBrand);
                    }

                    if (!price.equals(existingCar.getCarPrice())) {
                        existingCar.setCarPrice(price);
                    }
                    // Update car cat based on the selected value from spinner
                    String selectedCat = spinnerCat.getSelectedItem().toString();
                    if (!selectedCat.equals(existingCar.getCarCategory())) {
                        existingCar.setCarCategory(selectedCat);
                    }

                    // Update the car image if a new image is selected
                    if (uri != null) {
                        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("car images").child(uri.getLastPathSegment());

                        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> imageTask) {
                                if (imageTask.isSuccessful()) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri imgUri) {
                                            existingCar.setImageURL(imgUri.toString());

                                            // Update the database with the modified car object
                                            carRef.setValue(existingCar).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                                                        reference.delete();
                                                        Toast.makeText(UpdateCar.this, "Updated.", Toast.LENGTH_SHORT).show();
                                                        handleUpdateResult(true); // Pass true to indicate success
                                                        finish();
                                                    } else {
                                                        Toast.makeText(UpdateCar.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        handleUpdateResult(false);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Toast.makeText(UpdateCar.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // No new image selected, update with existing URL
                        carRef.setValue(existingCar).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateCar.this, "Updated.", Toast.LENGTH_SHORT).show();
                                    handleUpdateResult(true); // Pass true to indicate success
                                    finish();
                                } else {
                                    Toast.makeText(UpdateCar.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    handleUpdateResult(false);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateCar.this, "Update cancelled: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}