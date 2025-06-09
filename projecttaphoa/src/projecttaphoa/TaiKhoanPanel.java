package projecttaphoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TaiKhoanPanel extends JPanel {
    private JTextField txtUsername, txtPassword, txtRole;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<TaiKhoan> danhSachTK = new ArrayList<>();

    public TaiKhoanPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // ==== Panel Form gọn gàng ====
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

        // ==== Nút chức năng ====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ==== Bảng dữ liệu ====
        model = new DefaultTableModel(new String[]{"Username", "Password", "Role"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==== Sự kiện ====
        btnThem.addActionListener(e -> themTaiKhoan());
        btnSua.addActionListener(e -> suaTaiKhoan());
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
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String role = txtRole.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        danhSachTK.add(new TaiKhoan(user, pass, role));
        model.addRow(new Object[]{user, pass, role});
        clearForm();
    }

    private void suaTaiKhoan() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String role = txtRole.getText().trim();

        TaiKhoan tk = danhSachTK.get(row);
        tk.setUsername(user);
        tk.setPassword(pass);
        tk.setRole(role);

        model.setValueAt(user, row, 0);
        model.setValueAt(pass, row, 1);
        model.setValueAt(role, row, 2);
        clearForm();
    }

    private void xoaTaiKhoan() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        danhSachTK.remove(row);
        model.removeRow(row);
        clearForm();
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtRole.setText("");
        table.clearSelection();
    }

    // ==== Class model tài khoản ====
    class TaiKhoan {
        private String username, password, role;

        public TaiKhoan(String u, String p, String r) {
            username = u;
            password = p;
            role = r;
        }

        public void setUsername(String u) { username = u; }
        public void setPassword(String p) { password = p; }
        public void setRole(String r) { role = r; }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }
}
