package com.garaman.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Lop DAO dung chung: giu ket noi CSDL; cac lop DAO khac ke thua lop nay. */
public class DAO6 {
    private static final String URL = System.getProperty("db.url", "jdbc:h2:file:./data/garaman;DB_CLOSE_ON_EXIT=FALSE");
    private static final String USER = System.getProperty("db.user", "sa");
    private static final String PASS = System.getProperty("db.pass", "");

    public DAO6() { }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /** Tao bang (schema.sql) va nap du lieu mau (data.sql) neu CSDL con trong. */
    public static void initDatabase() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS); Statement st = con.createStatement()) {
            runScript(st, "schema.sql");
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM tblMember6")) {
                rs.next();
                if (rs.getInt(1) == 0) runScript(st, "data.sql");
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Khong khoi tao duoc CSDL: " + e.getMessage(), e);
        }
    }

    private static void runScript(Statement st, String resource) throws IOException, SQLException {
        try (InputStream in = DAO6.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) throw new IOException("Khong tim thay " + resource);
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            StringBuilder cleaned = new StringBuilder();
            for (String line : sql.split("\n")) {
                if (!line.trim().startsWith("--")) cleaned.append(line).append('\n');
            }
            for (String stmt : cleaned.toString().split(";\\s*\\n")) {
                if (!stmt.isBlank()) st.execute(stmt);
            }
        }
    }

    /** Bam mat khau SHA-256 (hex) - trung voi ham HASH cua CSDL khi nap du lieu mau. */
    public static String sha256(String s) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
