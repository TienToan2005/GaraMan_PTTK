package com.garaman.dao;

import com.garaman.model.SupplierStat6;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Thong ke nha cung cap theo luong hang nhap (module 2). totalQuantity duoc tinh tu tblImportDetail6. */
public class SupplierStatDAO6 extends DAO6 {
    public List<SupplierStat6> getSupplierStat(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT s.id, s.name, s.address, s.phone, SUM(d.quantity) AS total " +
                     "FROM tblSupplier6 s " +
                     "JOIN tblImportReceipt6 r ON r.tblSupplier6id = s.id " +
                     "JOIN tblImportDetail6 d ON d.tblImportReceipt6id = r.id " +
                     "WHERE r.importDate BETWEEN ? AND ? " +
                     "GROUP BY s.id, s.name, s.address, s.phone " +
                     "ORDER BY total DESC, s.name";
        List<SupplierStat6> res = new ArrayList<>();
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SupplierStat6 s = new SupplierStat6();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setAddress(rs.getString("address"));
                    s.setPhone(rs.getString("phone"));
                    s.setTotalQuantity(rs.getInt("total"));
                    res.add(s);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return res;
    }
}
