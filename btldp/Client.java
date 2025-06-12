package btldp;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MatHang[] ds = {new MatHang("l",10,20),new MatHang("2",15,25)};
		HoaDonFactory f = new HoaDonNhapFactory();
		HoaDon hd1 = f.TaoHoaDon(ds,"Cong ty TNHH 1 minh tao");
		System.out.println("Tong tien: "+ hd1.TinhTongTien());
	}

}
