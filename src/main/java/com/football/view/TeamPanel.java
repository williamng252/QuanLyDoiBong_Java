package com.football.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;

import com.football.dao.DoiBongDAO;
import com.football.dao.CauThuDAO;
import com.football.entity.DoiBong;
import com.football.entity.CauThu;
import com.football.entity.HLV;
import com.football.entity.SVD;
import com.football.dao.HLVDAO;
import com.football.dao.SVDDAO;

public class TeamPanel extends JPanel {
    private javax.swing.JLabel labelLogo;
    private DefaultTableModel tableModel;
    private JTable table;
    private DoiBongDAO doiBongDAO = new DoiBongDAO();
    private CauThuDAO cauThuDAO = new CauThuDAO();
    private HLVDAO hlvDAO = new HLVDAO();
    private SVDDAO svdDAO = new SVDDAO();
    
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtYear;
    private JTextField txtLogoPath;
    private JComboBox<HLVItem> cbHLV;
    private JComboBox<SVDItem> cbSVD;

    public TeamPanel() {
        // 1. Khởi tạo đối tượng
        labelLogo = new javax.swing.JLabel();

        // 2. Thiết lập cơ bản (tùy chọn)
        labelLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // 3. Thêm nó vào giao diện của Panel
        this.add(labelLogo);
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        // Header Title
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỘI BÓNG & SÂN VẬN ĐỘNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);

