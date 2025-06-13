package btldp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private LoginSystem loginSystem;

    public MainFrame(LoginSystem loginSystem) {
        this.loginSystem = loginSystem; // Nhận instance từ LoginFrame
        setTitle("Quản lý cửa hàng tạp hóa");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bên trái
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));
        String[] buttons;

        if (loginSystem.isChuCuaHang()) {
            buttons = new String[]{"Trang chủ", "Tài khoản", "Sản phẩm", "Khách hàng", 
                "Hóa đơn", "Doanh thu", "Phiếu giảm giá", "Đăng xuất"};
        } else {
            buttons = new String[]{"Trang chủ", "Đăng xuất"};
        }

        contentPanel = new JPanel(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            menuPanel.add(btn);
            btn.addActionListener(e -> switchPanel(btnText));
        }

        switchPanel("Trang chủ");
    }

    private void switchPanel(String page) {
        contentPanel.removeAll();
        switch (page) {
            case "Trang chủ" -> {
                if (TrangChuPanel.class.getName() != null) {
                    contentPanel.add(new TrangChuPanel());
                } else {
                    contentPanel.add(new JLabel("Trang chủ chưa được triển khai"));
                }
            }
            case "Tài khoản" -> {
                if (loginSystem.isChuCuaHang()) {
                    contentPanel.add(new TaiKhoanPanel(loginSystem));
                } else {
                    JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!");
                }
            }
            case "Sản phẩm" -> {
                if (SanPhamPanel.class.getName() != null) {
                    contentPanel.add(new SanPhamPanel());
                } else {
                    contentPanel.add(new JLabel("Sản phẩm chưa được triển khai"));
                }
            }
            case "Khách hàng" -> {
                if (KhachHangPanel.class.getName() != null) {
                    contentPanel.add(new KhachHangPanel());
                } else {
                    contentPanel.add(new JLabel("Khách hàng chưa được triển khai"));
                }
            }
            case "Hóa đơn" -> {
                if (HoaDonPanel.class.getName() != null) {
                    contentPanel.add(new HoaDonPanel());
                } else {
                    contentPanel.add(new JLabel("Hóa đơn chưa được triển khai"));
                }
            }
            case "Doanh thu" -> {
                if (DoanhThuPanel.class.getName() != null) {
                    contentPanel.add(new DoanhThuPanel());
                } else {
                    contentPanel.add(new JLabel("Doanh thu chưa được triển khai"));
                }
            }
            case "Phiếu giảm giá" -> {
                if (PhieuGiamGiaPanel.class.getName() != null) {
                    contentPanel.add(new PhieuGiamGiaPanel());
                } else {
                    contentPanel.add(new JLabel("Phiếu giảm giá chưa được triển khai"));
                }
            }
            case "Đăng xuất" -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    loginSystem.logout();
                    dispose();
                    new LoginFrame().setVisible(true); // Sử dụng instance tĩnh
                }
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
