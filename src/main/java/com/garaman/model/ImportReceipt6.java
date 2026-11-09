package com.garaman.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportReceipt6 {
    private int id;
    private LocalDate importDate;
    private String note;
    private Supplier6 supplier;
    private InventoryStaff6 inventoryStaff;
    private List<ImportDetail6> details = new ArrayList<>();
    private int totalQuantity;   // dung cho danh sach cac lan nhap

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return String.format("HDN%03d", id); }
    public LocalDate getImportDate() { return importDate; }
    public void setImportDate(LocalDate importDate) { this.importDate = importDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Supplier6 getSupplier() { return supplier; }
    public void setSupplier(Supplier6 supplier) { this.supplier = supplier; }
    public InventoryStaff6 getInventoryStaff() { return inventoryStaff; }
    public void setInventoryStaff(InventoryStaff6 s) { this.inventoryStaff = s; }
    public List<ImportDetail6> getDetails() { return details; }
    public void setDetails(List<ImportDetail6> details) { this.details = details; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int q) { this.totalQuantity = q; }

    /** Tong tien = tong thanh tien cac dong chi tiet (thuoc tinh dan xuat, khong luu CSDL). */
    public float getTotalAmount() {
        float s = 0;
        for (ImportDetail6 d : details) s += d.getAmount();
        return s;
    }
}
