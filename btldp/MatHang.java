package btldp;

public class MatHang {
	int id;
	String tenmh;
	double giaban;
	double gianhap;
	public MatHang(int id,String tenmh,double giaban,double gianhap) {
		this.id = id;
		this.tenmh = tenmh;
		this.giaban = giaban;
		this.gianhap = gianhap;
	}
	@Override
    public String toString() {
    	return tenmh;
    }
}	
