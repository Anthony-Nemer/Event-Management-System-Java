package com.example.eventmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class HostStatusRestockFragment extends Fragment {
    ConnectionClass connectionClass;
    Connection con;
    RecyclerView recyclerView;
    RestockStatusAdapter adapter;
    ArrayList<RequestStatus> restockList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_host_status_restock_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            connectionClass = new ConnectionClass();
            con = connectionClass.CONN();
            if (con != null) {
                fetchStatus();
            } else {
                Log.e("DBConnection", "Connection failed inside Fragment");
            }
        }).start();

        return view;
    }

    public void fetchStatus(){
        try{
            String query = "SELECT rr.id, rr.host_id, rr.supplier_id, rr.cuisine_id, rr.item_name, rr.quantity, rr.status, u.fullname, u.mobilenumber, c.cuisine FROM restock_requests AS rr LEFT JOIN users AS u ON rr.supplier_id = u.id LEFT JOIN cuisines AS c ON RR.cuisine_id = c.id WHERE rr.status = 'accepted';\n";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            ArrayList<RequestStatus> tempList = new ArrayList<>();
            while (rs.next()) {
                tempList.add(new RequestStatus(
                        rs.getInt("id"),
                        rs.getInt("host_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("cuisine_id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getString("fullname"),
                        rs.getString("mobilenumber"),
                        rs.getString("cuisine")
                ));
            }

            getActivity().runOnUiThread(() -> {
                restockList.clear();
                restockList.addAll(tempList);
                adapter = new RestockStatusAdapter(restockList);
                recyclerView.setAdapter(adapter);
            });
        }catch (Exception e) {
            Log.e("DB_ERROR", "Error fetching restock status: " + e.getMessage());
        }
    }
}