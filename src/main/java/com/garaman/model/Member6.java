package com.garaman.model;

/** Thanh vien he thong (lop truu tuong, cha cua Customer6 va Staff6). */
public abstract class Member6 {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    /** Vai tro dung de phan quyen: CUSTOMER, MANAGER, INVENTORY. */
    public abstract String getRole();
}
