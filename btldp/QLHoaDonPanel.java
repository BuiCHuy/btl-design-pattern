package btldp;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;

public class QLHoaDonPanel extends JPanel {
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHD, modelCT;

    public QLHoaDonPanel() {
        setLayout(new BorderLayout());

        // Bảng hóa đơn
        modelHD = new DefaultTableModel(new String[]{"Mã HĐ", "Loại", "Đối tác", "Tổng tiền"}, 0);
        tblHoaDon = new JTable(modelHD);
        JScrollPane spHD = new JScrollPane(tblHoaDon);
        spHD.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));

        // Bảng chi tiết hóa đơn
        modelCT = new DefaultTableModel(new String[]{"Mã mặt hàng", "Tên", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
        tblChiTiet = new JTable(modelCT);
        JScrollPane spCT = new JScrollPane(tblChiTiet);
        spCT.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        // Panel hiển thị
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spHD, spCT);
        split.setDividerLocation(200);
        add(split, BorderLayout.CENTER);

        loadHoaDon();

        // Xử lý khi chọn 1 hóa đơn
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            int row = tblHoaDon.getSelectedRow();
            if (row >= 0) {
                int mahd =  Integer.parseUnsignedInt( (String) modelHD.getValueAt(row, 0));
                loadChiTiet(mahd);
            }
        });
    }

    public void loadHoaDon() {
    	modelHD.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl1", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM invoice")) {
            modelHD.setRowCount(0); // Clear table
            while (rs.next()) {
                String mahd = rs.getString("id");
                String loai = rs.getString("type");
                String doiTac = rs.getString("partner");
                double tong = rs.getDouble("total");
                modelHD.addRow(new Object[]{mahd, loai, doiTac, tong});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadChiTiet(int mahd) {
    	modelCT.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl1", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT \r\n"
                     + "    idt.product_id,\r\n"
                     + "    p.name,\r\n"
                     + "    idt.quantity,\r\n"
                     + "    IF(i.type = 'Sale', p.selling_price, p.purchase_price) AS unit_price,\r\n"
                     + "    idt.quantity * IF(i.type = 'Sale', p.selling_price, p.purchase_price) AS total_price\r\n"
                     + "FROM invoice_detail idt\r\n"
                     + "JOIN product p ON idt.product_id = p.id\r\n"
                     + "JOIN invoice i ON idt.invoice_id = i.id\r\n"
                     + "WHERE idt.invoice_id = ?\r\n"
                     + "")) {
            stmt.setInt(1, mahd);
            ResultSet rs = stmt.executeQuery();
            modelCT.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String ten = rs.getString("name");
                int sl = rs.getInt("quantity");
                double dongia = rs.getDouble("unit_price");
                double tt = rs.getDouble("total_price");
                modelCT.addRow(new Object[]{id, ten, sl, dongia, tt});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
