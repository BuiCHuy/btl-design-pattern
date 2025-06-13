package btldp;

import java.util.Date;

class PhieuGiamGiaPercent extends PhieuGiamGia {
    public PhieuGiamGiaPercent(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
        super(ma, tenChuongTrinh, mucGiam, ngayBD, ngayKT, trangThai);
    }

    @Override
    public double tinhGiamGia(double gia) {
        return gia * (mucGiam / 100);
    }
}