        // --- Table Panel ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = { "Mã Đội", "Logo", "Tên Đội", "Năm TL", "HLV Trưởng", "Sân Nhà", "Điểm", "Hiệu Số" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Icon.class;
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // --- Form Panel ---
        JPanel formPanel = new JPanel(new net.miginfocom.swing.MigLayout("wrap 2", "[right][grow,fill]", "[]15[]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        txtId = new JTextField(15);
        txtName = new JTextField(15);
        txtYear = new JTextField(15);
        txtLogoPath = new JTextField(15);
        txtLogoPath.setEditable(false);
        JButton btnBrowseLogo = new JButton("Chọn...");
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.add(txtLogoPath, BorderLayout.CENTER);
        logoPanel.add(btnBrowseLogo, BorderLayout.EAST);

        cbHLV = new JComboBox<>();
        cbSVD = new JComboBox<>();
        loadCombos();

        formPanel.add(new JLabel("Mã đội (Mới):"));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên đội:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Năm thành lập:"));
        formPanel.add(txtYear);

        formPanel.add(new JLabel("Logo:"));
        formPanel.add(logoPanel);

        formPanel.add(new JLabel("HLV Trưởng:"));
        formPanel.add(cbHLV);
        
        formPanel.add(new JLabel("Sân Nhà:"));
        formPanel.add(cbSVD);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Mới");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa Đội");
        JButton btnViewPlayers = new JButton("Xem danh sách cầu thủ");

        btnBrowseLogo.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                txtLogoPath.setText(f.getAbsolutePath());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtId.setEditable(false); // Can't edit PK
                txtName.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
                txtYear.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                
                DoiBong dbCurrent = doiBongDAO.getByID(txtId.getText());
                txtLogoPath.setText(dbCurrent != null && dbCurrent.getLogo() != null ? dbCurrent.getLogo() : "");
                
                String hlvName = tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "";
                for (int i = 0; i < cbHLV.getItemCount(); i++) {
                    if (cbHLV.getItemAt(i).toString().equals(hlvName)) {
                        cbHLV.setSelectedIndex(i);
                        break;
                    }
                }
                String svdName = tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "";
                for (int i = 0; i < cbSVD.getItemCount(); i++) {
                    if (cbSVD.getItemAt(i).toString().equals(svdName)) {
                        cbSVD.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                txtId.setEditable(true);
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                DoiBong db = new DoiBong();
                db.setMaDoi(txtId.getText().trim());
                db.setTen(txtName.getText().trim());
                db.setNamThanhLap(Integer.parseInt(txtYear.getText().trim()));
                db.setLogo(txtLogoPath.getText().trim());
                
                if (cbHLV.getSelectedItem() != null) db.setHlv(((HLVItem)cbHLV.getSelectedItem()).hlv);
                if (cbSVD.getSelectedItem() != null) db.setSvd(((SVDItem)cbSVD.getSelectedItem()).svd);

                if (doiBongDAO.insert(db)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    SharedData.logActivity("Thêm đội: " + db.getTen());
                    loadData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Mã đội có thể bị trùng.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn 1 dòng để sửa!");
                return;
            }
            try {
                DoiBong db = doiBongDAO.getByID(tableModel.getValueAt(row, 0).toString());
                if (db != null) {
                    db.setTen(txtName.getText().trim());
                    db.setNamThanhLap(Integer.parseInt(txtYear.getText().trim()));
                    db.setLogo(txtLogoPath.getText().trim());
                    if (cbHLV.getSelectedItem() != null) db.setHlv(((HLVItem)cbHLV.getSelectedItem()).hlv);
                    if (cbSVD.getSelectedItem() != null) db.setSvd(((SVDItem)cbSVD.getSelectedItem()).svd);

                    if (doiBongDAO.update(db)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        SharedData.logActivity("Cập nhật đội: " + db.getTen());
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn 1 dòng để xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có trắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DoiBong db = doiBongDAO.getByID(tableModel.getValueAt(row, 0).toString());
                if (db != null && doiBongDAO.delete(db)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    SharedData.logActivity("Xóa đội: " + db.getTen());
                    loadData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        });

        btnViewPlayers.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn 1 đội để xem!");
                return;
            }
            String maDoi = tableModel.getValueAt(row, 0).toString();
            String tenDoi = tableModel.getValueAt(row, 2).toString();
            List<CauThu> players = cauThuDAO.locTheoDoiBong(maDoi);
            
            showPlayersDialog(tenDoi, players);
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        
        formPanel.add(btnViewPlayers, "span 2, center, gaptop 10, wrap");
        formPanel.add(btnPanel, "span 2, right");

        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(formPanel);

        add(splitPane, BorderLayout.CENTER);
        
        loadData();
    }

    
    private void showPlayersDialog(String tenDoi, List<CauThu> players) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cầu thủ đội: " + tenDoi, true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        String[] cols = {"Mã Cầu Thủ", "Tên", "Vị Trí", "Số Áo"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (CauThu c : players) {
            model.addRow(new Object[]{
                c.getMaCT(), c.getTen(), c.getViTri() != null ? c.getViTri().getTenViTri() : "", c.getSoAo()
            });
        }
        
        JTable pTable = new JTable(model);
        dialog.add(new JScrollPane(pTable), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void loadCombos() {
        cbHLV.removeAllItems();
        List<HLV> hlvs = hlvDAO.getAll();
        if (hlvs != null) {
            for (HLV h: hlvs) cbHLV.addItem(new HLVItem(h));
        }
        cbSVD.removeAllItems();
        List<SVD> svds = svdDAO.getAll();
        if (svds != null) {
            for (SVD s: svds) cbSVD.addItem(new SVDItem(s));
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<DoiBong> dbs = doiBongDAO.getAll();
        SharedData.totalTeams = 0;
        if (dbs != null) {
            SharedData.totalTeams = dbs.size();
            for (DoiBong db : dbs) {
                Icon logoIcon = null;
                if (db.getLogo() != null && !db.getLogo().isEmpty()) {
                    // Sử dụng ImageHelper đã tạo để lấy ảnh từ resources
                    ImageIcon icon = ImageHelper.createIcon(db.getLogo());

                    if (icon != null) {
                        // Co giãn ảnh xuống 35x35 để vừa với hàng của JTable
                        Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                        logoIcon = new ImageIcon(img);
                    }
                }
                tableModel.addRow(new Object[]{
                    db.getMaDoi(),
                    logoIcon,
                    db.getTen(),
                    db.getNamThanhLap(),
                    db.getHlv() != null ? db.getHlv().getTenHLV() : "",
                    db.getSvd() != null ? db.getSvd().getTenSan() : "",
                    db.getDiem(),
                    db.getHieuSo()
                });
            }
        }
        SharedData.notifyDashboardChanged();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtYear.setText("");
        txtLogoPath.setText("");
        txtId.setEditable(true);
    }
    
    class HLVItem {
        HLV hlv;
        public HLVItem(HLV h) { this.hlv = h; }
        @Override public String toString() { return hlv.getTenHLV() != null ? hlv.getTenHLV() : hlv.getMaHLV(); }
    }
    
    class SVDItem {
        SVD svd;
        public SVDItem(SVD s) { this.svd = s; }
        @Override public String toString() { return svd.getTenSan() != null ? svd.getTenSan() : svd.getMaSan(); }
    }
    // Giả sử db là đối tượng DoiBong bạn lấy từ Database
    public void updateTeamLogo(DoiBong db) {
        String fileName = db.getLogo(); // Kết quả là "haiphongfc.png"

        // 1. Gọi hàm tạo Icon từ lớp tiện ích ImageHelper
        ImageIcon icon = ImageHelper.createIcon(fileName);

        if (icon != null) {
            // 2. Co giãn ảnh để vừa với JLabel (ví dụ kích thước 150x150)
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            // 3. Gán ảnh đã co giãn vào JLabel của bạn
            labelLogo.setIcon(new ImageIcon(img));
        } else {
            // Nếu không tìm thấy ảnh, có thể gán một ảnh mặc định hoặc xóa icon cũ
            labelLogo.setIcon(null);
            System.out.println("Không tìm thấy logo cho đội: " + db.getTen());
        }
    }
}
