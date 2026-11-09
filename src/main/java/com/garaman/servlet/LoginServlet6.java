package com.garaman.servlet;

import com.garaman.dao.MemberDAO6;
import com.garaman.model.Member6;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/** UC Login (dung chung cho khach hang va quan li), UC dang xuat va lay thong tin phien dang nhap. */
@WebServlet(urlPatterns = {"/api/login", "/api/logout", "/api/me"})
public class LoginServlet6 extends HttpServlet {
    private final MemberDAO6 memberDAO = new MemberDAO6();

    private static class LoginForm { String username; String password; }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getServletPath().equals("/api/logout")) {
            HttpSession s = req.getSession(false);
            if (s != null) s.invalidate();
            Api6.json(resp, 200, Map.of("ok", true));
            return;
        }
        LoginForm f = Api6.readBody(req, LoginForm.class);
        if (f == null || f.username == null || f.username.isBlank() || f.password == null || f.password.isEmpty()) {
            Api6.error(resp, 400, "Vui lòng nhập tên đăng nhập và mật khẩu");
            return;
        }
        Member6 m = memberDAO.login(f.username.trim(), f.password);
        if (m == null) {
            Api6.error(resp, 401, "Sai tên đăng nhập hoặc mật khẩu");
            return;
        }
        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();
        req.getSession(true).setAttribute("member", m);
        Api6.json(resp, 200, info(m));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        Object m = s == null ? null : s.getAttribute("member");
        if (m instanceof Member6 mem) Api6.json(resp, 200, info(mem));
        else Api6.error(resp, 401, "Bạn chưa đăng nhập");
    }

    private static Map<String, Object> info(Member6 m) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", m.getId());
        r.put("fullName", m.getFullName());
        r.put("phone", m.getPhone());
        r.put("role", m.getRole());
        return r;
    }
}
