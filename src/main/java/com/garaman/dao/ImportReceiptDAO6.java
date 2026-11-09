package com.garaman.dao;

import com.garaman.model.Accessory6;
import com.garaman.model.ImportDetail6;
import com.garaman.model.ImportReceipt6;
import com.garaman.model.InventoryStaff6;
import com.garaman.model.Supplier6;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Truy xuat hoa don nhap (module 2): danh sach cac lan nhap cua nha cung cap va chi tiet mot hoa don. */
public class ImportReceiptDAO6 extends DAO6 {

    public Supplier6 getSupplier(int supplierId) {
        String sql = "SELECT id, name, address, phone FROM tblSupplier6 WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return readSupplier(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Cac lan nhap cua mot nha cung cap trong khoang ngay (kem nguoi lap va tong so luong). */
    public List<ImportReceipt6> getImportList(int supplierId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT r.id, r.importDate, r.note, m.id AS staffId, m.fullName AS staffName, SUM(d.quantity) AS qty " +
                     "FROM tblImportReceipt6 r " +
                     "JOIN tblMember6 m ON m.id = r.tblInventoryStaff6id " +
                     "JOIN tblImportDetail6 d ON d.tblImportReceipt6id = r.id " +
                     "WHERE r.tblSupplier6id = ? AND r.importDate BETWEEN ? AND ? " +
                     "GROUP BY r.id, r.importDate, r.note, m.id, m.fullName " +
                     "ORDER BY r.importDate, r.id";
        List<ImportReceipt6> res = new ArrayList<>();
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt6 r = new ImportReceipt6();
                    r.setId(rs.getInt("id"));
                    r.setImportDate(rs.getDate("importDate").toLocalDate());
                    r.setNote(rs.getString("note"));
                    InventoryStaff6 s = new InventoryStaff6();
                    s.setId(rs.getInt("staffId"));
                    s.setFullName(rs.getString("staffName"));
                    r.setInventoryStaff(s);
                    r.setTotalQuantity(rs.getInt("qty"));
                    res.add(r);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return res;
    }

    /** Hoa don nhap day du: thong tin chung, nha cung cap, nguoi lap va cac dong chi tiet. */
    public ImportReceipt6 getImportDetail(int receiptId) {
        String head = "SELECT r.id, r.importDate, r.note, s.id AS sid, s.name AS sname, s.address AS saddr, s.phone AS sphone, " +
                      "m.id AS staffId, m.fullName AS staffName " +
                      "FROM tblImportReceipt6 r JOIN tblSupplier6 s ON s.id = r.tblSupplier6id " +
                      "JOIN tblMember6 m ON m.id = r.tblInventoryStaff6id WHERE r.id = ?";
        String lines = "SELECT d.id, d.quantity, d.unitPrice, a.id AS aid, a.name AS aname, a.description AS adesc " +
                       "FROM tblImportDetail6 d JOIN tblAccessory6 a ON a.id = d.tblAccessory6id " +
                       "WHERE d.tblImportReceipt6id = ? ORDER BY d.id";
        try (Connection con = getConnection()) {
            ImportReceipt6 r;
            try (PreparedStatement ps = con.prepareStatement(head)) {
                ps.setInt(1, receiptId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return null;
                    r = new ImportReceipt6();
                    r.setId(rs.getInt("id"));
                    r.setImportDate(rs.getDate("importDate").toLocalDate());
                    r.setNote(rs.getString("note"));
                    Supplier6 s = new Supplier6();
                    s.setId(rs.getInt("sid"));
                    s.setName(rs.getString("sname"));
                    s.setAddress(rs.getString("saddr"));
                    s.setPhone(rs.getString("sphone"));
                    r.setSupplier(s);
                    InventoryStaff6 st = new InventoryStaff6();
                    st.setId(rs.getInt("staffId"));
                    st.setFullName(rs.getString("staffName"));
                    r.setInventoryStaff(st);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(lines)) {
                ps.setInt(1, receiptId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ImportDetail6 d = new ImportDetail6();
                        d.setId(rs.getInt("id"));
                        d.setQuantity(rs.getInt("quantity"));
                        d.setUnitPrice(rs.getFloat("unitPrice"));
                        Accessory6 a = new Accessory6();
                        a.setId(rs.getInt("aid"));
                        a.setName(rs.getString("aname"));
                        a.setDescription(rs.getString("adesc"));
                        d.setAccessory(a);
                        r.getDetails().add(d);
                    }
                }
            }
            return r;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Supplier6 readSupplier(ResultSet rs) throws SQLException {
        Supplier6 s = new Supplier6();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setAddress(rs.getString("address"));
        s.setPhone(rs.getString("phone"));
        return s;
    }
}
