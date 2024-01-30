package com.awang.carmatch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class DealerAdapter extends RecyclerView.Adapter<DealerViewHolder> {
    private Context context;
    private List<Dealer> dealerList;
    public DealerAdapter(Context context, List<Dealer> dealerList) {
        this.context = context;
        this.dealerList = dealerList;
    }

    @NonNull
    @Override
    public DealerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dealerlist, parent, false);
        return new DealerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerViewHolder holder, int position) {
        holder.dealerName.setText(dealerList.get(position).getName());

        final int currentPosition = holder.getAdapterPosition();

        holder.dealerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapDealership.class);
                intent.putExtra("Key",dealerList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("Dealer Name", dealerList.get(currentPosition).getName());

                intent.putExtra("Latitude", dealerList.get(currentPosition).getLatitude());
                intent.putExtra("Longitude", dealerList.get(currentPosition).getLongitude());

                Log.d("Latitude", dealerList.get(currentPosition).getLatitude());
                Log.d("Longitude", dealerList.get(currentPosition).getLongitude());

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dealerList.size();
    }
}

class DealerViewHolder extends RecyclerView.ViewHolder {
    TextView dealerName;
    CardView dealerList;

    public DealerViewHolder(@NonNull View itemView) {
        super(itemView);
        dealerName = itemView.findViewById(R.id.dealerName);
        dealerList = itemView.findViewById(R.id.dealerList);
    }
}
