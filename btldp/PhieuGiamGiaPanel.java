package btldp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhieuGiamGiaPanel extends JPanel {
    private JTextField txtMaPGG, txtTenChuongTrinh, txtMucGiam;
    private JTextField txtNgayBD, txtNgayKT;
    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbLoaiGiamGia; // Thêm chọn loại giảm giá
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<PhieuGiamGia> danhSachPGG = new ArrayList<>();
    private PhieuGiamGiaCreator creator;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    
    	
    public PhieuGiamGiaPanel() {
    	    setLayout(new BorderLayout(10, 10));

    	    JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU GIẢM GIÁ", JLabel.CENTER);
    	    lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
    	    add(lblTitle, BorderLayout.NORTH);

    	    // Form bên trái
    	    JPanel formPanel = new JPanel(new GridBagLayout());
    	    formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu giảm giá"));
    	    GridBagConstraints gbc = new GridBagConstraints();
    	    gbc.insets = new Insets(5, 5, 5, 5);
    	    gbc.fill = GridBagConstraints.HORIZONTAL;

    	    txtMaPGG = new JTextField(12);
    	    txtTenChuongTrinh = new JTextField(12);
    	    txtMucGiam = new JTextField(12);
    	    txtNgayBD = new JTextField(12);
    	    txtNgayKT = new JTextField(12);
    	    cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Hết hạn"});
    	    cbLoaiGiamGia = new JComboBox<>(new String[]{"Phần trăm", "Tiền mặt"});
    	    cbLoaiGiamGia.addActionListener(e -> {
    	        String loai = cbLoaiGiamGia.getSelectedItem().toString();
    	        creator = loai.equals("Phần trăm") ? new PhieuGiamGiaPercentCreator() : new PhieuGiamGiaMoneyCreator();
    	    });
    	    creator = new PhieuGiamGiaPercentCreator();

    	    // Thêm các thành phần
    	    gbc.gridx = 0; gbc.gridy = 0;
    	    formPanel.add(new JLabel("Mã giảm giá:"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(txtMaPGG, gbc);

    	    gbc.gridx = 0; gbc.gridy = 1;
    	    formPanel.add(new JLabel("Tên CT:"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(txtTenChuongTrinh, gbc);

    	    gbc.gridx = 0; gbc.gridy = 2;
    	    formPanel.add(new JLabel("Loại giảm giá:"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(cbLoaiGiamGia, gbc);

    	    gbc.gridx = 0; gbc.gridy = 3;
    	    formPanel.add(new JLabel("Mức giảm (% hoặc tiền):"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(txtMucGiam, gbc);

    	    gbc.gridx = 0; gbc.gridy = 4;
    	    formPanel.add(new JLabel("Ngày bắt đầu (dd/MM/yyyy):"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(txtNgayBD, gbc);

    	    gbc.gridx = 0; gbc.gridy = 5;
    	    formPanel.add(new JLabel("Ngày kết thúc (dd/MM/yyyy):"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(txtNgayKT, gbc);

    	    gbc.gridx = 0; gbc.gridy = 6;
    	    formPanel.add(new JLabel("Trạng thái:"), gbc);
    	    gbc.gridx = 1;
    	    formPanel.add(cbTrangThai, gbc);

    	    // Thêm ô Giá gốc (nếu có)
    	    gbc.gridx = 0; gbc.gridy = 7;
    	    formPanel.add(new JLabel("Giá gốc:"), gbc);
    	    gbc.gridx = 1;
    	    JTextField txtGiaGoc = new JTextField(12);
    	    formPanel.add(txtGiaGoc, gbc);

    	    // Di chuyển btnPanel xuống cuối
    	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    	    JButton btnThem = new JButton("Thêm");
    	    JButton btnSua = new JButton("Sửa");
    	    JButton btnXoa = new JButton("Xóa");
    	    JButton btnTinhGia = new JButton("Tính giá");
    	    btnPanel.add(btnThem);
    	    btnPanel.add(btnSua);
    	    btnPanel.add(btnXoa);
    	    btnPanel.add(btnTinhGia);

    	    gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; // Đặt ở hàng 8, cuối cùng
    	    formPanel.add(btnPanel, gbc);
        
        add(formPanel, BorderLayout.WEST);

   

        // Bảng bên phải
        model = new DefaultTableModel(new String[]{"Mã giảm giá", "Tên CT", "Mức giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Event nút
        btnThem.addActionListener(e -> themPhieuGiamGia());
        btnSua.addActionListener(e -> suaPhieuGiamGia());
        btnXoa.addActionListener(e -> xoaPhieuGiamGia());

     // Event cho nút Tính giá
        btnTinhGia.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu giảm giá!");
                return;
            }

            PhieuGiamGia pgg = danhSachPGG.get(row);
            try {
                double giaGoc = Double.parseDouble(txtGiaGoc.getText().trim());
                double giamGia = pgg.tinhGiamGia(giaGoc);
                double giaCuoi = giaGoc - giamGia;
                JOptionPane.showMessageDialog(this, "Giá sau giảm: " + String.format("%.2f", giaCuoi));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá gốc phải là số hợp lệ!");
            }
        });
        
        // Click bảng load lên form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0) {
                    PhieuGiamGia pgg = danhSachPGG.get(row);
                    txtMaPGG.setText(pgg.ma);
                    txtTenChuongTrinh.setText(pgg.tenChuongTrinh);
                    txtMucGiam.setText(String.valueOf(pgg.mucGiam));
                    txtNgayBD.setText(sdf.format(pgg.ngayBD));
                    txtNgayKT.setText(sdf.format(pgg.ngayKT));
                    cbTrangThai.setSelectedItem(pgg.trangThai);
                    cbLoaiGiamGia.setSelectedItem(pgg instanceof PhieuGiamGiaPercent ? "Phần trăm" : "Tiền mặt");
                    creator = (pgg instanceof PhieuGiamGiaPercent) ? new PhieuGiamGiaPercentCreator() : new PhieuGiamGiaMoneyCreator();
                }
            }
        });
    }

    private void themPhieuGiamGia() {
        String ma = txtMaPGG.getText().trim();
        String ten = txtTenChuongTrinh.getText().trim();
        String mucGiamStr = txtMucGiam.getText().trim();
        String ngayBDStr = txtNgayBD.getText().trim();
        String ngayKTStr = txtNgayKT.getText().trim();
        String trangThai = cbTrangThai.getSelectedItem().toString();
        String loaiGiamGia = cbLoaiGiamGia.getSelectedItem().toString(); // Lấy loại giảm giá từ JComboBox

        if (ma.isEmpty() || ten.isEmpty() || mucGiamStr.isEmpty() || ngayBDStr.isEmpty() || ngayKTStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        double mucGiam;
        try {
            mucGiam = Double.parseDouble(mucGiamStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mức giảm phải là số hợp lệ!");
            return;
        }

        Date ngayBD, ngayKT;
        try {
            ngayBD = sdf.parse(ngayBDStr);
            ngayKT = sdf.parse(ngayKTStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày tháng không đúng định dạng dd/MM/yyyy!");
            return;
        }

        if (ngayKT.before(ngayBD)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
            return;
        }

        // Kiểm tra ngày kết thúc không vượt quá 2 năm từ ngày hiện tại
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // Ngày hiện tại: 10:06 PM +07, 12/06/2025
        cal.add(Calendar.YEAR, 2);
        Date maxDate = cal.getTime(); // Giới hạn đến 12/06/2027
        if (ngayKT.after(maxDate)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được vượt quá 2 năm từ ngày hiện tại!");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO phieu_giam_gia (ma, ten_chuong_trinh, loai_giam_gia, muc_giam, ngay_bat_dau, ngay_ket_thuc, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ma);
            pstmt.setString(2, ten);
            pstmt.setString(3, loaiGiamGia);
            pstmt.setDouble(4, mucGiam);
            pstmt.setDate(5, new java.sql.Date(ngayBD.getTime()));
            pstmt.setDate(6, new java.sql.Date(ngayKT.getTime()));
            pstmt.setString(7, trangThai);
            pstmt.executeUpdate();
            
            // Tạo đối tượng PhieuGiamGia và thêm vào danh sách
            PhieuGiamGia pgg = creator.createPhieuGiamGia(ma, ten, mucGiam, ngayBD, ngayKT, trangThai);
            danhSachPGG.add(pgg);
            model.addRow(new Object[]{ma, ten, mucGiam, ngayBDStr, ngayKTStr, trangThai});
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm vào cơ sở dữ liệu: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

        clearForm();
    }

    private void suaPhieuGiamGia() {
        int row = table.getSelectedRow();
        if(row < 0) return;

        String ma = txtMaPGG.getText().trim();
        String ten = txtTenChuongTrinh.getText().trim();
        String mucGiamStr = txtMucGiam.getText().trim();
        String ngayBDStr = txtNgayBD.getText().trim();
        String ngayKTStr = txtNgayKT.getText().trim();
        String trangThai = cbTrangThai.getSelectedItem().toString();

        if(ma.isEmpty() || ten.isEmpty() || mucGiamStr.isEmpty() || ngayBDStr.isEmpty() || ngayKTStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        double mucGiam;
        try {
            mucGiam = Double.parseDouble(mucGiamStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mức giảm phải là số hợp lệ!");
            return;
        }

        Date ngayBD, ngayKT;
        try {
            ngayBD = sdf.parse(ngayBDStr);
            ngayKT = sdf.parse(ngayKTStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày tháng không đúng định dạng dd/MM/yyyy!");
            return;
        }

        if(ngayKT.before(ngayBD)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
            return;
        }

        PhieuGiamGia pgg = creator.createPhieuGiamGia(ma, ten, mucGiam, ngayBD, ngayKT, trangThai);
        danhSachPGG.set(row, pgg);
        model.setValueAt(ma, row, 0);
        model.setValueAt(ten, row, 1);
        model.setValueAt(mucGiam, row, 2);
        model.setValueAt(ngayBDStr, row, 3);
        model.setValueAt(ngayKTStr, row, 4);
        model.setValueAt(trangThai, row, 5);

        clearForm();
    }

    private void xoaPhieuGiamGia() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String ma = (String) model.getValueAt(row, 0);
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM phieu_giam_gia WHERE ma = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ma);
            pstmt.executeUpdate();
            danhSachPGG.remove(row);
            model.removeRow(row);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

        clearForm();
    }
    
    private void loadData() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM phieu_giam_gia";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String ma = rs.getString("ma");
                String ten = rs.getString("ten_chuong_trinh");
                String loai = rs.getString("loai_giam_gia");
                double mucGiam = rs.getDouble("muc_giam");
                Date ngayBD = rs.getDate("ngay_bat_dau");
                Date ngayKT = rs.getDate("ngay_ket_thuc");
                String trangThai = rs.getString("trang_thai");
                PhieuGiamGia pgg = (loai.equals("Phần trăm")) ? new PhieuGiamGiaPercentCreator().createPhieuGiamGia(ma, ten, mucGiam, ngayBD, ngayKT, trangThai)
                        : new PhieuGiamGiaMoneyCreator().createPhieuGiamGia(ma, ten, mucGiam, ngayBD, ngayKT, trangThai);
                danhSachPGG.add(pgg);
                model.addRow(new Object[]{ma, ten, mucGiam, sdf.format(ngayBD), sdf.format(ngayKT), trangThai});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void clearForm() {
        txtMaPGG.setText("");
        txtTenChuongTrinh.setText("");
        txtMucGiam.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        cbTrangThai.setSelectedIndex(0);
        cbLoaiGiamGia.setSelectedIndex(0);
        creator = new PhieuGiamGiaPercentCreator();
        table.clearSelection();
    }
}
