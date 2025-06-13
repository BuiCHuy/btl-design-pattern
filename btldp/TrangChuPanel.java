package btldp;


import javax.swing.*;
import java.awt.*;

public class TrangChuPanel extends JPanel {
    public TrangChuPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Chào mừng đến với hệ thống quản lý cửa hàng!", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        add(lbl, BorderLayout.CENTER);
    }
}
