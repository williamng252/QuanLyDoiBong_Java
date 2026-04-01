package com.football.gui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel lblTotalTeams;
    private JLabel lblTotalPlayers;
    private JLabel lblBudget;

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Title
        JLabel lblTitle = new JLabel("TỔNG QUAN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTitle, BorderLayout.NORTH);

        // Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 20));

        JPanel cardTeams = createCardPanel("Tổng Đội Bóng", new Color(41, 128, 185));
        lblTotalTeams = createCardValueLabel(String.valueOf(SharedData.totalTeams));
        cardTeams.add(lblTotalTeams, BorderLayout.CENTER);
        cardsPanel.add(cardTeams);

        JPanel cardPlayers = createCardPanel("Tổng Cầu Thủ", new Color(39, 174, 96));
        lblTotalPlayers = createCardValueLabel(String.valueOf(SharedData.totalPlayers));
        cardPlayers.add(lblTotalPlayers, BorderLayout.CENTER);
        cardsPanel.add(cardPlayers);

        JPanel cardBudget = createCardPanel("Ngân Sách Tối Đa", new Color(142, 68, 173));
        lblBudget = createCardValueLabel(String.format("$%,.0f", SharedData.userBudget));
        cardBudget.add(lblBudget, BorderLayout.CENTER);
        cardsPanel.add(cardBudget);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(cardsPanel, BorderLayout.NORTH);

        // A placeholder for maybe a chart or recent activity
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Hoạt động gần đây"));
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bottomPanel.add(new JScrollPane(area), BorderLayout.CENTER);

        centerContainer.add(bottomPanel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        SharedData.dashboardListeners.add(() -> {
            lblTotalTeams.setText(String.valueOf(SharedData.totalTeams));
            lblTotalPlayers.setText(String.valueOf(SharedData.totalPlayers));
            lblBudget.setText(String.format("$%,.0f", SharedData.userBudget));

            StringBuilder sb = new StringBuilder();
            for (String act : SharedData.activities) {
                sb.append(act).append("\n");
            }
            area.setText(sb.toString());
        });

        // Tự update lần đầu khi mở app
        SharedData.notifyDashboardChanged();
    }

    private JPanel createCardPanel(String title, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.NORTH);

        return panel;
    }

    private JLabel createCardValueLabel(String value) {
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        return lblValue;
    }
}
