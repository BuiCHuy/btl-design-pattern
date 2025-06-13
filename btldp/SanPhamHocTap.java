package btldp;

import java.util.Date;

public class SanPhamHocTap extends SanPham {
    public SanPhamHocTap() {}

    public SanPhamHocTap(int id, String name, double purchasePrice, double sellingPrice, String type, Date date, int quantity) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.type = type;
        this.date = date;
        this.quantity = quantity;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
    public void setType(String type) { this.type = type; }
    public void setDate(Date date) { this.date = date; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double tinhLoiNhuan() {
        return (sellingPrice - purchasePrice)-(sellingPrice*0.02);
    }
}