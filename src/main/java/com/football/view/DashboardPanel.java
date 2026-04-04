package com.football.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.football.dao.DoiBongDAO;
import com.football.dao.ChiTietTranDauDAO;
import com.football.dao.CauThuDAO;
import com.football.dao.HLVDAO;
import com.football.dao.SVDDAO;
import com.football.entity.DoiBong;
import com.football.entity.CauThu;

public class DashboardPanel extends JPanel {
    private DefaultTableModel bxhModel;
    private DefaultTableModel vuaPhaLuoiModel;
    
    private DoiBongDAO doiBongDAO = new DoiBongDAO();
    private ChiTietTranDauDAO chiTietTranDauDAO = new ChiTietTranDauDAO();
    private CauThuDAO cauThuDAO = new CauThuDAO();
    private HLVDAO hlvDAO = new HLVDAO();
    private SVDDAO svdDAO = new SVDDAO();

    private JLabel lblTotalTeams;
    private JLabel lblTotalPlayers;
    private JLabel lblTotalHLV;
    private JLabel lblTotalSVD;

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(204, 255, 229)); // Màu xanh mint nhạt

        // Header Structure
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("BẢNG XẾP HẠNG & THỐNG KÊ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Làm mới DB");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(41, 128, 185));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        // Thống kê tổng quan (4 ô Overview)
        JPanel overviewPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        overviewPanel.setOpaque(false);
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        lblTotalTeams = new JLabel("0", SwingConstants.CENTER);
        lblTotalPlayers = new JLabel("0", SwingConstants.CENTER);
        lblTotalHLV = new JLabel("0", SwingConstants.CENTER);
        lblTotalSVD = new JLabel("0", SwingConstants.CENTER);

        overviewPanel.add(createCard("TỔNG ĐỘI BÓNG", lblTotalTeams, new Color(52, 152, 219)));
        overviewPanel.add(createCard("TỔNG CẦU THỦ", lblTotalPlayers, new Color(46, 204, 113)));
        overviewPanel.add(createCard("HUẤN LUYỆN VIÊN", lblTotalHLV, new Color(155, 89, 182)));
        overviewPanel.add(createCard("SÂN VẬN ĐỘNG", lblTotalSVD, new Color(241, 196, 15)));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(overviewPanel, BorderLayout.CENTER);

        // Center split for the 2 tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // 50-50
        splitPane.setOpaque(false);

        // --- Table 1: Bảng Xếp Hạng ---
        JPanel pnlBXH = new JPanel(new BorderLayout());
        pnlBXH.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Bảng Xếp Hạng"));
        pnlBXH.setBackground(Color.WHITE);
        
        String[] colsBXH = {"Xếp Hạng", "Đội", "Điểm", "Hiệu Số"};
        bxhModel = new DefaultTableModel(colsBXH, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tblBXH = new JTable(bxhModel);
        tblBXH.setRowHeight(30);
        tblBXH.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlBXH.add(new JScrollPane(tblBXH), BorderLayout.CENTER);

        // --- Table 2: Vua Phá Lưới ---
        JPanel pnlVPL = new JPanel(new BorderLayout());
        pnlVPL.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Vua Phá Lưới (Top 10)"));
        pnlVPL.setBackground(Color.WHITE);
        
        String[] colsVPL = {"Xếp Hạng", "Cầu Thủ", "Đội", "Bàn Thắng"};
        vuaPhaLuoiModel = new DefaultTableModel(colsVPL, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tblVPL = new JTable(vuaPhaLuoiModel);
        tblVPL.setRowHeight(30);
        tblVPL.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlVPL.add(new JScrollPane(tblVPL), BorderLayout.CENTER);

        splitPane.setLeftComponent(pnlBXH);
        splitPane.setRightComponent(pnlVPL);

        add(topContainer, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            loadData();
            JOptionPane.showMessageDialog(this, "Đã cập nhật dữ liệu mới nhất từ CSDL!");
        });

        SharedData.dashboardListeners.add(this::loadData);

        // Initial Load
        loadData();
    }

    private JPanel createCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.GRAY);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void loadData() {
        // Cập nhật các ô Thống kê (Overview)
        List<DoiBong> dbs = doiBongDAO.getAll();
        lblTotalTeams.setText(String.valueOf(dbs != null ? dbs.size() : 0));
        
        List<?> cts = cauThuDAO.getAll();
        lblTotalPlayers.setText(String.valueOf(cts != null ? cts.size() : 0));
        
        List<?> hlvs = hlvDAO.getAll();
        lblTotalHLV.setText(String.valueOf(hlvs != null ? hlvs.size() : 0));
        
        List<?> svds = svdDAO.getAll();
        lblTotalSVD.setText(String.valueOf(svds != null ? svds.size() : 0));

        // Load Bảng xếp hạng
        bxhModel.setRowCount(0);
        if (dbs != null && !dbs.isEmpty()) {
            int rank = 1;
            for (DoiBong db : dbs) {
                bxhModel.addRow(new Object[]{
                    rank++,
                    db.getTen() != null ? db.getTen() : db.getMaDoi(),
                    db.getDiem(),
                    db.getHieuSo()
                });
            }
        } else {
            // Dữ liệu mẫu (theo yêu cầu user)
            bxhModel.addRow(new Object[]{1, "Manchester City", 85, "+50"});
            bxhModel.addRow(new Object[]{2, "Arsenal", 82, "+45"});
            bxhModel.addRow(new Object[]{3, "Liverpool FC", 79, "+40"});
            bxhModel.addRow(new Object[]{4, "Real Madrid", 75, "+30"});
        }

        // Load Vua Pha Luoi
        vuaPhaLuoiModel.setRowCount(0);
        List<Object[]> tops = chiTietTranDauDAO.getTopGhiBan(10);
        if (tops != null && !tops.isEmpty()) {
            int rank = 1;
            for (Object[] rowObj : tops) {
                CauThu ct = (CauThu) rowObj[0];
                Long goals = (Long) rowObj[1];
                vuaPhaLuoiModel.addRow(new Object[]{
                    rank++,
                    ct != null ? ct.getTen() : "Unknown",
                    (ct != null && ct.getDoiBong() != null) ? ct.getDoiBong().getTen() : "Tự do",
                    goals != null ? goals : 0
                });
            }
        } else {
             // Dữ liệu mẫu (theo yêu cầu user)
             vuaPhaLuoiModel.addRow(new Object[]{1, "Erling Haaland", "MC", 27});
             vuaPhaLuoiModel.addRow(new Object[]{2, "Mohamed Salah", "LIV", 21});
             vuaPhaLuoiModel.addRow(new Object[]{3, "Vinicius Junior", "RM", 18});
             vuaPhaLuoiModel.addRow(new Object[]{4, "Bukayo Saka", "ARS", 16});
        }
    }
}
