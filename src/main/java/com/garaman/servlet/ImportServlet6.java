package com.garaman.servlet;

import com.garaman.dao.ImportReceiptDAO6;
import com.garaman.model.ImportDetail6;
import com.garaman.model.ImportReceipt6;
import com.garaman.model.Member6;
import com.garaman.model.Supplier6;
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

/** Du lieu cho GDImportList6 (cac lan nhap cua nha cung cap) va GDImportDetail6 (hoa don nhap chi tiet). */
@WebServlet(urlPatterns = {"/api/stat/imports", "/api/stat/imports/detail"})
public class ImportServlet6 extends HttpServlet {
    private final ImportReceiptDAO6 receiptDAO = new ImportReceiptDAO6();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Member6 m = Api6.requireRole(req, resp, "MANAGER");
        if (m == null) return;
        try {
            if (req.getServletPath().endsWith("/detail")) detail(req, resp);
            else list(req, resp);
        } catch (NumberFormatException e) {
            Api6.error(resp, 400, "Mã không hợp lệ");
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int supplierId = Integer.parseInt(req.getParameter("supplierId"));
        LocalDate start = Api6.date(req, resp, "start");
        if (start == null) return;
        LocalDate end = Api6.date(req, resp, "end");
        if (end == null) return;
        Supplier6 s = receiptDAO.getSupplier(supplierId);
        if (s == null) {
            Api6.error(resp, 404, "Không tìm thấy nhà cung cấp");
            return;
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        for (ImportReceipt6 r : receiptDAO.getImportList(supplierId, start, end)) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", r.getId());
            row.put("code", r.getCode());
            row.put("importDate", r.getImportDate().toString());
            row.put("staffName", r.getInventoryStaff().getFullName());
            row.put("totalQuantity", r.getTotalQuantity());
            rows.add(row);
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("supplier", Map.of("id", s.getId(), "name", s.getName()));
        res.put("receipts", rows);
        Api6.json(resp, 200, res);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ImportReceipt6 r = receiptDAO.getImportDetail(id);
        if (r == null) {
            Api6.error(resp, 404, "Không tìm thấy hóa đơn nhập");
            return;
        }
        List<Map<String, Object>> lines = new ArrayList<>();
        int stt = 0;
        for (ImportDetail6 d : r.getDetails()) {
            Map<String, Object> l = new LinkedHashMap<>();
            l.put("stt", ++stt);
            l.put("accessoryId", d.getAccessory().getId());
            l.put("accessoryName", d.getAccessory().getName());
            l.put("quantity", d.getQuantity());
            l.put("unitPrice", d.getUnitPrice());
            l.put("amount", d.getAmount());
            lines.add(l);
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("id", r.getId());
        res.put("code", r.getCode());
        res.put("importDate", r.getImportDate().toString());
        res.put("note", r.getNote());
        res.put("supplierName", r.getSupplier().getName());
        res.put("supplierId", r.getSupplier().getId());
        res.put("staffName", r.getInventoryStaff().getFullName());
        res.put("lines", lines);
        res.put("totalAmount", r.getTotalAmount());
        Api6.json(resp, 200, res);
    }
}
