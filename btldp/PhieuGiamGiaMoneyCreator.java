package btldp;

import java.util.Date;

public class PhieuGiamGiaMoneyCreator extends PhieuGiamGiaCreator {
    @Override
    public PhieuGiamGia createPhieuGiamGia(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
        return new PhieuGiamGiaMoney(ma, tenChuongTrinh, mucGiam, ngayBD, ngayKT, trangThai);
    }
}
