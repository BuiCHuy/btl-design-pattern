package btldp;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class HoaDonPanel extends JPanel{
	HoaDonPanel(){
		setLayout(new BorderLayout());
		QLHoaDonPanel qlhd = new QLHoaDonPanel();
        HoaDonGUI taohd = new HoaDonGUI(qlhd);
        JTabbedPane tabbedPane = new JTabbedPane();     
        tabbedPane.add("Quản lý hóa đơn",qlhd);
        tabbedPane.add("Tạo hóa đơn", taohd);
        add(tabbedPane,BorderLayout.CENTER);
	}
}
