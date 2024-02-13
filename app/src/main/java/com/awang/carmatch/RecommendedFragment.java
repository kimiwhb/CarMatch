package com.awang.carmatch;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendedFragment extends Fragment {

    RecyclerView recyclerView;
    List<CarClass> carList;
    CarAdapter adapter;
    DatabaseReference databaseReference, databaseUser;
    ValueEventListener eventListener;
    EditText minRange;
    TextView maxRange;
    ImageView searchRange, clear;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecommendedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecommendedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendedFragment newInstance(String param1, String param2) {
        RecommendedFragment fragment = new RecommendedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recommended, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        maxRange = rootView.findViewById(R.id.maxRange);
        minRange = rootView.findViewById(R.id.minRange);
        searchRange = rootView.findViewById(R.id.searchRange);
        clear = rootView.findViewById(R.id.clear);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        carList = new ArrayList<>();
        adapter = new CarAdapter(getActivity(), carList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cars");
        databaseUser = FirebaseDatabase.getInstance().getReference().child("users");

        retrieveDataFromDatabase();

        // String minRangeTxt = minRange.getText().toString();

        /* databaseUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("budget")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userBudget = snapshot.getValue(String.class);
                // Log.d("User Budget", userBudget);

                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carList.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            try {
                                CarClass carClass = itemSnapshot.getValue(CarClass.class);
                                if (carClass != null && userBudget != null) {
                                    maxRange.setText(userBudget);

                                    int userBudgetInt = Integer.parseInt(userBudget);
                                    int carPriceInt = Integer.parseInt(carClass.getCarPrice());

                                    if (carPriceInt < userBudgetInt) {
                                        carClass.setKey(itemSnapshot.getKey());
                                        carList.add(carClass);
                                    }
                                } else {
                                    // Log or handle the case where conversion to CarClass is unsuccessful
                                    Log.e("CarList", "Failed to convert to CarClass: " + itemSnapshot.toString());
                                }
                            } catch (DatabaseException e) {
                                // Log or handle the exception
                                Log.e("CarList", "Exception: " + e.getMessage());
                            }

                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle the error
                Log.e("CarList", "DatabaseError: " + error.getMessage());
            }
        }); */

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minRange.setText("");
                retrieveDataFromDatabase();
            }
        });

        searchRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minRangeTxt = minRange.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                databaseUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("budget")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String userBudget = snapshot.getValue(String.class);
                                // Log.d("User Budget", userBudget);
                                maxRange.setText(userBudget);

                                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        carList.clear();
                                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                            try {
                                                CarClass carClass = itemSnapshot.getValue(CarClass.class);
                                                if (!minRangeTxt.isEmpty()) {
                                                    int minRangeInt = Integer.parseInt(minRangeTxt);
                                                    int maxRangeInt = Integer.parseInt(userBudget);



                                                    int carPriceInt = Integer.parseInt(carClass.getCarPrice());

                                                    if (carPriceInt >= minRangeInt && carPriceInt <= maxRangeInt) {
                                                        carClass.setKey(itemSnapshot.getKey());
                                                        carList.add(carClass);
                                                    }
                                                } else {
                                                    // Log or handle the case where conversion to CarClass is unsuccessful
                                                    Toast.makeText(getActivity(), "Enter min. amount for personalized list of cars.", Toast.LENGTH_SHORT).show();
                                                    Log.e("Search", "Min range is empty.");
                                                    retrieveDataFromDatabase();
                                                }
                                            } catch (DatabaseException e) {
                                                // Log or handle the exception
                                                Log.e("CarList", "Exception: " + e.getMessage());
                                            }

                                            adapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Log or handle the error
                                Log.e("CarList", "DatabaseError: " + error.getMessage());
                            }
                        });
            }
        });

        return rootView;
    }

    private void retrieveDataFromDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        databaseUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("budget")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userBudget = snapshot.getValue(String.class);
                        // Log.d("User Budget", userBudget);

                        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                carList.clear();
                                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                    try {
                                        CarClass carClass = itemSnapshot.getValue(CarClass.class);
                                        if (carClass != null && userBudget != null) {
                                            maxRange.setText(userBudget);

                                            int userBudgetInt = Integer.parseInt(userBudget);
                                            int carPriceInt = Integer.parseInt(carClass.getCarPrice());

                                            if (carPriceInt < userBudgetInt) {
                                                carClass.setKey(itemSnapshot.getKey());
                                                carList.add(carClass);
                                            }
                                        } else {
                                            // Log or handle the case where conversion to CarClass is unsuccessful
                                            Log.e("CarList", "Failed to convert to CarClass: " + itemSnapshot.toString());
                                        }
                                    } catch (DatabaseException e) {
                                        // Log or handle the exception
                                        Log.e("CarList", "Exception: " + e.getMessage());
                                    }

                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Log or handle the error
                        Log.e("CarList", "DatabaseError: " + error.getMessage());
                    }
                });
    }

}