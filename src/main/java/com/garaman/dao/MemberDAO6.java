package com.garaman.dao;

import com.garaman.model.Customer6;
import com.garaman.model.InventoryStaff6;
import com.garaman.model.Manager6;
import com.garaman.model.Member6;
import com.garaman.model.Staff6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Dang nhap (UC Login): kiem tra tai khoan, xac dinh vai tro. */
public class MemberDAO6 extends DAO6 {
    private static final String SQL =
        "SELECT m.id, m.username, m.fullName, m.phone, m.email, m.address, s.position," +
        " (c.tblMember6id IS NOT NULL) AS isCustomer, (mg.tblMember6id IS NOT NULL) AS isManager," +
        " (i.tblMember6id IS NOT NULL) AS isInventory " +
        "FROM tblMember6 m " +
        "LEFT JOIN tblCustomer6 c ON c.tblMember6id = m.id " +
        "LEFT JOIN tblStaff6 s ON s.tblMember6id = m.id " +
        "LEFT JOIN tblManager6 mg ON mg.tblMember6id = m.id " +
        "LEFT JOIN tblInventoryStaff6 i ON i.tblMember6id = m.id " +
        "WHERE m.username = ? AND m.password = ?";

    /** @return thanh vien neu dung tai khoan/mat khau, nguoc lai null. */
    public Member6 login(String username, String password) {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, username);
            ps.setString(2, sha256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Member6 m;
                if (rs.getBoolean("isManager")) m = new Manager6();
                else if (rs.getBoolean("isInventory")) m = new InventoryStaff6();
                else if (rs.getBoolean("isCustomer")) m = new Customer6();
                else return null;
                m.setId(rs.getInt("id"));
                m.setUsername(rs.getString("username"));
                m.setFullName(rs.getString("fullName"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setAddress(rs.getString("address"));
                if (m instanceof Staff6) ((Staff6) m).setPosition(rs.getString("position"));
                return m;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
