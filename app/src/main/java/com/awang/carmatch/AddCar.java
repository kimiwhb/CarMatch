package com.awang.carmatch;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCar extends AppCompatActivity {

    EditText carModel, carPrice, intF1, intF2, intF3, extF1, extF2, extF3, SF1, SF2, SF3;
    ImageView btnBack, imgCar;
    Spinner carBrandSpinner, carCatSpinner;
    Button btnAddCar;
    Uri uri;
    // DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://carmatchfyp-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnAddCar = (Button) findViewById(R.id.btnAddCar);
        imgCar = (ImageView) findViewById(R.id.imgCar);
        // btnSelectImage = findViewById(R.id.btnSelectImage);
        carBrandSpinner = (Spinner) findViewById(R.id.carBrandSpinner);
        carCatSpinner = (Spinner) findViewById(R.id.carCatSpinner);
        carModel = (EditText) findViewById(R.id.carModel);
        carPrice = (EditText) findViewById(R.id.carPrice);
        intF1 = (EditText) findViewById(R.id.intF1);
        intF2 = (EditText) findViewById(R.id.intF2);
        intF3 = (EditText) findViewById(R.id.intF3);
        extF1 = (EditText) findViewById(R.id.extF1);
        extF2 = (EditText) findViewById(R.id.extF2);
        extF3 = (EditText) findViewById(R.id.extF3);
        SF1 = (EditText) findViewById(R.id.SF1);
        SF2 = (EditText) findViewById(R.id.SF2);
        SF3 = (EditText) findViewById(R.id.SF3);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageCar.class);
                startActivity(intent);
                finish();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            imgCar.setImageURI(uri);
                        } else {
                            Toast.makeText(AddCar.this, "No Image Selected.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        ArrayAdapter<CharSequence> carBrandAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.car_brands)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set the hint color
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Specify the layout to use when the list of choices appears
        carBrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        carBrandSpinner.setAdapter(carBrandAdapter);

        ArrayAdapter<CharSequence> carCatAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.car_cats)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set the hint color
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Specify the layout to use when the list of choices appears
        carCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        carCatSpinner.setAdapter(carCatAdapter);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    /*public void saveData() {
        if (areAllFieldsFilled()) {
            if (uri != null) {

                String carID = databaseReference.child("cars").push().getKey();
                String carModelTxt = carModel.getText().toString();
                String carBrand = carBrandSpinner.getSelectedItem().toString();
                String carCategory = carCatSpinner.getSelectedItem().toString();
                String carPriceTxt = carPrice.getText().toString();

                StorageReference imageRef = storageReference.child("car images").child(carID + ".jpg");

                imageRef.putFile(uri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        imageRef.getDownloadUrl().addOnSuccessListener(imguri -> {
                            imageURL = imguri.toString();
                            Log.d("Car Image: ", imageURL);

                            CarClass carClass = new CarClass(carModelTxt, carBrand, carCategory, carPriceTxt, imageURL);

                            databaseReference.child("cars").child(carID)
                                    .setValue(carClass)
                                    .addOnCompleteListener(uploadTask -> {
                                        if (uploadTask.isSuccessful()) {
                                            Toast.makeText(AddCar.this, "Car Details Added.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(AddCar.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });


                        });
                    } else {
                        Toast.makeText(AddCar.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(AddCar.this, "Please select an image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddCar.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void saveData() {
        if (areAllFieldsFilled()) {
            if (uri != null) {
                String carID = databaseReference.child("cars").push().getKey();
                String carModelTxt = carModel.getText().toString();
                String carBrand = carBrandSpinner.getSelectedItem().toString();
                String carCategory = carCatSpinner.getSelectedItem().toString();
                String carPriceTxt = carPrice.getText().toString();

                StorageReference imageRef = storageReference.child("car images").child(carID + ".jpg");

                try {
                    imageRef.putFile(uri)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    imageRef.getDownloadUrl()
                                            .addOnSuccessListener(urlImage -> {
                                                String imageURL = urlImage.toString();
                                                Log.d("Car Image ", imageURL);

                                                CarClass carClass = new CarClass(carModelTxt, carBrand, carCategory, carPriceTxt, imageURL);

                                                databaseReference.child("cars").child(carID)
                                                        .setValue(carClass)
                                                        .addOnCompleteListener(uploadTask -> {
                                                            if (uploadTask.isSuccessful()) {
                                                                Toast.makeText(AddCar.this, "Car Details Added.", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), ManageCar.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("Firebase", "Error uploading data to database: " + e.getMessage());
                                                            Toast.makeText(AddCar.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("Firebase", "Error getting download URL: " + e.getMessage());
                                                Toast.makeText(AddCar.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Log.e("Firebase", "Error uploading image: " + task.getException().getMessage());
                                    Toast.makeText(AddCar.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    Log.e("Firebase", "Exception during image upload: " + e.getMessage());
                    Toast.makeText(AddCar.this, "Exception during image upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddCar.this, "Please select an image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddCar.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean areAllFieldsFilled() {

        int selectedBrandPosition = carBrandSpinner.getSelectedItemPosition();
        int selectedCategoryPosition = carCatSpinner.getSelectedItemPosition();

        return  !carModel.getText().toString().isEmpty() &&
                selectedBrandPosition != 0  &&
                selectedCategoryPosition != 0 &&
                !carPrice.getText().toString().isEmpty();
    }

    /*public void uploadData() {
        String carModelTxt = carModel.getText().toString();
        String carBrand = carBrandSpinner.getSelectedItem().toString();
        String carCategory = carCatSpinner.getSelectedItem().toString();
        String carPriceTxt = carPrice.getText().toString();

        CarClass carClass = new CarClass(carModelTxt, carBrand, carCategory, carPriceTxt);

        String carID = databaseReference.child("cars").push().getKey();

        databaseReference.child("cars").child(carID)
                .setValue(carClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddCar.this, "Car Details Added.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCar.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }*/

}