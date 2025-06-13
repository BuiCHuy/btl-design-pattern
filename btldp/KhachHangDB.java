package btldp;
import java.sql.*;

public class KhachHangDB {
	public static Connection getConn() throws ClassNotFoundException, SQLException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
         Connection conn = DriverManager.getConnection(
             "jdbc:mysql://localhost:3306/btl1", "root", "");
         System.out.println("Kết nối thành công!");
         return conn;
	}
	
	public static KhachHangData getKH(int id) throws SQLException, ClassNotFoundException {
		KhachHangData k = new KhachHangData();
		Connection conn = getConn();
		Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
        while (rs.next()) {
            if(rs.getInt("id") == id) {
            	k.id = rs.getInt("id");
            	k.hoten = rs.getString("hoten");
            	k.diachi = rs.getString("diachi");
            	k.sdt = rs.getString("sdt");
            }
        }
        conn.close();
		return k;
	}
	
	public static void ThemKh(int id, String hoten, String diachi, String sdt, String rank) throws ClassNotFoundException, SQLException {
		Connection conn = getConn();
		String sql = "INSERT INTO customer (id,hoten, diachi,sdt,rank) VALUES (?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		stmt.setString(2, hoten);
		stmt.setString(3, diachi);
		stmt.setString(4, sdt);
		stmt.setString(5, rank);
		
        stmt.executeUpdate();
		conn.close();
	}
	
	public static void SuaKh(int id, String hoten, String diachi, String sdt, String rank) throws SQLException, ClassNotFoundException {
		Connection conn = getConn();
		String sql = "UPDATE customer SET hoten = ?, diachi = ?, sdt = ?, rank = ? WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, hoten);
		stmt.setString(2, diachi);
		stmt.setString(3, sdt);
		stmt.setString(4, rank);
		stmt.setInt(5, id);
		
		int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("đã sửa khách hàng.");
        } else {
            System.out.println("không tìm thấy khách hàng để sửa.");
        }
		conn.close();
	}
	
	public static void XoaKh(int id) throws SQLException, ClassNotFoundException {
		Connection conn = getConn();
		String sql = "DELETE FROM customer WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1,id);
		
		stmt.executeUpdate();
	}
	
	public static void showList() throws ClassNotFoundException, SQLException {
		try {
			
			Connection conn = getConn();
			Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("hoten"));
            }
            conn.close();
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
	}
	
	public static void main(String[] args) {
		try {
			KhachHangDB.showList();
		}
		catch(Exception e) {
			
		}
	}
}