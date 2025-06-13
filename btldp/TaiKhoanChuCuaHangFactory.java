package btldp;

public class TaiKhoanChuCuaHangFactory extends TaiKhoanFactory {
    @Override
    public TaiKhoanNguoiDung taoNguoiDung(String username, String password) {
        return new TaiKhoanChuCuaHang(username, password);
    }
}