package btldp;


import java.sql.*;

public class KetNoiCSDL {
    private static final String URL = "jdbc:mysql://localhost:3306/btl1";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver không tìm thấy: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static double tinhDoanhThuNgay(String ngay) throws SQLException {
        String sql = "SELECT SUM(p.selling_price * id.quantity) FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE DATE(i.created_date) = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ngay);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble(1);
                    return total > 0 ? total : 0.0;
                }
            }
            return 0.0;
        }
    }

    public static double tinhDoanhThuThang(String thang) throws SQLException {
        String sql = "SELECT SUM(p.selling_price * id.quantity) FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE DATE_FORMAT(i.created_date, '%m/%Y') = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, thang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble(1);
                    return total > 0 ? total : 0.0;
                }
            }
            return 0.0;
        }
    }

    public static double tinhDoanhThuNam(int nam) throws SQLException {
        String sql = "SELECT SUM(p.selling_price * id.quantity) FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE YEAR(i.created_date) = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble(1);
                    return total > 0 ? total : 0.0;
                }
            }
            return 0.0;
        }
    }
}