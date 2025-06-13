package btldp;

public abstract class KhachHangFactory  {
	public abstract KhachHang taoKhachHang(KhachHangData ikh);
	
	public String getRank(int id) {
		
		try {
			KhachHangData ikh = KhachHangDB.getKH(id);
			KhachHang kh = taoKhachHang(ikh);
			return kh.getRank();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public String getInfo(int id) {
		try {
			KhachHangData ikh = KhachHangDB.getKH(id);
			KhachHang kh = taoKhachHang(ikh);
			return kh.getInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}