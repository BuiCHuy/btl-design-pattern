package btldp;

public abstract class HoaDonFactory {
	String loai;
	public abstract HoaDon TaoHoaDon(MatHang[] ds,String doituong);
}
