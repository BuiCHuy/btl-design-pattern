package btldp;

public class ChiTietHoaDon {
    String mahd;
    SanPham mathang;
    int soluong;

    public ChiTietHoaDon(String mahd,SanPham matHang, int soLuong) {
    	this.mahd = mahd;
        this.mathang = matHang;
        this.soluong = soLuong;
    }
    public double TinhThanhTien(String loai) {
    	if(loai == "Bán") {
    		return soluong*mathang.sellingPrice;
    	}
    	else return soluong*mathang.purchasePrice;
    }
    
}
