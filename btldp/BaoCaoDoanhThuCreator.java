package btldp;

public abstract class BaoCaoDoanhThuCreator {
    public abstract BaoCaoDoanhThu taoBaoCao();

    public void xuatBaoCao() {
        BaoCaoDoanhThu baoCao = taoBaoCao();
        baoCao.tinhDoanhThu();
    }
}