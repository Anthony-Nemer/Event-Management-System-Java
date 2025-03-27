package com.example.eventmanagementsystem;

public class ClientFeedback {
    private int id;
    private int userId;
    private String feedbackText;
    private int rating;
    private String services;
    private String suggestions;

    public ClientFeedback(int id, int userId, String feedbackText, int rating, String services, String suggestions) {
        this.id = id;
        this.userId = userId;
        this.feedbackText = feedbackText;
        this.rating = rating;
        this.services = services;
        this.suggestions = suggestions;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }
    public int getRating() {
        return rating;
    }
    public String getServices() {
        return services;
    }
    public String getSuggestions() {
        return suggestions;
    }
}