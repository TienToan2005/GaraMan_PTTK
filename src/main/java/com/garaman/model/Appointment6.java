package com.garaman.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment6 {
    private int id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String licensePlate;
    private String contactName;
    private String contactPhone;
    private String status;
    private Customer6 customer;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate d) { this.appointmentDate = d; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime t) { this.appointmentTime = t; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String s) { this.licensePlate = s; }
    public String getContactName() { return contactName; }
    public void setContactName(String s) { this.contactName = s; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String s) { this.contactPhone = s; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public Customer6 getCustomer() { return customer; }
    public void setCustomer(Customer6 c) { this.customer = c; }
}
