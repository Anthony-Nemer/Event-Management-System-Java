package com.example.eventmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private ArrayList<ClientFeedback> feedbackList;

    public FeedbackAdapter(ArrayList<ClientFeedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientFeedback feedback = feedbackList.get(position);
        holder.feedbackText.setText("Feedback: " + feedback.getFeedbackText());
        holder.rating.setText("Rating: " + feedback.getRating() + "★");
        holder.services.setText("Services: " + feedback.getServices());
        holder.suggestions.setText("Suggestions: " + feedback.getSuggestions());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView feedbackText, rating, services, suggestions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackText = itemView.findViewById(R.id.feedback_text);
            rating = itemView.findViewById(R.id.rating);
            services = itemView.findViewById(R.id.services);
            suggestions = itemView.findViewById(R.id.suggestions);
        }
    }
}