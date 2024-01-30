package com.awang.carmatch;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class UpdateCar extends AppCompatActivity {

    Button updateButton;
    TextView carModel, carBrand, carPrice, carCategory;
    ImageView imgCar;
    Uri uri;
    // String imageUrl;
    String key, oldImageURL;
    DatabaseReference databaseReference;
    // StorageReference storageReference;
    String model, brand, price, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        updateButton = (Button) findViewById(R.id.updateButton);
        carModel = (TextView) findViewById(R.id.carModel);
        carBrand = (TextView) findViewById(R.id.carBrand);
        carPrice = (TextView) findViewById(R.id.carPrice);
        carCategory = (TextView) findViewById(R.id.carCat);
        imgCar = (ImageView) findViewById(R.id.imgCar);

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
            carBrand.setText(bundle.getString("Car Brand"));
            carPrice.setText(bundle.getString("Car Price"));
            carCategory.setText(bundle.getString("Car Cat"));
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
                Intent intent = new Intent(UpdateCar.this, CarList.class);
                startActivity(intent);
            }
        });
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
        brand = carBrand.getText().toString().trim();
        price = carPrice.getText().toString().trim();
        category = carCategory.getText().toString().trim();

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
                    if (!brand.equals(existingCar.getCarBrand())) {
                        existingCar.setCarBrand(brand);
                    }
                    if (!price.equals(existingCar.getCarPrice())) {
                        existingCar.setCarPrice(price);
                    }
                    if (!category.equals(existingCar.getCarCategory())) {
                        existingCar.setCarCategory(category);
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
                                                        finish();
                                                    } else {
                                                        Toast.makeText(UpdateCar.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                                    finish();
                                } else {
                                    Toast.makeText(UpdateCar.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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