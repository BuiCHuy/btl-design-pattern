package btldp;

public class HoaDonBanFactory extends HoaDonFactory {

	@Override
	public HoaDon TaoHoaDon(HoaDonData data) {
		// TODO Auto-generated method stub
		return new HoaDonBan(data.mahd,data.ds,data.tenkh);
	}


}
