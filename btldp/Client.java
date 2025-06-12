package btldp;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý hóa đơn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            QLHoaDonPanel qlhd = new QLHoaDonPanel();
            HoaDonGUI taohd = new HoaDonGUI(qlhd);
            JTabbedPane tabbedPane = new JTabbedPane();
            
            tabbedPane.add("Quản lý hóa đơn",qlhd);
            
            tabbedPane.add("Tạo hóa đơn", taohd);
            frame.add(tabbedPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
	}

}
