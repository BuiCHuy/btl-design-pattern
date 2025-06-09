package projecttaphoa;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HoaDonPanel extends JPanel {
    private JTextField txtMaHD, txtMaKH_NCC;
    private JFormattedTextField txtNgayLap, txtTongTien;
    private JComboBox<String> cbLoaiHD;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<HoaDon> danhSachHD = new ArrayList<>();

    public HoaDonPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = new JTextField(12);
        txtMaKH_NCC = new JTextField(12);
        txtNgayLap = new JFormattedTextField();
        txtNgayLap.setColumns(12);
        txtTongTien = new JFormattedTextField();
        txtTongTien.setColumns(12);

        cbLoaiHD = new JComboBox<>(new String[]{"Hóa đơn nhập", "Hóa đơn bán"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã hóa đơn:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaHD, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mã KH/NCC:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaKH_NCC, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Ngày lập:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNgayLap, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTongTien, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Loại hóa đơn:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbLoaiHD, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        model = new DefaultTableModel(new String[]{"Mã HD", "Mã KH/NCC", "Ngày lập", "Tổng tiền", "Loại HD"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnThem.addActionListener(e -> themHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0) {
                    HoaDon hd = danhSachHD.get(row);
                    txtMaHD.setText(hd.maHD);
                    txtMaKH_NCC.setText(hd.maKH_NCC);
                    txtNgayLap.setText(hd.ngayLap);
                    txtTongTien.setText(String.valueOf(hd.tongTien));
                    cbLoaiHD.setSelectedItem(hd.loaiHD);
                }
            }
        });
    }

    private void themHoaDon() {
        String maHD = txtMaHD.getText().trim();
        String maKH_NCC = txtMaKH_NCC.getText().trim();
        String ngayLap = txtNgayLap.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();
        String loaiHD = cbLoaiHD.getSelectedItem().toString();

        if(maHD.isEmpty() || maKH_NCC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã hóa đơn và Mã KH/NCC không được để trống!");
            return;
        }

        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(tongTienStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tổng tiền phải là số hợp lệ!");
            return;
        }

        HoaDon hd = new HoaDon(maHD, maKH_NCC, ngayLap, tongTien, loaiHD);
        danhSachHD.add(hd);
        model.addRow(new Object[]{maHD, maKH_NCC, ngayLap, tongTien, loaiHD});
        clearForm();
    }

    private void suaHoaDon() {
        int row = table.getSelectedRow();
        if(row < 0) return;

        String maHD = txtMaHD.getText().trim();
        String maKH_NCC = txtMaKH_NCC.getText().trim();
        String ngayLap = txtNgayLap.getText().trim();
        String tongTienStr = txtTongTien.getText().trim();
        String loaiHD = cbLoaiHD.getSelectedItem().toString();

        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(tongTienStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tổng tiền phải là số hợp lệ!");
            return;
        }

        HoaDon hd = danhSachHD.get(row);
        hd.maHD = maHD;
        hd.maKH_NCC = maKH_NCC;
        hd.ngayLap = ngayLap;
        hd.tongTien = tongTien;
        hd.loaiHD = loaiHD;

        model.setValueAt(maHD, row, 0);
        model.setValueAt(maKH_NCC, row, 1);
        model.setValueAt(ngayLap, row, 2);
        model.setValueAt(tongTien, row, 3);
        model.setValueAt(loaiHD, row, 4);
        clearForm();
    }

    private void xoaHoaDon() {
        int row = table.getSelectedRow();
        if(row < 0) return;
        danhSachHD.remove(row);
        model.removeRow(row);
        clearForm();
    }

    private void clearForm() {
        txtMaHD.setText("");
        txtMaKH_NCC.setText("");
        txtNgayLap.setText("");
        txtTongTien.setText("");
        cbLoaiHD.setSelectedIndex(0);
        table.clearSelection();
    }

    class HoaDon {
        String maHD, maKH_NCC, ngayLap, loaiHD;
        double tongTien;
        public HoaDon(String maHD, String maKH_NCC, String ngayLap, double tongTien, String loaiHD) {
            this.maHD = maHD;
            this.maKH_NCC = maKH_NCC;
            this.ngayLap = ngayLap;
            this.tongTien = tongTien;
            this.loaiHD = loaiHD;
        }
    }
}
