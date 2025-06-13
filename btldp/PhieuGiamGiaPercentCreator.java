package btldp;

import java.util.Date;

public class PhieuGiamGiaPercentCreator extends PhieuGiamGiaCreator {
    @Override
    public PhieuGiamGia createPhieuGiamGia(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
        return new PhieuGiamGiaPercent(ma, tenChuongTrinh, mucGiam, ngayBD, ngayKT, trangThai);
    }
}