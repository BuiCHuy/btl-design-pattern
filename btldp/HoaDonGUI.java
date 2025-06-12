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
    private JComboBox<MatHang> cboMatHang;
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
        bottomPanel.add(lblTongTien, BorderLayout.WEST);
        bottomPanel.add(btnLuu, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);       
        add(midPanel, BorderLayout.LINE_END);         
        add(bottomPanel, BorderLayout.SOUTH);

        loadMatHang();

        btnThem.addActionListener(e -> themMatHang());
        btnLuu.addActionListener(e -> luuHoaDon());
    }

    private void loadMatHang() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM mathang")) {
            while (rs.next()) {
                MatHang mh = new MatHang(rs.getInt("id"), rs.getString("loai"), rs.getDouble("giaban"), rs.getDouble("gianhap"));
                cboMatHang.addItem(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void themMatHang() {
    	MatHang mh = (MatHang) cboMatHang.getSelectedItem();
        if (mh == null) return;

        String loaiHoaDon = (String) cboLoaiHD.getSelectedItem();
        int sl;
        try {
            sl = Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ");
            return;
        }

        ChiTietHoaDon ct = new ChiTietHoaDon(txtMaHD.getText(), mh, sl);
        dsChiTiet.add(ct);

        double thanhTien = ct.TinhThanhTien(loaiHoaDon);

        String chiTiet = "Mã MH: " + mh.id +
                " | Tên: " + mh.tenmh +
                " | SL: " + sl +
                " | Đơn giá: " + (loaiHoaDon.equals("Bán") ? mh.giaban : mh.gianhap) +
                " | Thành tiền: " + thanhTien + " VND";

        modelList.addElement(chiTiet);
        capNhatTongTien();
        cboLoaiHD.setEnabled(false);
        txtMaHD.setEnabled(false);
        txtTen.setEnabled(false);
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
    	HoaDonData data = new HoaDonData();
        data.mahd = txtMaHD.getText();
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
}
