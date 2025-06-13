package btldp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaoCaoDoanhThuNgay implements BaoCaoDoanhThu {
    private String ngay;

    public BaoCaoDoanhThuNgay(String ngay) {
        this.ngay = ngay;
    }

    @Override
    public void tinhDoanhThu() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayTinh = sdf.parse(ngay);
            double doanhThu = Math.random() * 10_000_000; // Giả lập doanh thu ngày
            System.out.println("Doanh thu ngày " + ngay + ": " + String.format("%,.0f VNĐ", doanhThu));
        } catch (ParseException e) {
            System.out.println("Định dạng ngày không hợp lệ!");
        }
    }
}