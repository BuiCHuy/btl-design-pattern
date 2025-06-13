package btldp;

import java.util.Date;

public abstract class SanPham {
	int id;
	String name, type;
    double purchasePrice, sellingPrice;
    Date date;
    int quantity;
	abstract double tinhLoiNhuan();
	@Override
	public String toString() {
		return name;
	}
}