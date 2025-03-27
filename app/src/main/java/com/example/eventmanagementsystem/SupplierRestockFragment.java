package com.example.eventmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SupplierRestockFragment extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    int userId, cuisineId;
    RecyclerView recyclerView;
    RestockAdapter adapter;
    ArrayList<RestockRequest> restockList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_supplier_restock_fragment, container, false);

        if (getArguments() != null) {
            userId = getArguments().getInt("userID", 0);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        new Thread(() -> {
            connectionClass = new ConnectionClass();
            con = connectionClass.CONN();
            if (con != null) {
                fetchCuisineId();
            } else {
                Log.e("DBConnection", "Connection failed inside Fragment");
            }
        }).start();


        return view;
    }

    private void fetchCuisineId() {
        try {
            String query = "SELECT cuisineId FROM users WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cuisineId = rs.getInt("cuisineId");
                fetchRestockRequests();
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error fetching cuisineId: " + e.getMessage());
        }
    }

    private void fetchRestockRequests() {
        new Thread(() -> {
            try {
                String query = "SELECT id, item_name, quantity, status FROM restock_requests WHERE cuisine_id = ? AND status = 'pending'";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, cuisineId);
                ResultSet rs = stmt.executeQuery();

                ArrayList<RestockRequest> tempList = new ArrayList<>();
                while (rs.next()) {
                    tempList.add(new RestockRequest(
                            rs.getInt("id"),
                            rs.getString("item_name"),
                            rs.getInt("quantity"),
                            rs.getString("status")
                    ));
                }

                Log.d("DBConnection", "Fetched " + tempList.size() + " restock requests");

                getActivity().runOnUiThread(() -> {
                    restockList.clear();
                    restockList.addAll(tempList);

                    if (adapter == null) {
                        adapter = new RestockAdapter(restockList, this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    recyclerView.invalidate(); // Force UI refresh
                });


            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching restock requests: " + e.getMessage());
            }
        }).start();
    }



    public void acceptRequest(int requestId) {
        new Thread(() -> {
            try {
                if (con == null || con.isClosed()) {
                    Log.d("AcceptRequest", "Connection is closed; attempting to reconnect.");
                    con = connectionClass.CONN();
                }

                if (con != null) {
                    String query = "UPDATE restock_requests SET status = 'accepted', supplier_id = ? WHERE id = ?";
                    Log.d("AcceptRequest", "Executing update: " + query);
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setInt(1, userId);
                    stmt.setInt(2, requestId);

                    Log.d("AcceptRequest", "Query parameters: supplier_id = " + userId + ", request_id = " + requestId);

                    int rowsUpdated = stmt.executeUpdate();
                    Log.d("AcceptRequest", "Rows updated: " + rowsUpdated);

                    if (rowsUpdated > 0) {
                        getActivity().runOnUiThread(() -> {
                            restockList.removeIf(request -> request.getId() == requestId);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Request accepted!", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Log.d("AcceptRequest", "No rows updated. Possible reasons: Invalid ID or status.");
                    }
                } else {
                    Log.e("DB_ERROR", "Connection is null. Cannot execute update.");
                }
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error executing update: " + e.getMessage(), e);
            }
        }).start();
    }





}
