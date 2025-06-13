package btldp;

import java.sql.SQLException;
import java.util.List;

public abstract class HoaDon {
	int Mahd;
	List<ChiTietHoaDon> ds;
	double TongTien;
	public abstract String getLoai();
	public abstract void LuuHoaDon() throws SQLException ;
	public abstract void TinhTongTien();
}
