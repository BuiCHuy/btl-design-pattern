package btldp;

public class TaiKhoanNhanVienFactory extends TaiKhoanFactory {
    @Override
    public TaiKhoanNguoiDung taoNguoiDung(String username, String password) {
        return new TaiKhoanNhanVien(username, password);
    }
}
