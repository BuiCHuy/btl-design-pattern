package btldp;

public class ChiTietHoaDon {
    String mahd;
	MatHang mathang;
    int soluong;

    public ChiTietHoaDon(String mahd,MatHang matHang, int soLuong) {
    	this.mahd = mahd;
        this.mathang = matHang;
        this.soluong = soLuong;
    }
    public double TinhThanhTien(String loai) {
    	if(loai == "Bán") {
    		return soluong*mathang.giaban;
    	}
    	else return soluong*mathang.gianhap;
    }
    
}
