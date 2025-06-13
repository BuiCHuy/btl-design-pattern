package btldp;


public class SanPhamDienTuFactory extends SanPhamFactory {
    @Override
    public SanPham taoSanPham() {
        return new SanPhamDienTu();
    }
}
