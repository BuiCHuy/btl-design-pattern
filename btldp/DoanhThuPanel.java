package btldp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Date;

public class DoanhThuPanel extends JPanel {
    private JComboBox<String> cbLoaiThongKe;
    private JTextField txtNgay, txtThang, txtNam;
    private JButton btnTinh;
    private JLabel lblKetQua;
    private JTable tableDoanhThu;
    private DefaultTableModel tableModel;
    private BaoCaoDoanhThuCreator baoCaoCreator;

    public DoanhThuPanel() {
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new FlowLayout());
        cbLoaiThongKe = new JComboBox<>(new String[]{"Theo Ngày", "Theo Tháng", "Theo Năm"});
        inputPanel.add(new JLabel("Chọn loại thống kê:"));
        inputPanel.add(cbLoaiThongKe);

        txtNgay = new JTextField(10);
        txtThang = new JTextField(8);
        txtNam = new JTextField(6);

        inputPanel.add(new JLabel("Ngày (dd/MM/yyyy):"));
        inputPanel.add(txtNgay);
        inputPanel.add(new JLabel("Tháng (MM/yyyy):"));
        inputPanel.add(txtThang);
        inputPanel.add(new JLabel("Năm (yyyy):"));
        inputPanel.add(txtNam);

        add(inputPanel, BorderLayout.NORTH);

        // Panel bảng dữ liệu
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Số Lượng Sản Phẩm Đã Bán Ra");
        tableModel.addColumn("Tổng Doanh Thu");
        tableDoanhThu = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableDoanhThu);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút và kết quả
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnTinh = new JButton("Tính Doanh Thu");
        lblKetQua = new JLabel("Kết quả doanh thu: ");
        bottomPanel.add(btnTinh);
        bottomPanel.add(lblKetQua);

        add(bottomPanel, BorderLayout.SOUTH);

        // Thêm listener
        cbLoaiThongKe.addActionListener(e -> updateInputFields());
        btnTinh.addActionListener(e -> tinhDoanhThu());

        // Khởi tạo mặc định
        updateInputFields();
    }

    private void updateInputFields() {
        String loai = (String) cbLoaiThongKe.getSelectedItem();
        txtNgay.setEnabled("Theo Ngày".equals(loai));
        txtThang.setEnabled("Theo Tháng".equals(loai));
        txtNam.setEnabled("Theo Năm".equals(loai));
        txtNgay.setText("");
        txtThang.setText("");
        txtNam.setText("");
        tableModel.setRowCount(0); // Xóa dữ liệu bảng khi thay đổi loại thống kê
    }

    private void tinhDoanhThu() {
        String loai = (String) cbLoaiThongKe.getSelectedItem();
        double tongDoanhThu = 0.0;
        tableModel.setRowCount(0); // Xóa dữ liệu bảng trước khi tính mới

        try {
            switch (loai) {
                case "Theo Ngày":
                    String ngayStr = txtNgay.getText().trim();
                    if (ngayStr.isEmpty()) {
                        showError("Vui lòng nhập ngày theo định dạng dd/MM/yyyy");
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);
                    Date ngay = sdf.parse(ngayStr);
                    java.sql.Date sqlDate = new java.sql.Date(ngay.getTime());
                    baoCaoCreator = new BaoCaoDoanhThuCreatorNgay(ngayStr);
                    tongDoanhThu = layDoanhThuVaHienThiChiTietNgay(sqlDate.toString());
                    break;

                case "Theo Tháng":
                    String thangStr = txtThang.getText().trim();
                    if (thangStr.isEmpty()) {
                        showError("Vui lòng nhập tháng theo định dạng MM/yyyy");
                        return;
                    }
                    sdf = new SimpleDateFormat("MM/yyyy");
                    sdf.setLenient(false);
                    sdf.parse(thangStr);
                    baoCaoCreator = new BaoCaoDoanhThuCreatorThang(thangStr);
                    tongDoanhThu = layDoanhThuVaHienThiChiTietThang(thangStr);
                    break;

                case "Theo Năm":
                    String namStr = txtNam.getText().trim();
                    if (namStr.isEmpty()) {
                        showError("Vui lòng nhập năm (yyyy)");
                        return;
                    }
                    int nam = Integer.parseInt(namStr);
                    baoCaoCreator = new BaoCaoDoanhThuCreatorNam(nam);
                    tongDoanhThu = layDoanhThuVaHienThiChiTietNam(nam);
                    break;
            }

            if (baoCaoCreator != null) {
                baoCaoCreator.xuatBaoCao();
                lblKetQua.setText(String.format("Kết quả doanh thu: %, .0f VNĐ", tongDoanhThu));
            }
        } catch (ParseException ex) {
            showError("Định dạng ngày/tháng không hợp lệ: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            showError("Năm phải là số nguyên: " + ex.getMessage());
        } catch (SQLException ex) {
            showError("Lỗi truy vấn cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private double layDoanhThuVaHienThiChiTietNgay(String ngay) throws SQLException {
        String sql = "SELECT SUM(id.quantity) AS tong_so_luong, SUM(p.selling_price * id.quantity) AS tong_doanh_thu " +
                     "FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE DATE(i.created_date) = ?";
        return thucHienTruyVanVaHienThi(sql, ngay);
    }

    private double layDoanhThuVaHienThiChiTietThang(String thang) throws SQLException {
        String sql = "SELECT SUM(id.quantity) AS tong_so_luong, SUM(p.selling_price * id.quantity) AS tong_doanh_thu " +
                     "FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE DATE_FORMAT(i.created_date, '%m/%Y') = ?";
        return thucHienTruyVanVaHienThi(sql, thang);
    }

    private double layDoanhThuVaHienThiChiTietNam(int nam) throws SQLException {
        String sql = "SELECT SUM(id.quantity) AS tong_so_luong, SUM(p.selling_price * id.quantity) AS tong_doanh_thu " +
                     "FROM Invoice_detail id " +
                     "JOIN Invoice i ON id.invoice_id = i.id " +
                     "JOIN Product p ON id.product_id = p.id " +
                     "WHERE YEAR(i.created_date) = ?";
        return thucHienTruyVanVaHienThi(sql, String.valueOf(nam));
    }

    private double thucHienTruyVanVaHienThi(String sql, String param) throws SQLException {
        double tongDoanhThu = 0.0;
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (param.matches("\\d{4}")) { // Nếu là năm
                pstmt.setInt(1, Integer.parseInt(param));
            } else {
                pstmt.setString(1, param);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int tongSoLuong = rs.getInt("tong_so_luong");
                    tongDoanhThu = rs.getDouble("tong_doanh_thu");
                    tableModel.addRow(new Object[]{tongSoLuong, tongDoanhThu});
                }
            }
        }
        return tongDoanhThu;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}