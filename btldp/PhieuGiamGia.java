package btldp;

import java.util.Date;

public abstract class PhieuGiamGia {
    String ma, tenChuongTrinh, trangThai;
    double mucGiam;
    Date ngayBD, ngayKT;

    public PhieuGiamGia(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
        this.ma = ma;
        this.tenChuongTrinh = tenChuongTrinh;
        this.mucGiam = mucGiam;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
    }

    // Phương thức trừu tượng để áp dụng giảm giá
    public abstract double tinhGiamGia(double gia);
}