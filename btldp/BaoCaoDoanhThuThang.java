package btldp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaoCaoDoanhThuThang implements BaoCaoDoanhThu {
    private String thang;

    public BaoCaoDoanhThuThang(String thang) {
        this.thang = thang;
    }

    @Override
    public void tinhDoanhThu() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            Date thangTinh = sdf.parse(thang);
            double doanhThu = Math.random() * 300_000_000; // Giả lập doanh thu tháng
            System.out.println("Doanh thu tháng " + thang + ": " + String.format("%,.0f VNĐ", doanhThu));
        } catch (ParseException e) {
            System.out.println("Định dạng tháng không hợp lệ!");
        }
    }
}