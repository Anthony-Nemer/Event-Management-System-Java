package com.example.eventmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RestockStatusAdapter extends RecyclerView.Adapter<RestockStatusAdapter.ViewHolder> {

    private ArrayList<RequestStatus> restockList;

    public RestockStatusAdapter(ArrayList<RequestStatus> restockList) {
        this.restockList = restockList;
    }

    @NonNull
    @Override
    public RestockStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_restock_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestockStatusAdapter.ViewHolder holder, int position) {
        RequestStatus status = restockList.get(position);

        holder.itemName.setText("Item: " + status.getItemName());
        holder.quantity.setText("Quantity: " + status.getQuantity());
        holder.status.setText("Status: " + status.getStatus());
        holder.status.setText("Accepted By: " + status.getSupplierName());
        holder.supplierId.setText("Mobile Number: " + status.getMobileNumber());
        holder.cuisineId.setText("Cuisine: " + status.getCuisineName());
    }

    @Override
    public int getItemCount() {
        return restockList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, quantity, status, supplierId, cuisineId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.quantity);
            status = itemView.findViewById(R.id.status);
            supplierId = itemView.findViewById(R.id.supplier_id);
            cuisineId = itemView.findViewById(R.id.cuisine_id);
        }
    }
}
