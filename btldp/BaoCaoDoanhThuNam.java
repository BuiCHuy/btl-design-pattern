package btldp;

public class BaoCaoDoanhThuNam implements BaoCaoDoanhThu {
    private int nam;

    public BaoCaoDoanhThuNam(int nam) {
        this.nam = nam;
    }

    @Override
    public void tinhDoanhThu() {
        double doanhThu = Math.random() * 3_000_000_000L; // Giả lập doanh thu năm
        System.out.println("Doanh thu năm " + nam + ": " + String.format("%,.0f VNĐ", doanhThu));
    }
}