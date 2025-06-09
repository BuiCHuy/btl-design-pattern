package projecttaphoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhieuGiamGiaPanel extends JPanel {
    private JTextField txtMaPGG, txtTenChuongTrinh, txtMucGiam;
    private JTextField txtNgayBD, txtNgayKT;
    private JComboBox<String> cbTrangThai;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<PhieuGiamGia> danhSachPGG = new ArrayList<>();

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
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaPGG = new JTextField(12);
        txtTenChuongTrinh = new JTextField(12);
        txtMucGiam = new JTextField(12);
        txtNgayBD = new JTextField(12);
        txtNgayKT = new JTextField(12);
        cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Hết hạn"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã giảm giá:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaPGG, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên CT:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenChuongTrinh, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Mức giảm (% hoặc tiền):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMucGiam, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Ngày bắt đầu (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNgayBD, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Ngày kết thúc (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNgayKT, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbTrangThai, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
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

        PhieuGiamGia pgg = new PhieuGiamGia(ma, ten, mucGiam, ngayBD, ngayKT, trangThai);
        danhSachPGG.add(pgg);
        model.addRow(new Object[]{ma, ten, mucGiam, ngayBDStr, ngayKTStr, trangThai});
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

        PhieuGiamGia pgg = danhSachPGG.get(row);
        pgg.ma = ma;
        pgg.tenChuongTrinh = ten;
        pgg.mucGiam = mucGiam;
        pgg.ngayBD = ngayBD;
        pgg.ngayKT = ngayKT;
        pgg.trangThai = trangThai;

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
        if(row < 0) return;
        danhSachPGG.remove(row);
        model.removeRow(row);
        clearForm();
    }

    private void clearForm() {
        txtMaPGG.setText("");
        txtTenChuongTrinh.setText("");
        txtMucGiam.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }

    // Class model phiếu giảm giá
    class PhieuGiamGia {
        String ma, tenChuongTrinh, trangThai;
        double mucGiam;
        Date ngayBD, ngayKT;

        public PhieuGiamGia(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai) {
            this.ma = ma;
            this.tenChuongTrinh = tenChuongTrinh;
            this.mucGiam = mucGiam;
            this.ngayBD = ngayBD;
            this.ngayKT = ngayKT;
            this.trangThai = trangThai;
        }
    }
}
