package com.awang.carmatch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> {
    private Context context;
    private List<CarClass> carList;
    public CarAdapter(Context context, List<CarClass> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_carlist, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Glide.with(context).load(carList.get(position).getImageURL()).into(holder.carImg);
        holder.carModel.setText(carList.get(position).getCarModel());
        holder.carCat.setText(carList.get(position).getCarCategory());
        holder.carPrice.setText("RM" + carList.get(position).getCarPrice());

        final int currentPosition = holder.getAdapterPosition();

        holder.carList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarDetail.class);
                intent.putExtra("Key",carList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Car Image", carList.get(holder.getAdapterPosition()).getImageURL());
                intent.putExtra("Car Model", carList.get(holder.getAdapterPosition()).getCarModel());
                intent.putExtra("Car Cat", carList.get(holder.getAdapterPosition()).getCarCategory());
                intent.putExtra("Car Price", carList.get(holder.getAdapterPosition()).getCarPrice());

                intent.putExtra("Car Brand", carList.get(currentPosition).getCarBrand());

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return carList.size();
    }
}

class CarViewHolder extends RecyclerView.ViewHolder {
    ImageView carImg;
    TextView carModel, carCat, carPrice;
    CardView carList;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);
        carImg = itemView.findViewById(R.id.carImg);
        carList = itemView.findViewById(R.id.carList);
        carModel = itemView.findViewById(R.id.carModel);
        carCat = itemView.findViewById(R.id.carCat);
        carPrice = itemView.findViewById(R.id.carPrice);
    }
}
