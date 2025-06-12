package btldp;


import java.util.List;

public abstract class HoaDon {
	String Mahd;
	List<ChiTietHoaDon> ds;
	double TongTien;
	public abstract String getLoai();
	public abstract void LuuHoaDon();
	public abstract void TinhTongTien();
}
