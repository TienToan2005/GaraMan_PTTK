package com.garaman.dao;

import com.garaman.model.Appointment6;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Truy xuat du lieu lich hen (module 1). */
public class AppointmentDAO6 extends DAO6 {
    private static final Object LOCK = new Object();
    public static final String STATUS_NEW = "Chờ tiếp nhận";
    public static final String STATUS_CANCELLED = "Đã hủy";

    /** Cac khung gio da co nguoi dat trong ngay (de giao dien GDSelectTime6 hien khung gio da kin). */
    public List<LocalTime> getBookedTimes(LocalDate date) {
        List<LocalTime> res = new ArrayList<>();
        String sql = "SELECT appointmentTime FROM tblAppointment6 WHERE appointmentDate = ? AND status <> ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, STATUS_CANCELLED);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(rs.getTime(1).toLocalTime());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return res;
    }

    /**
     * Luu lich hen: kiem tra khung gio con trong roi ghi vao tblAppointment6.
     * @return true neu luu thanh cong; false neu khung gio da co nguoi dat.
     */
    public boolean saveAppointment(Appointment6 a) {
        synchronized (LOCK) {
            if (getBookedTimes(a.getAppointmentDate()).contains(a.getAppointmentTime())) return false;
            String sql = "INSERT INTO tblAppointment6 (appointmentDate, appointmentTime, licensePlate, contactName," +
                         " contactPhone, status, tblCustomer6id) VALUES (?,?,?,?,?,?,?)";
            try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(a.getAppointmentDate()));
                ps.setTime(2, Time.valueOf(a.getAppointmentTime()));
                ps.setString(3, a.getLicensePlate());
                ps.setString(4, a.getContactName());
                ps.setString(5, a.getContactPhone());
                ps.setString(6, STATUS_NEW);
                ps.setInt(7, a.getCustomer().getId());
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) a.setId(k.getInt(1));
                }
                a.setStatus(STATUS_NEW);
                return true;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
