package btldp;

public abstract class SanPhamFactory {
	abstract SanPham taoSanPham();
	public double tinhLoiNhuan(SanPham SanPham) {
        return SanPham.tinhLoiNhuan();
    }
}
