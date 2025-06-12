package btldp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HoaDonBan extends HoaDon{
	String khachhang;
	public HoaDonBan(String Ma,List<ChiTietHoaDon> ds,String kh) {
		this.Mahd = Ma;
		this.ds = ds;
		this.khachhang = kh; 
		TinhTongTien();
	}

	
	@Override
	public String getLoai() {
		// TODO Auto-generated method stub
		return "Bán";
	}
	@Override
	public void LuuHoaDon() {
		// TODO Auto-generated method stub
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl", "root", "")){
            conn.setAutoCommit(false);
            String query = "INSERT INTO hoadon(mahd, loai,doitac, tongtien) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, Mahd);
            stmt.setString(2,getLoai());
            stmt.setString(3, khachhang);
            stmt.setDouble(4, TongTien);
            stmt.executeUpdate();
            
            String query2 = "INSERT INTO chitiethoadon(mahd, mathang_id, soluong) VALUES (?, ?, ?)";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            for(ChiTietHoaDon ct: ds) {
            	stmt2.setString(1,Mahd);
                stmt2.setInt(2,ct.mathang.id);
                stmt2.setInt(3, ct.soluong);
                stmt2.addBatch();
            }
            stmt2.executeBatch();
            conn.commit();
            
		}
		catch(SQLException e) {
            e.printStackTrace();
		}
	}


	@Override
	public void TinhTongTien() {
		// TODO Auto-generated method stub
		for(ChiTietHoaDon ct: ds) {
			TongTien += ct.TinhThanhTien("Bán");
		}
	}
	
}
