package btldp;

import java.util.Date;

class PhieuGiamGiaMoney extends PhieuGiamGia {
    public PhieuGiamGiaMoney(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
        super(ma, tenChuongTrinh, mucGiam, ngayBD, ngayKT, trangThai);
    }

    @Override
    public double tinhGiamGia(double gia) {
        return (mucGiam > gia) ? gia : mucGiam;
    }
}