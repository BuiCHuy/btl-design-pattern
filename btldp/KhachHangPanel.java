package btldp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class KhachHangPanel extends JPanel {
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JTextArea txtDiaChi;
    private JComboBox<String> cbLoaiKH;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<KhachHangFactory> danhSachKH = new ArrayList<>();
    private ArrayList<Integer> danhSachId = new ArrayList<>();

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

        cbLoaiKH = new JComboBox<>(new String[]{"thường", "vip"});

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
        loadKh();
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
                    KhachHangFactory kh = danhSachKH.get(row);
                    int id = Integer.parseInt((String)table.getValueAt(row, 0));
                    
                   
                    
                    String in4 = kh.getInfo(id);
                    String[] arrin4 = in4.split("\\|");
                    for (int i=0 ; i<arrin4.length ; i++)
                    	System.out.println(arrin4[i]);
                    
                    txtMaKH.setText(arrin4[0]);
                    txtTenKH.setText(arrin4[1]);
                    txtDiaChi.setText(arrin4[2]);
                    txtSDT.setText(arrin4[3]);
                    cbLoaiKH.setSelectedItem(kh.getRank(row));
                }
            }
        });
    }
    
    public void loadKh() {
    	model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl1", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {
            model.setRowCount(0); // Clear table
            while (rs.next()) {
                int ma = rs.getInt("id");
                String hoten = rs.getString("hoten");
                String sdt = rs.getString("sdt");
                String diachi = rs.getString("diachi");
                String loai = rs.getString("rank");
                model.addRow(new Object[]{ma+"", hoten, sdt, diachi, loai});
                
                KhachHangFactory kh = null;
                if(loai.equals("thường")) {
                	kh = new KhachTgFac();
                }
                else if(loai.equals("vip")) {
                	kh = new KhachVipFac();
                }
                danhSachKH.add(kh);
                danhSachId.add(ma);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        
        KhachHangData kh = new KhachHangData();
        kh.id = Integer.parseInt(ma);
        kh.hoten = ten;
        kh.diachi = diachi;
        kh.sdt = sdt;
        kh.rank = loai;
        
        // sd factory method
        KhachHangFactory khfac = null;
        if(loai == "thường") {
        	khfac = new KhachTgFac();
        	khfac.taoKhachHang(kh);
        	JOptionPane.showMessageDialog(this, "tạo khách tg thành công");
        }
        else if (loai == "vip") {
        	khfac = new KhachVipFac();
        	khfac.taoKhachHang(kh);
        	JOptionPane.showMessageDialog(this, "tạo khách vip thành công");
        }
        
        danhSachKH.add(khfac);
        danhSachId.add(kh.id);
        try {
			KhachHangDB.ThemKh(kh.id, ten, diachi, sdt, loai);
		} catch (Exception e) {e.printStackTrace();}
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
        int id = Integer.parseInt(ma);
        
        try {
			KhachHangDB.SuaKh(id, ten, diachi, sdt, loai);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        KhachHangFactory kh = danhSachKH.get(row);
        
        if(kh.getRank(id) != loai) {
        	kh = null;
        	if(loai == "thường")
        		kh = new KhachTgFac();
        	else
        		kh = new KhachVipFac();
        }
        		
        String in4 = kh.getInfo(id);
        String[] arrin4 = in4.split("\\|");

        String mams = arrin4[0];
        String tenms = arrin4[1];
        String sdtms = arrin4[3];
        String diachims = arrin4[2];
        String loaims = kh.getRank(id);

        model.setValueAt(mams, row, 0);
        model.setValueAt(tenms, row, 1);
        model.setValueAt(sdtms, row, 2);
        model.setValueAt(diachims, row, 3);
        model.setValueAt(loaims, row, 4);
        clearForm();
        JOptionPane.showMessageDialog(null, "Sửa thành công");
    }

    private void xoaKhachHang() {
    	int result = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa dữ liệu này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Dữ liệu đã được xóa.");
        } else {
            return;
        }
        int row = table.getSelectedRow();
        if(row < 0) return;
        int id = danhSachId.get(row);
        
        try {
			KhachHangDB.XoaKh(id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "gặp lỗi khi xóa");
			e.printStackTrace();
		}
        
        danhSachKH.remove(row);
        danhSachId.remove(row);
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
    

}