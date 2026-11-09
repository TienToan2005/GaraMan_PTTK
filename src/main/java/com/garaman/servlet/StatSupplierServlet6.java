package com.garaman.servlet;

import com.garaman.dao.SupplierStatDAO6;
import com.garaman.model.Member6;
import com.garaman.model.SupplierStat6;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Du lieu cho GDStatSupplier6: thong ke nha cung cap theo luong hang nhap trong khoang ngay. */
@WebServlet("/api/stat/suppliers")
public class StatSupplierServlet6 extends HttpServlet {
    private final SupplierStatDAO6 statDAO = new SupplierStatDAO6();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Member6 m = Api6.requireRole(req, resp, "MANAGER");
        if (m == null) return;
        LocalDate start = Api6.date(req, resp, "start");
        if (start == null) return;
        LocalDate end = Api6.date(req, resp, "end");
        if (end == null) return;
        if (end.isBefore(start)) {
            Api6.error(resp, 400, "Khoảng thời gian không hợp lệ (ngày kết thúc nhỏ hơn ngày bắt đầu)");
            return;
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        for (SupplierStat6 s : statDAO.getSupplierStat(start, end)) {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", s.getId());
            r.put("name", s.getName());
            r.put("address", s.getAddress());
            r.put("phone", s.getPhone());
            r.put("totalQuantity", s.getTotalQuantity());
            rows.add(r);
        }
        Api6.json(resp, 200, rows);
    }
}
