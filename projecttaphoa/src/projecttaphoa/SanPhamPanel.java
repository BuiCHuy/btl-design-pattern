package projecttaphoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SanPhamPanel extends JPanel {
    private JTextField txtMaSP, txtTenSP, txtGia, txtLoai;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<SanPham> danhSachSP = new ArrayList<>();

    public SanPhamPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // ==== Form nhập liệu ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSP = new JTextField(12);
        txtTenSP = new JTextField(12);
        txtGia = new JTextField(12);
        txtLoai = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã SP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaSP, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên SP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenSP, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtGia, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLoai, gbc);

        // ==== Nút chức năng ====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ==== Bảng dữ liệu ====
        model = new DefaultTableModel(new String[]{"Mã SP", "Tên SP", "Giá", "Loại"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==== Sự kiện ====
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(model.getValueAt(row, 0).toString());
                    txtTenSP.setText(model.getValueAt(row, 1).toString());
                    txtGia.setText(model.getValueAt(row, 2).toString());
                    txtLoai.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void themSanPham() {
        String ma = txtMaSP.getText().trim();
        String ten = txtTenSP.getText().trim();
        String giaStr = txtGia.getText().trim();
        String loai = txtLoai.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || giaStr.isEmpty() || loai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        double gia;
        try {
            gia = Double.parseDouble(giaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số!");
            return;
        }

        danhSachSP.add(new SanPham(ma, ten, gia, loai));
        model.addRow(new Object[]{ma, ten, gia, loai});
        clearForm();
    }

    private void suaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String ma = txtMaSP.getText().trim();
        String ten = txtTenSP.getText().trim();
        String giaStr = txtGia.getText().trim();
        String loai = txtLoai.getText().trim();

        double gia;
        try {
            gia = Double.parseDouble(giaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số!");
            return;
        }

        SanPham sp = danhSachSP.get(row);
        sp.setMa(ma);
        sp.setTen(ten);
        sp.setGia(gia);
        sp.setLoai(loai);

        model.setValueAt(ma, row, 0);
        model.setValueAt(ten, row, 1);
        model.setValueAt(gia, row, 2);
        model.setValueAt(loai, row, 3);
        clearForm();
    }

    private void xoaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        danhSachSP.remove(row);
        model.removeRow(row);
        clearForm();
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtLoai.setText("");
        table.clearSelection();
    }

    // ==== Class model sản phẩm ====
    class SanPham {
        private String ma, ten, loai;
        private double gia;

        public SanPham(String ma, String ten, double gia, String loai) {
            this.ma = ma;
            this.ten = ten;
            this.gia = gia;
            this.loai = loai;
        }

        public void setMa(String ma) { this.ma = ma; }
        public void setTen(String ten) { this.ten = ten; }
        public void setGia(double gia) { this.gia = gia; }
        public void setLoai(String loai) { this.loai = loai; }
    }
}

