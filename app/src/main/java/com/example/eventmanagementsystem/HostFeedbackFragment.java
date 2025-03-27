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

public class HostFeedbackFragment extends Fragment {

    ConnectionClass connectionClass;
    Connection con;
    RecyclerView recyclerView;
    FeedbackAdapter adapter;
    ArrayList<ClientFeedback> feedbackList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_host_feedback_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            connectionClass = new ConnectionClass();
            con = connectionClass.CONN();
            if (con != null) {
                fetchFeedback();
            } else {
                Log.e("DBConnection", "Connection failed inside Fragment");
            }
        }).start();

        return view;
    }

    private void fetchFeedback() {
        try {
            String query = "SELECT id, user_id, feedback, rating, services, suggestions FROM feedback";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            ArrayList<ClientFeedback> tempList = new ArrayList<>();
            while (rs.next()) {
                tempList.add(new ClientFeedback(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("feedback"),
                        rs.getInt("rating"),
                        rs.getString("services"),
                        rs.getString("suggestions")
                ));
            }

            Log.d("DBConnection", "Fetched " + tempList.size() + " feedback entries");

            getActivity().runOnUiThread(() -> {
                feedbackList.clear();
                feedbackList.addAll(tempList);
                adapter = new FeedbackAdapter(feedbackList);
                recyclerView.setAdapter(adapter);
            });

        } catch (Exception e) {
            Log.e("DB_ERROR", "Error fetching feedback: " + e.getMessage());
        }
    }
}