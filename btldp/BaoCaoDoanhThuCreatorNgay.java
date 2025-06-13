package btldp;

public class BaoCaoDoanhThuCreatorNgay extends BaoCaoDoanhThuCreator {
    private String ngay;

    public BaoCaoDoanhThuCreatorNgay(String ngay) {
        this.ngay = ngay;
    }

    @Override
    public BaoCaoDoanhThu taoBaoCao() {
        return new BaoCaoDoanhThuNgay(ngay);
    }
}