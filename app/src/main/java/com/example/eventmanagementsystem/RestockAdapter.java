package com.example.eventmanagementsystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RestockAdapter extends RecyclerView.Adapter<RestockAdapter.ViewHolder> {

    private ArrayList<RestockRequest> restockList;
    private SupplierRestockFragment fragment;

    public RestockAdapter(ArrayList<RestockRequest> restockList, SupplierRestockFragment fragment) {
        this.restockList = restockList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_restock_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestockRequest request = restockList.get(position);


        holder.itemName.setText(request.getItemName());
        holder.quantity.setText("Qty: " + request.getQuantity());
        holder.status.setText("Status: " + request.getStatus());

        holder.acceptButton.setOnClickListener(v -> {
            fragment.acceptRequest(request.getId());
        });

    }



    @Override
    public int getItemCount() {
        return restockList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, quantity, status;
        Button acceptButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.quantity);
            status = itemView.findViewById(R.id.status);
            acceptButton = itemView.findViewById(R.id.accept_button);
        }
    }
}
