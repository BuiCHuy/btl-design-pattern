package btldp;

public class HoaDonNhap implements HoaDon {
	MatHang[] dsmh;
	double TongTien;
	String NhaCungCap;
	public HoaDonNhap(MatHang[] dsmh,String nhacungcap) {
		this.dsmh = dsmh;
		this.NhaCungCap = nhacungcap;
	}
	@Override
	public double TinhTongTien() {
		// TODO Auto-generated method stub
		for(MatHang mh: dsmh) {
			TongTien += mh.gianhap;
		}
		return TongTien;
	}
	
	
}
