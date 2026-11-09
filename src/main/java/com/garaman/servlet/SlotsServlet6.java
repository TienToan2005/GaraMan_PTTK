package com.garaman.servlet;

import com.garaman.dao.AppointmentDAO6;
import com.garaman.model.Member6;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Du lieu cho giao dien GDSelectTime6: cac khung gio hen trong ngay va khung nao con trong. */
@WebServlet("/api/slots")
public class SlotsServlet6 extends HttpServlet {
    /** Khung gio lam viec cua gara (moi khung 1 gio). */
    static final int[] HOURS = {8, 9, 10, 11, 13, 14, 15, 16};
    private final AppointmentDAO6 appointmentDAO = new AppointmentDAO6();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Member6 m = Api6.requireRole(req, resp, "CUSTOMER");
        if (m == null) return;
        LocalDate date = Api6.date(req, resp, "date");
        if (date == null) return;
        if (date.isBefore(LocalDate.now())) {
            Api6.error(resp, 400, "Ngày hẹn đã qua, vui lòng chọn ngày khác");
            return;
        }
        List<LocalTime> booked = appointmentDAO.getBookedTimes(date);
        List<Map<String, Object>> slots = new ArrayList<>();
        for (int h : HOURS) {
            LocalTime t = LocalTime.of(h, 0);
            boolean past = date.equals(LocalDate.now()) && LocalDateTime.of(date, t).isBefore(LocalDateTime.now());
            Map<String, Object> s = new LinkedHashMap<>();
            s.put("time", t.toString());
            s.put("available", !booked.contains(t) && !past);
            slots.add(s);
        }
        Api6.json(resp, 200, slots);
    }
}
