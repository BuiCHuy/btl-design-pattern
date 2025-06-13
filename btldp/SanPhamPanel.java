package btldp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SanPhamPanel extends JPanel {
    private JTextField txtId, txtName, txtPurchasePrice, txtSellingPrice, txtDate, txtQuantity;
    private JComboBox<String> cbType;
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<SanPham> danhSachSP = new ArrayList<>();
    private Connection conn;

    public SanPhamPanel() {
        initDatabaseConnection();
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // ==== Form nhập liệu ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(12);
        txtName = new JTextField(12);
        txtPurchasePrice = new JTextField(12);
        txtSellingPrice = new JTextField(12);
        txtDate = new JTextField(12); // Format: yyyy-MM-dd
        txtQuantity = new JTextField(12);
        cbType = new JComboBox<>(new String[]{"Food", "Electronic", "Learning"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Purchase Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPurchasePrice, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Selling Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtSellingPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbType, gbc);

        // ==== Nút chức năng ====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnTaiLai = new JButton("Tải lại");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnTaiLai);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ==== Bảng dữ liệu ====
        model = new DefaultTableModel(new String[]{"ID", "Name", "Purchase Price", "Selling Price", "Type", "Date", "Quantity", "Profit"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==== Sự kiện ====
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnTaiLai.addActionListener(e -> taiLaiDuLieu());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtPurchasePrice.setText(model.getValueAt(row, 2).toString());
                    txtSellingPrice.setText(model.getValueAt(row, 3).toString());
                    cbType.setSelectedItem(model.getValueAt(row, 4).toString());
                    txtDate.setText(model.getValueAt(row, 5).toString());
                    txtQuantity.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        loadDataFromDatabase();
    }

    private void initDatabaseConnection() {
        String url = "jdbc:mysql://localhost:3306/btl1";
        String user = "root";
        String password = "";
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kết nối cơ sở dữ liệu thất bại: " + e.getMessage());
        }
    }

    private void themSanPham() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String purchasePriceStr = txtPurchasePrice.getText().trim();
        String sellingPriceStr = txtSellingPrice.getText().trim();
        String dateStr = txtDate.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String type = (String) cbType.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || purchasePriceStr.isEmpty() || sellingPriceStr.isEmpty() || dateStr.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        double purchasePrice, sellingPrice;
        int quantity;
        Date date = null;
        try {
            purchasePrice = Double.parseDouble(purchasePriceStr);
            sellingPrice = Double.parseDouble(sellingPriceStr);
            quantity = Integer.parseInt(quantityStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(dateStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số!");
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày phải đúng định dạng yyyy-MM-dd!");
            return;
        }

        SanPhamFactory factory;
        switch (type.toLowerCase()) {
            case "food":
                factory = new SanPhamDoAnFactory();
                break;
            case "electronic":
                factory = new SanPhamDienTuFactory();
                break;
            case "learning":
                factory = new SanPhamHocTapFactory();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Loại sản phẩm không hợp lệ!");
                return;
        }

        SanPham SanPham = factory.taoSanPham();
        if (SanPham instanceof SanPhamDoAn) {
            ((SanPhamDoAn) SanPham).setId(Integer.parseInt(id));
            ((SanPhamDoAn) SanPham).setName(name);
            ((SanPhamDoAn) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamDoAn) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamDoAn) SanPham).setType(type);
            ((SanPhamDoAn) SanPham).setDate(date);
            ((SanPhamDoAn) SanPham).setQuantity(quantity);
        } else if (SanPham instanceof SanPhamDienTu) {
            ((SanPhamDienTu) SanPham).setId(Integer.parseInt(id));
            ((SanPhamDienTu) SanPham).setName(name);
            ((SanPhamDienTu) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamDienTu) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamDienTu) SanPham).setType(type);
            ((SanPhamDienTu) SanPham).setDate(date);
            ((SanPhamDienTu) SanPham).setQuantity(quantity);
        } else if (SanPham instanceof SanPhamHocTap) {
            ((SanPhamHocTap) SanPham).setId(Integer.parseInt(id));
            ((SanPhamHocTap) SanPham).setName(name);
            ((SanPhamHocTap) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamHocTap) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamHocTap) SanPham).setType(type);
            ((SanPhamHocTap) SanPham).setDate(date);
            ((SanPhamHocTap) SanPham).setQuantity(quantity);
        }

        try {
            String sql = "INSERT INTO product (id, name, purchase_price, selling_price, type, date, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setDouble(3, purchasePrice);
            pstmt.setDouble(4, sellingPrice);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(5, type);
            pstmt.setString(6, dateFormat.format(date));
            pstmt.setInt(7, quantity);
            pstmt.executeUpdate();
            danhSachSP.add(SanPham);
            model.addRow(new Object[]{id, name, purchasePrice, sellingPrice, type, dateFormat.format(date), quantity, factory.tinhLoiNhuan(SanPham)});
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        clearForm();
    }

    private void suaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String purchasePriceStr = txtPurchasePrice.getText().trim();
        String sellingPriceStr = txtSellingPrice.getText().trim();
        String dateStr = txtDate.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String type = (String) cbType.getSelectedItem();

        double purchasePrice, sellingPrice;
        int quantity;
        Date date = null;
        try {
            purchasePrice = Double.parseDouble(purchasePriceStr);
            sellingPrice = Double.parseDouble(sellingPriceStr);
            quantity = Integer.parseInt(quantityStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(dateStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số!");
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày phải đúng định dạng yyyy-MM-dd!");
            return;
        }

        SanPham SanPham = danhSachSP.get(row);
        SanPhamFactory factory;
        if (SanPham instanceof SanPhamDoAn) {
            ((SanPhamDoAn) SanPham).setId(Integer.parseInt(id));
            ((SanPhamDoAn) SanPham).setName(name);
            ((SanPhamDoAn) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamDoAn) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamDoAn) SanPham).setType(type);
            ((SanPhamDoAn) SanPham).setDate(date);
            ((SanPhamDoAn) SanPham).setQuantity(quantity);
            factory = new SanPhamDoAnFactory();
        } else if (SanPham instanceof SanPhamDienTu) {
            ((SanPhamDienTu) SanPham).setId(Integer.parseInt(id));
            ((SanPhamDienTu) SanPham).setName(name);
            ((SanPhamDienTu) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamDienTu) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamDienTu) SanPham).setType(type);
            ((SanPhamDienTu) SanPham).setDate(date);
            ((SanPhamDienTu) SanPham).setQuantity(quantity);
            factory = new SanPhamDienTuFactory();
        } else if (SanPham instanceof SanPhamHocTap) {
            ((SanPhamHocTap) SanPham).setId(Integer.parseInt(id));
            ((SanPhamHocTap) SanPham).setName(name);
            ((SanPhamHocTap) SanPham).setPurchasePrice(purchasePrice);
            ((SanPhamHocTap) SanPham).setSellingPrice(sellingPrice);
            ((SanPhamHocTap) SanPham).setType(type);
            ((SanPhamHocTap) SanPham).setDate(date);
            ((SanPhamHocTap) SanPham).setQuantity(quantity);
            factory = new SanPhamHocTapFactory();
        } else {
            return; // Trường hợp không hợp lệ
        }

        try {
            String sql = "UPDATE product SET name=?, purchase_price=?, selling_price=?, type=?, date=?, quantity=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, purchasePrice);
            pstmt.setDouble(3, sellingPrice);
            pstmt.setString(4, type);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(5, dateFormat.format(date));
            pstmt.setInt(6, quantity);
            pstmt.setString(7, id);
            pstmt.executeUpdate();
            model.setValueAt(id, row, 0);
            model.setValueAt(name, row, 1);
            model.setValueAt(purchasePrice, row, 2);
            model.setValueAt(sellingPrice, row, 3);
            model.setValueAt(type, row, 4);
            model.setValueAt(dateFormat.format(date), row, 5);
            model.setValueAt(quantity, row, 6);
            model.setValueAt(factory.tinhLoiNhuan(SanPham), row, 7);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa sản phẩm: " + e.getMessage());
        }
        clearForm();
    }

    private void xoaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String id = model.getValueAt(row, 0).toString();
        try {
            String sql = "DELETE FROM product WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            danhSachSP.remove(row);
            model.removeRow(row);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        clearForm();
    }

    private void taiLaiDuLieu() {
        model.setRowCount(0);
        danhSachSP.clear();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        try {
            String sql = "SELECT id, name, purchase_price, selling_price, type, date, quantity FROM product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double purchasePrice = rs.getDouble("purchase_price");
                double sellingPrice = rs.getDouble("selling_price");
                String type = rs.getString("type");
                Date date = rs.getDate("date");
                int quantity = rs.getInt("quantity");
                SanPhamFactory factory;
                switch (type.toLowerCase()) {
                    case "food":
                        factory = new SanPhamDoAnFactory();
                        break;
                    case "electronic":
                        factory = new SanPhamDienTuFactory();
                        break;
                    case "learning":
                        factory = new SanPhamHocTapFactory();
                        break;
                    default:
                        factory = new SanPhamDoAnFactory(); // Default case
                }
                SanPham SanPham = factory.taoSanPham();
                if (SanPham instanceof SanPhamDoAn) {
                    ((SanPhamDoAn) SanPham).setId(Integer.parseInt(id));
                    ((SanPhamDoAn) SanPham).setName(name);
                    ((SanPhamDoAn) SanPham).setPurchasePrice(purchasePrice);
                    ((SanPhamDoAn) SanPham).setSellingPrice(sellingPrice);
                    ((SanPhamDoAn) SanPham).setType(type);
                    ((SanPhamDoAn) SanPham).setDate(date);
                    ((SanPhamDoAn) SanPham).setQuantity(quantity);
                } else if (SanPham instanceof SanPhamDienTu) {
                    ((SanPhamDienTu) SanPham).setId(Integer.parseInt(id));
                    ((SanPhamDienTu) SanPham).setName(name);
                    ((SanPhamDienTu) SanPham).setPurchasePrice(purchasePrice);
                    ((SanPhamDienTu) SanPham).setSellingPrice(sellingPrice);
                    ((SanPhamDienTu) SanPham).setType(type);
                    ((SanPhamDienTu) SanPham).setDate(date);
                    ((SanPhamDienTu) SanPham).setQuantity(quantity);
                } else if (SanPham instanceof SanPhamHocTap) {
                    ((SanPhamHocTap) SanPham).setId(Integer.parseInt(id));
                    ((SanPhamHocTap) SanPham).setName(name);
                    ((SanPhamHocTap) SanPham).setPurchasePrice(purchasePrice);
                    ((SanPhamHocTap) SanPham).setSellingPrice(sellingPrice);
                    ((SanPhamHocTap) SanPham).setType(type);
                    ((SanPhamHocTap) SanPham).setDate(date);
                    ((SanPhamHocTap) SanPham).setQuantity(quantity);
                }
                danhSachSP.add(SanPham);
                model.addRow(new Object[]{id, name, purchasePrice, sellingPrice, type, dateFormat.format(date), quantity, factory.tinhLoiNhuan(SanPham)});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPurchasePrice.setText("");
        txtSellingPrice.setText("");
        txtDate.setText("");
        txtQuantity.setText("");
        cbType.setSelectedIndex(0);
        table.clearSelection();
    }
}