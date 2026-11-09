package com.garaman.servlet;

import com.garaman.dao.AppointmentDAO6;
import com.garaman.model.Appointment6;
import com.garaman.model.Customer6;
import com.garaman.model.Member6;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Trang xu li DoSaveAppointment6: dong goi Appointment6 va goi AppointmentDAO6.saveAppointment(). */
@WebServlet("/api/appointments")
public class DoSaveAppointment6 extends HttpServlet {
    private static final Pattern PHONE = Pattern.compile("^0\\d{9}$");
    private static final Pattern PLATE = Pattern.compile("^[0-9]{2}[A-Za-z][A-Za-z0-9]?[- ]?[0-9]{3,5}(\\.[0-9]{2})?$");
    private final AppointmentDAO6 appointmentDAO = new AppointmentDAO6();

    private static class Form { String date; String time; String name; String phone; String licensePlate; }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Member6 m = Api6.requireRole(req, resp, "CUSTOMER");
        if (m == null) return;
        Form f = Api6.readBody(req, Form.class);
        if (f == null) {
            Api6.error(resp, 400, "Dữ liệu không hợp lệ");
            return;
        }
        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(f.date);
            time = LocalTime.parse(f.time);
        } catch (DateTimeParseException | NullPointerException e) {
            Api6.error(resp, 400, "Ngày giờ hẹn không hợp lệ");
            return;
        }
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            Api6.error(resp, 400, "Ngày giờ hẹn không hợp lệ (đã qua)");
            return;
        }
        boolean validSlot = false;
        for (int h : SlotsServlet6.HOURS) if (time.equals(LocalTime.of(h, 0))) validSlot = true;
        if (!validSlot) {
            Api6.error(resp, 400, "Khung giờ hẹn không hợp lệ");
            return;
        }
        String name = f.name == null ? "" : f.name.trim();
        String phone = f.phone == null ? "" : f.phone.trim();
        String plate = f.licensePlate == null ? "" : f.licensePlate.trim().toUpperCase();
        if (name.isEmpty() || phone.isEmpty() || plate.isEmpty()) {
            Api6.error(resp, 400, "Vui lòng nhập đủ họ tên, số điện thoại và biển số xe");
            return;
        }
        if (!PHONE.matcher(phone).matches()) {
            Api6.error(resp, 400, "Số điện thoại không đúng định dạng (10 chữ số, bắt đầu bằng 0)");
            return;
        }
        if (!PLATE.matcher(plate).matches()) {
            Api6.error(resp, 400, "Biển số xe không đúng định dạng (ví dụ 30A-123.45)");
            return;
        }
        // dong goi doi tuong thuc the Appointment6
        Appointment6 a = new Appointment6();
        Customer6 c = new Customer6();
        c.setId(m.getId());
        a.setCustomer(c);
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setContactName(name);
        a.setContactPhone(phone);
        a.setLicensePlate(plate);

        if (!appointmentDAO.saveAppointment(a)) {
            Api6.error(resp, 409, "Khung giờ này đã kín, vui lòng chọn khung giờ khác");
            return;
        }
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", a.getId());
        r.put("date", a.getAppointmentDate().toString());
        r.put("time", a.getAppointmentTime().toString());
        r.put("licensePlate", a.getLicensePlate());
        r.put("contactName", a.getContactName());
        r.put("status", a.getStatus());
        Api6.json(resp, 201, r);
    }
}
