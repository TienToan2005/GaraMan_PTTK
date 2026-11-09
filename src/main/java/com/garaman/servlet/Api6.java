package com.garaman.servlet;

import com.garaman.model.Member6;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/** Tien ich dung chung cho cac servlet: doc/ghi JSON, kiem tra dang nhap va vai tro. */
final class Api6 {
    static final Gson GSON = new GsonBuilder().create();

    private Api6() { }

    static void json(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(GSON.toJson(body));
    }

    static void error(HttpServletResponse resp, int status, String message) throws IOException {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("error", message);
        json(resp, status, m);
    }

    static <T> T readBody(HttpServletRequest req, Class<T> type) throws IOException {
        req.setCharacterEncoding("UTF-8");
        return GSON.fromJson(req.getReader(), type);
    }

    /** Tra ve thanh vien dang nhap neu co dung vai tro; nguoc lai gui loi 401/403 va tra ve null. */
    static Member6 requireRole(HttpServletRequest req, HttpServletResponse resp, String role) throws IOException {
        Object o = req.getSession(false) == null ? null : req.getSession(false).getAttribute("member");
        if (!(o instanceof Member6 m)) {
            error(resp, 401, "Bạn chưa đăng nhập");
            return null;
        }
        if (!role.equals(m.getRole())) {
            error(resp, 403, "Bạn không có quyền sử dụng chức năng này");
            return null;
        }
        return m;
    }

    /** Doc ngay dang yyyy-MM-dd; tra ve null va gui loi 400 neu sai dinh dang. */
    static LocalDate date(HttpServletRequest req, HttpServletResponse resp, String param) throws IOException {
        String v = req.getParameter(param);
        try {
            return LocalDate.parse(v);
        } catch (DateTimeParseException | NullPointerException e) {
            error(resp, 400, "Ngày không hợp lệ (" + param + ")");
            return null;
        }
    }
}
