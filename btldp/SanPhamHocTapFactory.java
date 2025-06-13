package btldp;

public class SanPhamHocTapFactory extends SanPhamFactory {
    @Override
    public SanPham taoSanPham() {
        return new SanPhamHocTap();
    }
}