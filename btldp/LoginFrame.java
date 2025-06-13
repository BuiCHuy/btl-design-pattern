package btldp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        LoginSystem loginSystem = LoginSystem.getInstance(); // Sử dụng instance tĩnh
        setTitle("Đăng nhập");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel(""));
        btnLogin = new JButton("Đăng nhập");
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> login(loginSystem));
    }

    private void login(LoginSystem loginSystem) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (loginSystem.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose();
            new MainFrame(loginSystem).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}