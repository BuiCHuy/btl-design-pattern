package btldp;

public class HoaDonNhapFactory extends HoaDonFactory {
	
	@Override
	public HoaDon TaoHoaDon(HoaDonData data) {
		// TODO Auto-generated method stub
		return new HoaDonNhap(data.mahd,data.ds,data.nhacungcap);
	}

}
