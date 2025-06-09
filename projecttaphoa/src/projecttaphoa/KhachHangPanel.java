package projecttaphoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class KhachHangPanel extends JPanel {
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JTextArea txtDiaChi;
    private JComboBox<String> cbLoaiKH;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<KhachHang> danhSachKH = new ArrayList<>();

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Form panel với GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaKH = new JTextField(12);
        txtTenKH = new JTextField(12);
        txtSDT = new JTextField(12);
        txtDiaChi = new JTextArea(2, 12);
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setWrapStyleWord(true);
        JScrollPane scrollDiaChi = new JScrollPane(txtDiaChi);

        cbLoaiKH = new JComboBox<>(new String[]{"Khách thường", "Khách VIP"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaKH, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên KH:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenKH, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(scrollDiaChi, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Loại KH:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbLoaiKH, gbc);

        // Nút chức năng
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

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"Mã KH", "Tên KH", "SĐT", "Địa chỉ", "Loại KH"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện nút
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());

        // Chọn dòng bảng để load lên form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0) {
                    KhachHang kh = danhSachKH.get(row);
                    txtMaKH.setText(kh.ma);
                    txtTenKH.setText(kh.ten);
                    txtSDT.setText(kh.sdt);
                    txtDiaChi.setText(kh.diaChi);
                    cbLoaiKH.setSelectedItem(kh.loai);
                }
            }
        });
    }

    private void themKhachHang() {
        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diachi = txtDiaChi.getText().trim();
        String loai = cbLoaiKH.getSelectedItem().toString();

        if(ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và Tên khách hàng không được để trống!");
            return;
        }

        KhachHang kh = new KhachHang(ma, ten, sdt, diachi, loai);
        danhSachKH.add(kh);
        model.addRow(new Object[]{ma, ten, sdt, diachi, loai});
        clearForm();
    }

    private void suaKhachHang() {
        int row = table.getSelectedRow();
        if(row < 0) return;

        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diachi = txtDiaChi.getText().trim();
        String loai = cbLoaiKH.getSelectedItem().toString();

        KhachHang kh = danhSachKH.get(row);
        kh.ma = ma; kh.ten = ten; kh.sdt = sdt; kh.diaChi = diachi; kh.loai = loai;

        model.setValueAt(ma, row, 0);
        model.setValueAt(ten, row, 1);
        model.setValueAt(sdt, row, 2);
        model.setValueAt(diachi, row, 3);
        model.setValueAt(loai, row, 4);
        clearForm();
    }

    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if(row < 0) return;
        danhSachKH.remove(row);
        model.removeRow(row);
        clearForm();
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        cbLoaiKH.setSelectedIndex(0);
        table.clearSelection();
    }

    // Class model Khách hàng
    class KhachHang {
        String ma, ten, sdt, diaChi, loai;
        public KhachHang(String ma, String ten, String sdt, String diaChi, String loai) {
            this.ma = ma; this.ten = ten; this.sdt = sdt; this.diaChi = diaChi; this.loai = loai;
        }
    }
}

