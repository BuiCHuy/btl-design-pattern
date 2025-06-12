package btldp;

public class HoaDonNhapFactory extends HoaDonFactory {
	
	@Override
	public HoaDon TaoHoaDon(MatHang[] ds, String doituong) {
		// TODO Auto-generated method stub
		return new HoaDonNhap(ds,doituong);
	}

}
