package com.football.view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.util.Date;
import java.util.List;

import com.football.dao.TranDauDAO;
import com.football.dao.DoiBongDAO;
import com.football.entity.TranDau;
import com.football.entity.DoiBong;

public class MatchPanel extends JPanel {
    private DoiBongDAO doiBongDAO = new DoiBongDAO();
    private TranDauDAO tranDauDAO = new TranDauDAO();
    private JComboBox<DoiBongItem> cbHomeTeam;
    private JComboBox<DoiBongItem> cbAwayTeam;

    public MatchPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        // Header Title
        JLabel lblTitle = new JLabel("QUẢN LÝ TRẬN ĐẤU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center Panel for Form
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new MigLayout("wrap 2", "[right][grow,fill]", "[]20[]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Ghi nhận kết quả trận đấu"));
        formPanel.setBackground(Color.WHITE);

        cbHomeTeam = new JComboBox<>();
        cbAwayTeam = new JComboBox<>();
        
        loadTeams();

        JTextField txtHomeGoals = new JTextField(5);
        txtHomeGoals.setHorizontalAlignment(JTextField.CENTER);
        txtHomeGoals.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JTextField txtAwayGoals = new JTextField(5);
        txtAwayGoals.setHorizontalAlignment(JTextField.CENTER);
        txtAwayGoals.setFont(new Font("Segoe UI", Font.BOLD, 16));

        formPanel.add(new JLabel("Đội chủ nhà:"));
        formPanel.add(cbHomeTeam);

        formPanel.add(new JLabel("Đội khách:"));
        formPanel.add(cbAwayTeam);

        formPanel.add(new JLabel("Bàn thắng đội nhà:"));
        formPanel.add(txtHomeGoals);

        formPanel.add(new JLabel("Bàn thắng đội khách:"));
        formPanel.add(txtAwayGoals);

        JButton btnRecord = new JButton("Ghi nhận kết quả");
        btnRecord.setBackground(new Color(46, 204, 113));
        btnRecord.setForeground(Color.WHITE);
        btnRecord.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRecord.setFocusPainted(false);

        btnRecord.addActionListener(e -> {
            DoiBongItem homeTeamItem = (DoiBongItem) cbHomeTeam.getSelectedItem();
            DoiBongItem awayTeamItem = (DoiBongItem) cbAwayTeam.getSelectedItem();

            if (homeTeamItem == null || awayTeamItem == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đội bóng.");
                return;
            }

            if (homeTeamItem.doiBong.getMaDoi().equals(awayTeamItem.doiBong.getMaDoi())) {
                JOptionPane.showMessageDialog(this, "Đội chủ nhà và đội khách không được trùng nhau.");
                return;
            }

            try {
                int homeG = Integer.parseInt(txtHomeGoals.getText().trim());
                int awayG = Integer.parseInt(txtAwayGoals.getText().trim());

                if (homeG < 0 || awayG < 0) {
                    JOptionPane.showMessageDialog(this, "Số bàn thắng không hợp lệ.");
                    return;
                }

                TranDau tran = new TranDau();
                String maTran = "M" + System.currentTimeMillis();
                if (maTran.length() > 50) maTran = maTran.substring(0, 50); // Just in case
                tran.setMaTran(maTran);
                tran.setNgayGio(new Date());
                tran.setTiSo(homeG + "-" + awayG);
                tran.setBanThangDoiNha(homeG);
                tran.setBanThangDoiKhach(awayG);
                tran.setDoiChuNha(homeTeamItem.doiBong);
                tran.setDoiKhach(awayTeamItem.doiBong);

                boolean success = tranDauDAO.capNhatKetQua(tran);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Ghi nhận kết quả thành công!");
                    SharedData.logActivity("Cập nhật trận đấu " + homeTeamItem.doiBong.getTen() + " " + homeG + " - " + awayG + " " + awayTeamItem.doiBong.getTen());
                    txtHomeGoals.setText("");
                    txtAwayGoals.setText("");
                    SharedData.notifyDashboardChanged();
                } else {
                    JOptionPane.showMessageDialog(this, "Ghi nhận kết quả thất bại.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số bàn thắng hợp lệ.");
            }
        });

        formPanel.add(btnRecord, "span 2, center, gaptop 15");

        centerPanel.add(formPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadTeams() {
        cbHomeTeam.removeAllItems();
        cbAwayTeam.removeAllItems();
        List<DoiBong> teams = doiBongDAO.getAll();
        if (teams != null) {
            for (DoiBong db : teams) {
                DoiBongItem item = new DoiBongItem(db);
                cbHomeTeam.addItem(item);
                cbAwayTeam.addItem(item);
            }
        }
    }

    class DoiBongItem {
        DoiBong doiBong;
        public DoiBongItem(DoiBong db) { this.doiBong = db; }
        @Override public String toString() { return doiBong.getTen() != null ? doiBong.getTen() : doiBong.getMaDoi(); }
    }
}
