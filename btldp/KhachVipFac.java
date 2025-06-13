package btldp;
public class KhachVipFac extends KhachHangFactory{

	@Override
	public KhachHang taoKhachHang(KhachHangData ikh) {
		// TODO Auto-generated method stub
		return new KhachVip(ikh.id, ikh.hoten, ikh.diachi, ikh.sdt);
	}
	
}