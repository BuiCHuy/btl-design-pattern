package projecttaphoa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DoanhThuPanel extends JPanel {
    private JComboBox<String> cbLoaiThongKe;
    private JTextField txtNgay, txtThang, txtNam;
    private JButton btnTinh;
    private JLabel lblKetQua;

    public DoanhThuPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());

        cbLoaiThongKe = new JComboBox<>(new String[]{"Theo Ngày", "Theo Tháng", "Theo Năm"});
        inputPanel.add(new JLabel("Chọn loại thống kê:"));
        inputPanel.add(cbLoaiThongKe);

        txtNgay = new JTextField(8);
        txtThang = new JTextField(6);
        txtNam = new JTextField(6);

        inputPanel.add(new JLabel("Ngày (dd/MM/yyyy):"));
        inputPanel.add(txtNgay);
        inputPanel.add(new JLabel("Tháng (MM/yyyy):"));
        inputPanel.add(txtThang);
        inputPanel.add(new JLabel("Năm (yyyy):"));
        inputPanel.add(txtNam);

        add(inputPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnTinh = new JButton("Tính Doanh Thu");
        lblKetQua = new JLabel("Kết quả doanh thu: ");
        bottomPanel.add(btnTinh);
        bottomPanel.add(lblKetQua);

        add(bottomPanel, BorderLayout.SOUTH);

        updateInputFields();

        // Thay đổi lựa chọn sẽ ẩn hiện trường phù hợp
        cbLoaiThongKe.addActionListener(e -> updateInputFields());

        btnTinh.addActionListener(e -> tinhDoanhThu());
    }

    private void updateInputFields() {
        String loai = (String) cbLoaiThongKe.getSelectedItem();
        txtNgay.setEnabled(false);
        txtThang.setEnabled(false);
        txtNam.setEnabled(false);

        if ("Theo Ngày".equals(loai)) {
            txtNgay.setEnabled(true);
        } else if ("Theo Tháng".equals(loai)) {
            txtThang.setEnabled(true);
        } else {
            txtNam.setEnabled(true);
        }
    }

    // Hàm giả lập tính doanh thu, bạn sẽ thay bằng code lấy dữ liệu thật
    private void tinhDoanhThu() {
        String loai = (String) cbLoaiThongKe.getSelectedItem();
        double doanhThu = 0;

        try {
            if ("Theo Ngày".equals(loai)) {
                String ngayStr = txtNgay.getText().trim();
                if (ngayStr.isEmpty()) {
                    showError("Vui lòng nhập ngày theo định dạng dd/MM/yyyy");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date ngay = sdf.parse(ngayStr);
                // giả sử doanh thu ngày là số ngẫu nhiên
                doanhThu = fakeTinhDoanhThuNgay(ngay);

            } else if ("Theo Tháng".equals(loai)) {
                String thangStr = txtThang.getText().trim();
                if (thangStr.isEmpty()) {
                    showError("Vui lòng nhập tháng theo định dạng MM/yyyy");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
                Date thang = sdf.parse(thangStr);
                doanhThu = fakeTinhDoanhThuThang(thang);

            } else {
                String namStr = txtNam.getText().trim();
                if (namStr.isEmpty()) {
                    showError("Vui lòng nhập năm (yyyy)");
                    return;
                }
                int nam = Integer.parseInt(namStr);
                doanhThu = fakeTinhDoanhThuNam(nam);
            }

            lblKetQua.setText(String.format("Kết quả doanh thu: %, .0f VNĐ", doanhThu));

        } catch (ParseException ex) {
            showError("Ngày tháng không đúng định dạng!");
        } catch (NumberFormatException ex) {
            showError("Năm phải là số!");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Giả lập: doanh thu ngày là số ngẫu nhiên 0..10 triệu
    private double fakeTinhDoanhThuNgay(Date ngay) {
        return Math.random() * 10_000_000;
    }

    // Giả lập: doanh thu tháng 0..300 triệu
    private double fakeTinhDoanhThuThang(Date thang) {
        return Math.random() * 300_000_000;
    }

    // Giả lập: doanh thu năm 0..3 tỷ
    private double fakeTinhDoanhThuNam(int nam) {
        return Math.random() * 3_000_000_000L;
    }

 
}

