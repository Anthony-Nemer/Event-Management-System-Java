package com.example.eventmanagementsystem;

public class RequestStatus {
    private int id;
    private int hostId;
    private int supplierId;
    private int cuisineId;
    private String itemName;
    private int quantity;
    private String status;
    private String supplierName;
    private String mobileNumber;
    private String cuisineName;

    public RequestStatus(int id, int hostId, int supplierId, int cuisineId, String itemName, int quantity, String status, String fullname, String mobileNumber, String cuisineName) {
        this.id = id;
        this.hostId = hostId;
        this.supplierId = supplierId;
        this.cuisineId = cuisineId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.status = status;
        this.supplierName = fullname;
        this.mobileNumber = mobileNumber;
        this.cuisineName = cuisineName;
    }

    public int getId() {
        return id;
    }

    public int getHostId() {
        return hostId;
    }

    public int getSupplierId(){
        return supplierId;
    }
    public int getCuisineId() {
        return cuisineId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }
    public String getStatus(){
        return status;
    }

    public String getSupplierName() {return supplierName;}

    public String getMobileNumber() {return mobileNumber;}

    public String getCuisineName() {return cuisineName;}
}
