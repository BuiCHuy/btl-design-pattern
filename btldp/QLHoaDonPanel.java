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
                String mahd = (String) modelHD.getValueAt(row, 0);
                loadChiTiet(mahd);
            }
        });
    }

    public void loadHoaDon() {
    	modelHD.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hoadon")) {
            modelHD.setRowCount(0); // Clear table
            while (rs.next()) {
                String mahd = rs.getString("mahd");
                String loai = rs.getString("loai");
                String doiTac = rs.getString("doitac");
                double tong = rs.getDouble("tongtien");
                modelHD.addRow(new Object[]{mahd, loai, doiTac, tong});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadChiTiet(String mahd) {
    	modelCT.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/btl", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ct.mathang_id, mh.loai, ct.soluong, " +
                             "IF(hd.loai='Bán', mh.giaban, mh.gianhap) AS dongia, " +
                             "ct.soluong * IF(hd.loai='Bán', mh.giaban, mh.gianhap) AS thanhtien " +
                             "FROM chitiethoadon ct " +
                             "JOIN mathang mh ON ct.mathang_id = mh.id " +
                             "JOIN hoadon hd ON ct.mahd = hd.mahd " +
                             "WHERE ct.mahd = ?")) {
            stmt.setString(1, mahd);
            ResultSet rs = stmt.executeQuery();
            modelCT.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("mathang_id");
                String ten = rs.getString("loai");
                int sl = rs.getInt("soluong");
                double dongia = rs.getDouble("dongia");
                double tt = rs.getDouble("thanhtien");
                modelCT.addRow(new Object[]{id, ten, sl, dongia, tt});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
