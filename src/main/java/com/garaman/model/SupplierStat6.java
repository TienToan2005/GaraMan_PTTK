package com.garaman.model;

/** Lop thong ke: ke thua Supplier6, them thuoc tinh dan xuat totalQuantity (khong co bang rieng trong CSDL). */
public class SupplierStat6 extends Supplier6 {
    private int totalQuantity;
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
}
