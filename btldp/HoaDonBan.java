package btldp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HoaDonBan extends HoaDon{
	String khachhang;
	public HoaDonBan(int mahd,List<ChiTietHoaDon> ds,String kh) {
		this.Mahd = mahd;
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
	public void LuuHoaDon() throws SQLException{
		// TODO Auto-generated method stub
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl1", "root", "")){
            conn.setAutoCommit(false);
            String query = "INSERT INTO invoice(id,type,partner, total) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Mahd);
            stmt.setString(2,getLoai());
            stmt.setString(3, khachhang);
            stmt.setDouble(4, TongTien);
            stmt.executeUpdate();
            
            String query2 = "INSERT INTO invoice_detail(invoice_id, product_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            for(ChiTietHoaDon ct: ds) {
            	stmt2.setInt(1,Mahd);
                stmt2.setInt(2,ct.mathang.id);
                stmt2.setInt(3, ct.soluong);
                stmt2.addBatch();
            }
            stmt2.executeBatch();
            conn.commit();
            
		}
//		catch(SQLException e) {
//            e.printStackTrace();
//		}
	}


	@Override
	public void TinhTongTien() {
		// TODO Auto-generated method stub
		for(ChiTietHoaDon ct: ds) {
			TongTien += ct.TinhThanhTien("Bán");
		}
	}
	
}
