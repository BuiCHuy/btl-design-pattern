package btldp;

public class BaoCaoDoanhThuCreatorNam extends BaoCaoDoanhThuCreator {
    private int nam;

    public BaoCaoDoanhThuCreatorNam(int nam) {
        this.nam = nam;
    }

    @Override
    public BaoCaoDoanhThu taoBaoCao() {
        return new BaoCaoDoanhThuNam(nam);
    }
}