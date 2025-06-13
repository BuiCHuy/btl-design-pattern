package btldp;

public class KhachVip implements KhachHang {
	private int id;
	private String hoten;
	private String diachi;
	private String sdt;
	
	public KhachVip() {
		
	}
	public KhachVip(int iid, String ihoten, String idiachi, String isdt) {
		System.out.println("iid: " + iid);
		id = iid;
		hoten = ihoten;
		diachi = idiachi;
		sdt = isdt;
	}
	
	@Override
	public String getRank() {
		// TODO Auto-generated method stub
		return "vip";
	}
	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return id + "|" + hoten + "|" + diachi + "|" + sdt;
	}
}
