package btldp;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class HoaDonGUI extends JPanel {
    private JTextField txtMaHD, txtTen, txtSoLuong;
    private JComboBox<String> cboLoaiHD;
    private JComboBox<SanPham> cboMatHang;
    private DefaultListModel<String> modelList;
    private JList<String> listChiTiet;
    private List<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
    private JLabel lblTongTien;
    private QLHoaDonPanel qlhd;
    public HoaDonGUI(QLHoaDonPanel qlhd) {
    	this.qlhd = qlhd;
        setLayout(new BorderLayout());

        // Top form
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        cboLoaiHD = new JComboBox<>(new String[]{"Bán", "Nhập"});
        txtMaHD = new JTextField();
        txtTen = new JTextField();
        cboMatHang = new JComboBox<>();
        txtSoLuong = new JTextField(3);

        topPanel.add(new JLabel("Loại hóa đơn:"));
        topPanel.add(cboLoaiHD);
        topPanel.add(new JLabel("Mã hóa đơn:"));
        topPanel.add(txtMaHD);
        topPanel.add(new JLabel("Khách hàng / Nhà cung cấp:"));
        topPanel.add(txtTen);
        topPanel.add(new JLabel("Mặt hàng:"));
        topPanel.add(cboMatHang);

        // Giữa
        JPanel midPanel = new JPanel(new FlowLayout());
        midPanel.add(new JLabel("Số lượng:"));
        midPanel.add(txtSoLuong);
        JButton btnThem = new JButton("Thêm mặt hàng");
        midPanel.add(btnThem);
        
        // Danh sách mặt hàng
        modelList = new DefaultListModel<>();
        listChiTiet = new JList<>(modelList);
        JScrollPane scrollPane = new JScrollPane(listChiTiet);

        // Cuối
        JPanel bottomPanel = new JPanel(new BorderLayout());
        lblTongTien = new JLabel("Tổng tiền: 0");
        JButton btnLuu = new JButton("Lưu hóa đơn");
        JButton btnHuy = new JButton("Hủy đơn");

        bottomPanel.add(lblTongTien, BorderLayout.WEST);
        JPanel groupBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        groupBtn.add(btnLuu, BorderLayout.WEST);
        groupBtn.add(btnHuy, BorderLayout.EAST);
        bottomPanel.add(groupBtn,BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);       
        add(midPanel, BorderLayout.LINE_END);         
        add(bottomPanel, BorderLayout.SOUTH);

        loadMatHang();

        btnThem.addActionListener(e -> themMatHang());
        btnLuu.addActionListener(e -> luuHoaDon());
        btnHuy.addActionListener(e -> huyDon());
        
    }

    private void loadMatHang() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl1", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM product")) {
            while (rs.next()) {
            	SanPham mh;
            	if(rs.getString("type")=="Food") {
                	mh = new SanPhamDoAn(rs.getInt("id"), rs.getString("name"), rs.getDouble("purchase_price"),rs.getDouble("selling_price") ,rs.getString("type"),rs.getDate("date"),rs.getInt("quantity"));
	
            	}
            	else if(rs.getString("type")=="Electronic") {
            		mh = new SanPhamDienTu(rs.getInt("id"), rs.getString("name"), rs.getDouble("purchase_price"),rs.getDouble("selling_price") ,rs.getString("type"),rs.getDate("date"),rs.getInt("quantity"));
            	}
            	else mh = new SanPhamHocTap(rs.getInt("id"), rs.getString("name"), rs.getDouble("purchase_price"),rs.getDouble("selling_price") ,rs.getString("type"),rs.getDate("date"),rs.getInt("quantity"));
                cboMatHang.addItem(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void themMatHang() {
    	if(txtMaHD.getText().isEmpty()||txtTen.getText().isEmpty()||txtSoLuong.getText().isBlank()) {
    		JOptionPane.showMessageDialog(null, "Không được để trống, vui lòng nhập lại","Thông báo",JOptionPane.ERROR_MESSAGE);
    	}
    	else {
    		
            try {
            	SanPham mh = (SanPham) cboMatHang.getSelectedItem();
                if (mh == null) return;

                String loaiHoaDon = (String) cboLoaiHD.getSelectedItem();
                int sl;
                sl = Integer.parseInt(txtSoLuong.getText());
                ChiTietHoaDon ct = new ChiTietHoaDon(txtMaHD.getText(), mh, sl);
                dsChiTiet.add(ct);

                double thanhTien = ct.TinhThanhTien(loaiHoaDon);

                String chiTiet = "Mã MH: " + mh.id +
                        " | Tên: " + mh.name +
                        " | SL: " + sl +
                        " | Đơn giá: " + (loaiHoaDon.equals("Bán") ? mh.sellingPrice : mh.purchasePrice) +
                        " | Thành tiền: " + thanhTien + " VND";

                modelList.addElement(chiTiet);
                capNhatTongTien();
                cboLoaiHD.setEnabled(false);
                txtMaHD.setEnabled(false);
                txtTen.setEnabled(false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ","Thông báo",JOptionPane.ERROR_MESSAGE);
                return;
            }
    	}
    	
    }

    private void capNhatTongTien() {
        String loai = (String) cboLoaiHD.getSelectedItem();
        double tong = 0;
        for (ChiTietHoaDon ct : dsChiTiet) {
            tong += ct.TinhThanhTien(loai);
        }
        lblTongTien.setText("Tổng tiền: " + tong + " VND");
    }

    private void luuHoaDon() {
    	try {
	    	HoaDonData data = new HoaDonData();
	    	data.mahd = Integer.parseInt( txtMaHD.getText());
	        data.loai = (String) cboLoaiHD.getSelectedItem();
	        data.ds = dsChiTiet;
	        if(data.loai=="Bán") {
	        	data.tenkh = txtTen.getText();
	        }
	        else data.nhacungcap = txtTen.getText();
	        
	        
	        HoaDonFactory factory = cboLoaiHD.getSelectedItem() == "Nhập"? new HoaDonNhapFactory() : new HoaDonBanFactory() ;
	        HoaDon hd = factory.TaoHoaDon(data);
	        
	        factory.LuuHoaDon(hd);
	        modelList.clear();
	        JOptionPane.showMessageDialog(null,"Tạo hóa đơn thành công","Thông báo",JOptionPane.INFORMATION_MESSAGE);
	        cboLoaiHD.setEnabled(true);
	        txtMaHD.setEnabled(true);
	        txtTen.setEnabled(true);
	        txtMaHD.setText("");
	        txtTen.setText("");
	        txtSoLuong.setText("");
	        lblTongTien.setText("Tổng tiền: 0 VND");
	        dsChiTiet.clear();
	        qlhd.loadHoaDon();
    	}
		catch(NumberFormatException e) {
    		JOptionPane.showMessageDialog(null,"Mã hóa đơn phải nhập số","Thông báo",JOptionPane.ERROR_MESSAGE);
        	txtMaHD.setEnabled(true);
    	}
        catch(SQLException e) {
        	JOptionPane.showMessageDialog(null, "Mã hóa đơn bị trùng!","Lỗi",JOptionPane.ERROR_MESSAGE);
        	txtMaHD.setEnabled(true);
        }
    }
    private void huyDon() {
        int xacNhan = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn hủy đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (xacNhan == JOptionPane.YES_OPTION) {
            modelList.clear();           
            dsChiTiet.clear();            
            txtMaHD.setText("");           
            txtTen.setText("");
            txtSoLuong.setText("");
            cboLoaiHD.setEnabled(true);    
            txtMaHD.setEnabled(true);
            txtTen.setEnabled(true);
            lblTongTien.setText("Tổng tiền: 0 VND"); 
        }
    }

}
