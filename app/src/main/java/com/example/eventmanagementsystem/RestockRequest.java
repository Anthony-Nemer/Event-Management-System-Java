package com.example.eventmanagementsystem;

public class RestockRequest {
    private int id;
    private String itemName;
    private int quantity;
    private String status;

    public RestockRequest(int id, String itemName, int quantity, String status) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
