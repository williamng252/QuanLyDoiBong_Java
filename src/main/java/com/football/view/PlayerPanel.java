package com.football.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

import com.football.dao.CauThuDAO;
import com.football.dao.DoiBongDAO;
import com.football.dao.QuocTichDAO;
import com.football.dao.ViTriDAO;
import com.football.entity.CauThu;
import com.football.entity.DoiBong;
import com.football.entity.HopDong;
import com.football.entity.QuocTich;
import com.football.entity.ViTri;

public class PlayerPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    
    private CauThuDAO cauThuDAO = new CauThuDAO();
    private DoiBongDAO doiBongDAO = new DoiBongDAO();
    private QuocTichDAO quocTichDAO = new QuocTichDAO();
    private ViTriDAO viTriDAO = new ViTriDAO();
    
    private JComboBox<DoiBongItem> filterCbTeam;
    private JComboBox<QuocTichItem> filterCbNation;
    private JTextField txtSearch;
    private JTextField txtSearchSoAo;

    private JTextField txtId;
    private JTextField txtName;
    private JSpinner spinSoAo;
    private JSpinner spinDob;
    private JTextField txtChieuCao;
    private JTextField txtCanNang;
    private JComboBox<DoiBongItem> cbTeam;
    private JComboBox<QuocTichItem> cbNation;
    private JComboBox<ViTriItem> cbPosition;

    public static PlayerPanel instance;

    public PlayerPanel() {
        instance = this;
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        // Header Title
        JLabel lblTitle = new JLabel("QUẢN LÝ CẦU THỦ & CHUYỂN NHƯỢNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.65);

        // --- Table Panel (Left) ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new MigLayout("wrap 5", "[][grow,fill][][grow,fill][]"));
        filterCbTeam = new JComboBox<>();
        filterCbNation = new JComboBox<>();
        txtSearch = new JTextField(15);
        txtSearchSoAo = new JTextField(5);
        
        JButton btnSearch = new JButton("Tìm tên");
        JButton btnFilterTeam = new JButton("Lọc Đội");
        JButton btnFilterNation = new JButton("Lọc QT");
        JButton btnSearchSoAo = new JButton("Tìm SC");
        JButton btnClearFilter = new JButton("Khôi phục (Reset)");

        filterPanel.add(new JLabel("Tên Cầu Thủ:"), "right");
        filterPanel.add(txtSearch);
        filterPanel.add(btnSearch, "wrap");
        
        filterPanel.add(new JLabel("Số Áo:"), "right");
        filterPanel.add(txtSearchSoAo);
        filterPanel.add(btnSearchSoAo, "wrap");

        filterPanel.add(new JLabel("Đội Bóng:"), "right");
        filterPanel.add(filterCbTeam);
        filterPanel.add(btnFilterTeam, "wrap");

        filterPanel.add(new JLabel("Quốc Tịch:"), "right");
        filterPanel.add(filterCbNation);
        filterPanel.add(btnFilterNation);
        filterPanel.add(btnClearFilter, "wrap");
        
        tableContainer.add(filterPanel, BorderLayout.NORTH);

        String[] columns = { "Mã CT", "Tên Cầu Thủ", "Số Áo", "Đội", "Quốc Tịch", "Vị Trí" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        // Nút Chuyển nhượng ngay bên dưới bảng
        JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTransfer = new JButton("Chuyển Nhượng (Ký mới)");
        btnTransfer.setBackground(new Color(52, 152, 219));
        btnTransfer.setForeground(Color.WHITE);
        btnTransfer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        transferPanel.add(btnTransfer);
        tableContainer.add(transferPanel, BorderLayout.SOUTH);

        // --- Form Panel (Right) ---
        JPanel formPanel = new JPanel(new MigLayout("wrap 2", "[right][grow,fill]", "[]12[]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        txtId = new JTextField(15);
        txtName = new JTextField(15);
        spinSoAo = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        
        spinDob = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinDob, "dd/MM/yyyy");
        spinDob.setEditor(dateEditor);
        
        txtChieuCao = new JTextField(10);
        txtCanNang = new JTextField(10);
        
        cbTeam = new JComboBox<>();
        cbNation = new JComboBox<>();
        cbPosition = new JComboBox<>();
        
        loadCombos();

        formPanel.add(new JLabel("Mã cầu thủ:"));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên cầu thủ:"));
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("Ngày Sinh:"));
        formPanel.add(spinDob);

        formPanel.add(new JLabel("Chiều Cao (cm):"));
        formPanel.add(txtChieuCao);
        
        formPanel.add(new JLabel("Cân Nặng (kg):"));
        formPanel.add(txtCanNang);

        formPanel.add(new JLabel("Số áo:"));
        formPanel.add(spinSoAo);

        formPanel.add(new JLabel("Đội bóng:"));
        formPanel.add(cbTeam);

        formPanel.add(new JLabel("Quốc tịch:"));
        formPanel.add(cbNation);

        formPanel.add(new JLabel("Vị trí:"));
        formPanel.add(cbPosition);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Mới");
        JButton btnUpdate = new JButton("Cập Nhật");
        JButton btnDelete = new JButton("Xóa");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        formPanel.add(btnPanel, "span 2, right");

        splitPane.setLeftComponent(tableContainer);
        splitPane.setRightComponent(formPanel);

        add(splitPane, BorderLayout.CENTER);

        // --- EVENTS ---
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String maCT = tableModel.getValueAt(row, 0).toString();
                CauThu ct = cauThuDAO.getByID(maCT);
                if (ct != null) {
                    txtId.setText(ct.getMaCT());
                    txtId.setEditable(false);
                    txtName.setText(ct.getTen());
                    spinSoAo.setValue(ct.getSoAo());
                    if (ct.getNgaySinh() != null) spinDob.setValue(ct.getNgaySinh());
                    txtChieuCao.setText(String.valueOf(ct.getChieuCao()));
                    txtCanNang.setText(String.valueOf(ct.getCanNang()));
                    
                    if (ct.getDoiBong() != null) {
                        for (int i=0; i<cbTeam.getItemCount(); i++) {
                            if (cbTeam.getItemAt(i).db.getMaDoi().equals(ct.getDoiBong().getMaDoi())) { cbTeam.setSelectedIndex(i); break; }
                        }
                    }
                    if (ct.getQuocTich() != null) {
                        for (int i=0; i<cbNation.getItemCount(); i++) {
                            if (cbNation.getItemAt(i).qt.getMaQT().equals(ct.getQuocTich().getMaQT())) { cbNation.setSelectedIndex(i); break; }
                        }
                    }
                    if (ct.getViTri() != null) {
                        for (int i=0; i<cbPosition.getItemCount(); i++) {
                            if (cbPosition.getItemAt(i).vt.getMaVT().equals(ct.getViTri().getMaVT())) { cbPosition.setSelectedIndex(i); break; }
                        }
                    }
                }
            } else {
                clearFields();
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                CauThu ct = new CauThu();
                ct.setMaCT(txtId.getText().trim());
                ct.setTen(txtName.getText().trim());
                ct.setSoAo((Integer) spinSoAo.getValue());
                ct.setNgaySinh((Date) spinDob.getValue());
                try {
                    ct.setChieuCao(Double.parseDouble(txtChieuCao.getText().trim()));
                    ct.setCanNang(Double.parseDouble(txtCanNang.getText().trim()));
                } catch (NumberFormatException ex) {}
                
                if (cbTeam.getSelectedItem() != null) ct.setDoiBong(((DoiBongItem)cbTeam.getSelectedItem()).db);
                if (cbNation.getSelectedItem() != null) ct.setQuocTich(((QuocTichItem)cbNation.getSelectedItem()).qt);
                if (cbPosition.getSelectedItem() != null) ct.setViTri(((ViTriItem)cbPosition.getSelectedItem()).vt);

                if (cauThuDAO.insert(ct)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    SharedData.logActivity("Thêm cầu thủ: " + ct.getTen());
                    loadData(cauThuDAO.getAll());
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
             if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Chọn cầu thủ"); return; }
             try {
                CauThu ct = cauThuDAO.getByID(txtId.getText().trim());
                if (ct != null) {
                    ct.setTen(txtName.getText().trim());
                    ct.setSoAo((Integer) spinSoAo.getValue());
                    ct.setNgaySinh((Date) spinDob.getValue());
                    try {
                        ct.setChieuCao(Double.parseDouble(txtChieuCao.getText().trim()));
                        ct.setCanNang(Double.parseDouble(txtCanNang.getText().trim()));
                    } catch (NumberFormatException ex) {}
                    
                    if (cbTeam.getSelectedItem() != null) ct.setDoiBong(((DoiBongItem)cbTeam.getSelectedItem()).db);
                    if (cbNation.getSelectedItem() != null) ct.setQuocTich(((QuocTichItem)cbNation.getSelectedItem()).qt);
                    if (cbPosition.getSelectedItem() != null) ct.setViTri(((ViTriItem)cbPosition.getSelectedItem()).vt);

                    if (cauThuDAO.update(ct)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        SharedData.logActivity("Cập nhật cầu thủ: " + ct.getTen());
                        loadData(cauThuDAO.getAll());
                    }
                }
             } catch (Exception ex) {}
        });

        btnDelete.addActionListener(e -> {
             if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Chọn cầu thủ"); return; }
             int conf = JOptionPane.showConfirmDialog(this, "Chắc chứ?", "Xóa", JOptionPane.YES_NO_OPTION);
             if (conf == JOptionPane.YES_OPTION) {
                 CauThu ct = cauThuDAO.getByID(txtId.getText().trim());
                 if (ct != null && cauThuDAO.delete(ct)) {
                     JOptionPane.showMessageDialog(this, "Xóa thành công!");
                     SharedData.logActivity("Xóa cầu thủ: " + ct.getTen());
                     loadData(cauThuDAO.getAll());
                 }
             }
        });

        btnSearch.addActionListener(e -> {
            String kw = txtSearch.getText().trim();
            if (!kw.isEmpty()) loadData(cauThuDAO.timKiemTheoTen(kw));
        });
        
        btnSearchSoAo.addActionListener(e -> {
            try {
                int number = Integer.parseInt(txtSearchSoAo.getText().trim());
                loadData(cauThuDAO.timKiemTheoSoAo(number));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nhập số áo hợp lệ.");
            }
        });

        btnFilterTeam.addActionListener(e -> {
            if (filterCbTeam.getSelectedItem() != null) {
                String maDoi = ((DoiBongItem)filterCbTeam.getSelectedItem()).db.getMaDoi();
                loadData(cauThuDAO.locTheoDoiBong(maDoi));
            }
        });

        btnFilterNation.addActionListener(e -> {
            if (filterCbNation.getSelectedItem() != null) {
                String maQt = ((QuocTichItem)filterCbNation.getSelectedItem()).qt.getMaQT();
                loadData(cauThuDAO.locTheoQuocTich(maQt));
            }
        });

        btnClearFilter.addActionListener(e -> loadData(cauThuDAO.getAll()));

        btnTransfer.addActionListener(e -> openTransferDialog());

        loadData(cauThuDAO.getAll());
    }

    private void openTransferDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 cầu thủ từ danh sách trước!");
            return;
        }
        String maCT = tableModel.getValueAt(row, 0).toString();
        CauThu ct = cauThuDAO.getByID(maCT);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Hợp đồng (Chuyển Nhượng)", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new MigLayout("wrap 2", "[right][grow,fill]", "[]15[]"));
        dialog.setLocationRelativeTo(this);

        JComboBox<DoiBongItem> dCB = new JComboBox<>();
        for (int i=0; i<cbTeam.getItemCount(); i++) dCB.addItem(cbTeam.getItemAt(i));

        JTextField txtSalary = new JTextField();
        JSpinner spinDuration = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
        dialog.add(new JLabel("Cầu thủ:"));
        dialog.add(new JLabel(ct.getTen()), "wrap");

        dialog.add(new JLabel("Đội bóng mới:"));
        dialog.add(dCB);

        dialog.add(new JLabel("Giá trị (Mức lương mới):"));
        dialog.add(txtSalary);

        dialog.add(new JLabel("Thời hạn (Năm):"));
        dialog.add(spinDuration);

        JButton btnSave = new JButton("Ký Hợp Đồng (Bom Tấn)");
        btnSave.addActionListener(ev -> {
            if (dCB.getSelectedItem() == null) return;
            DoiBong dbMoi = ((DoiBongItem)dCB.getSelectedItem()).db;
            if (ct.getDoiBong() != null && ct.getDoiBong().getMaDoi().equals(dbMoi.getMaDoi())) {
                JOptionPane.showMessageDialog(dialog, "Cầu thủ đã thuộc đội bóng này!");
                return;
            }
            try {
                double val = Double.parseDouble(txtSalary.getText().trim());
                HopDong hd = new HopDong();
                String maHD = "HD" + System.currentTimeMillis();
                hd.setMaHD(maHD.substring(0, Math.min(maHD.length(), 255)));
                hd.setGiaTri(val);
                hd.setNgayKy(new Date());
                // calculate expiration based on duration...
                Date expiry = new Date();
                int dur = (Integer)spinDuration.getValue();
                expiry.setTime(expiry.getTime() + (long)dur * 365 * 24 * 60 * 60 * 1000L); // approx
                hd.setNgayHetHan(expiry);

                if (cauThuDAO.thucHienChuyenNhuong(maCT, dbMoi.getMaDoi(), hd)) {
                    JOptionPane.showMessageDialog(dialog, "Bom tấn đã nổ! Chuyển nhượng thành công.");
                    SharedData.logActivity(ct.getTen() + " gia nhập " + dbMoi.getTen());
                    dialog.dispose();
                    loadData(cauThuDAO.getAll());
                } else {
                    JOptionPane.showMessageDialog(dialog, "Chuyển nhượng thất bại.");
                }
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu nhập không hợp lệ.");
            }
        });
        dialog.add(btnSave, "span 2, center, gaptop 15");
        dialog.setVisible(true);
    }

    private void loadCombos() {
        cbTeam.removeAllItems(); filterCbTeam.removeAllItems();
        List<DoiBong> dbs = doiBongDAO.getAll();
        if (dbs != null) {
            for (DoiBong db : dbs) {
                DoiBongItem dt = new DoiBongItem(db);
                cbTeam.addItem(dt); filterCbTeam.addItem(dt);
            }
        }

        cbNation.removeAllItems(); filterCbNation.removeAllItems();
        List<QuocTich> qts = quocTichDAO.getAll();
        if (qts != null) {
            for (QuocTich qt : qts) {
                QuocTichItem qi = new QuocTichItem(qt);
                cbNation.addItem(qi); filterCbNation.addItem(qi);
            }
        }

        cbPosition.removeAllItems();
        List<ViTri> vts = viTriDAO.getAll();
        if (vts != null) {
            for (ViTri vt : vts) cbPosition.addItem(new ViTriItem(vt));
        }
    }

    public void addPlayerFromMarket(String name, String position, String ovr, String price) {
    }

    private void loadData(List<CauThu> ds) {
        tableModel.setRowCount(0);
        SharedData.totalPlayers = 0;
        if (ds != null) {
            SharedData.totalPlayers = ds.size();
            for (CauThu c : ds) {
                tableModel.addRow(new Object[]{
                    c.getMaCT(),
                    c.getTen(),
                    c.getSoAo(),
                    c.getDoiBong() != null ? c.getDoiBong().getTen() : "",
                    c.getQuocTich() != null ? c.getQuocTich().getTenQuocGia() : "",
                    c.getViTri() != null ? c.getViTri().getKiHieu(): ""
                });
            }
        }
        SharedData.notifyDashboardChanged();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        spinSoAo.setValue(1);
        txtChieuCao.setText("");
        txtCanNang.setText("");
        txtId.setEditable(true);
    }

    class DoiBongItem {
        DoiBong db;
        public DoiBongItem(DoiBong db) { this.db = db; }
        @Override public String toString() { return db.getTen(); }
    }
    class QuocTichItem {
        QuocTich qt;
        public QuocTichItem(QuocTich qt) { this.qt = qt; }
        @Override public String toString() { return qt.getTenQuocGia(); }
    }
    class ViTriItem {
        ViTri vt;
        public ViTriItem(ViTri vt) { this.vt = vt; }
        @Override public String toString() { return vt.getKiHieu() + " - " + vt.getTenViTri(); }
    }
}
