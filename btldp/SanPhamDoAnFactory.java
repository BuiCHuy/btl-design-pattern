package btldp;

public class SanPhamDoAnFactory extends SanPhamFactory{
	@Override
    public SanPham taoSanPham() {
        return new SanPhamDoAn();
    }
}