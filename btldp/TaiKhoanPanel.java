package btldp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TaiKhoanPanel extends JPanel {
    private JTextField txtUsername, txtPassword, txtRole;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<TaiKhoanNguoiDung> danhSachTK;
    private LoginSystem loginSystem;

    public TaiKhoanPanel(LoginSystem loginSystem) {
        this.loginSystem = loginSystem;
        this.danhSachTK = loginSystem.getDanhSachTK();
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Panel Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(12);
        txtPassword = new JTextField(12);
        txtRole = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtRole, gbc);

        // Nút chức năng
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");

        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"Username", "Password", "Role"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Load existing accounts
        for (TaiKhoanNguoiDung tk : danhSachTK) {
            model.addRow(new Object[]{tk.getUsername(), tk.getPassword(), 
                tk instanceof TaiKhoanChuCuaHang ? "ChuCuaHang" : "NhanVien"});
        }

        // Sự kiện
        btnThem.addActionListener(e -> themTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtUsername.setText(model.getValueAt(row, 0).toString());
                    txtPassword.setText(model.getValueAt(row, 1).toString());
                    txtRole.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void themTaiKhoan() {
        if (!loginSystem.isChuCuaHang()) {
            JOptionPane.showMessageDialog(this, "Chỉ chủ cửa hàng mới có thể thêm tài khoản!");
            return;
        }

        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String role = txtRole.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (danhSachTK.stream().anyMatch(tk -> tk.getUsername().equalsIgnoreCase(user))) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!");
            return;
        }

        TaiKhoanFactory factory;
        if (role.equalsIgnoreCase("ChuCuaHang")) {
            factory = new TaiKhoanChuCuaHangFactory();
        } else if (role.equalsIgnoreCase("NhanVien")) {
            factory = new TaiKhoanNhanVienFactory();
        } else {
            JOptionPane.showMessageDialog(this, "Vai trò không hợp lệ! (Chỉ chấp nhận ChuCuaHang hoặc NhanVien)");
            return;
        }

        TaiKhoanNguoiDung taiKhoan = factory.taoNguoiDung(user, pass);
        danhSachTK.add(taiKhoan);
        loginSystem.saveToDatabase(taiKhoan); // Lưu vào MySQL
        model.addRow(new Object[]{user, pass, role});
        clearForm();
        JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
    }

    private void xoaTaiKhoan() {
        if (!loginSystem.isChuCuaHang()) {
            JOptionPane.showMessageDialog(this, "Chỉ chủ cửa hàng mới có thể xóa tài khoản!");
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) return;

        TaiKhoanNguoiDung tk = danhSachTK.get(row);
        if (tk == loginSystem.getCurrentUser()) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản đang đăng nhập!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản " + tk.getUsername() + "?", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tk.dangXuat();
            loginSystem.deleteFromDatabase(tk.getUsername()); // Xóa khỏi MySQL
            danhSachTK.remove(row);
            model.removeRow(row);
            clearForm();
            JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtRole.setText("");
        table.clearSelection();
    }
}