package btldp;

public class BaoCaoDoanhThuCreatorThang extends BaoCaoDoanhThuCreator {
    private String thang;

    public BaoCaoDoanhThuCreatorThang(String thang) {
        this.thang = thang;
    }

    @Override
    public BaoCaoDoanhThu taoBaoCao() {
        return new BaoCaoDoanhThuThang(thang);
    }
}