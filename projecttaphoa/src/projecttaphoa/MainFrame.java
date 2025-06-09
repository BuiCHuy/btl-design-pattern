package projecttaphoa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Quản lý cửa hàng tạp hóa");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bên trái
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));
        String[] buttons = {
            "Trang chủ", "Tài khoản", "Sản phẩm", "Khách hàng", 
            "Hóa đơn", "Doanh thu", "Phiếu giảm giá"
        };

        contentPanel = new JPanel(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            menuPanel.add(btn);
            btn.addActionListener(e -> switchPanel(btnText));
        }

        switchPanel("Trang chủ"); // Mặc định hiển thị trang chủ
    }

    private void switchPanel(String page) {
        contentPanel.removeAll();
        switch (page) {
            case "Trang chủ" -> contentPanel.add(new TrangChuPanel());
            case "Tài khoản" -> contentPanel.add(new TaiKhoanPanel());
            case "Sản phẩm" -> contentPanel.add(new SanPhamPanel());
            case "Khách hàng" -> contentPanel.add(new KhachHangPanel());
            case "Hóa đơn" -> contentPanel.add(new HoaDonPanel());
            case "Doanh thu" -> contentPanel.add(new DoanhThuPanel());
            case "Phiếu giảm giá" -> contentPanel.add(new PhieuGiamGiaPanel());
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

