package btldp;


import java.sql.*;
import java.util.ArrayList;

public class LoginSystem {
    private static LoginSystem instance; // Instance tĩnh
    private ArrayList<TaiKhoanNguoiDung> danhSachTK = new ArrayList<>();
    private TaiKhoanNguoiDung currentUser;

    private LoginSystem() {
        loadFromDatabase(); // Tải dữ liệu từ MySQL khi khởi tạo
    }

    public static LoginSystem getInstance() {
        if (instance == null) {
            instance = new LoginSystem();
        }
        return instance;
    }

    private void loadFromDatabase() {
        try {
            // Thay "your_password" bằng mật khẩu MySQL của bạn
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/btl1", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, password, role FROM accounts");
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                TaiKhoanFactory factory = role.equals("ChuCuaHang") ? new TaiKhoanChuCuaHangFactory() : new TaiKhoanNhanVienFactory();
                danhSachTK.add(factory.taoNguoiDung(username, password));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void saveToDatabase(TaiKhoanNguoiDung taiKhoan) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/btl1", "root", "");
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO accounts (username, password, role) VALUES (?, ?, ?)");
            pstmt.setString(1, taiKhoan.getUsername());
            pstmt.setString(2, taiKhoan.getPassword());
            pstmt.setString(3, taiKhoan instanceof TaiKhoanChuCuaHang ? "ChuCuaHang" : "NhanVien");
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase(String username) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/btl1", "root", "");
            PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM accounts WHERE username = ?");
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        for (TaiKhoanNguoiDung tk : danhSachTK) {
            if (tk.getUsername().equals(username) && tk.getPassword().equals(password)) {
                currentUser = tk;
                currentUser.dangNhap();
                return true;
            }
        }
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.dangXuat();
            currentUser = null;
        }
    }

    public TaiKhoanNguoiDung getCurrentUser() { return currentUser; }
    public boolean isChuCuaHang() { return currentUser instanceof TaiKhoanChuCuaHang; }
    public ArrayList<TaiKhoanNguoiDung> getDanhSachTK() { return danhSachTK; }
}