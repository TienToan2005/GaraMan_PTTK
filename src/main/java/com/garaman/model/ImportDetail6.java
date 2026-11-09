package com.garaman.model;

public class ImportDetail6 {
    private int id;
    private int quantity;
    private float unitPrice;
    private Accessory6 accessory;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }
    public Accessory6 getAccessory() { return accessory; }
    public void setAccessory(Accessory6 accessory) { this.accessory = accessory; }

    /** Thanh tien = so luong x don gia (thuoc tinh dan xuat). */
    public float getAmount() { return quantity * unitPrice; }
}
