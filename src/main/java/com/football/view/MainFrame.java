package com.football.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    public MainFrame() {
        setTitle("Quản Lý Đội Bóng - Football Manager");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80)); // Đen xám xanh đậm

        JLabel logoLabel = new JLabel("FOOTBALL MGR", SwingConstants.CENTER);
        logoLabel.setForeground(new Color(192, 192, 192));
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 50, 0));
        sidebar.add(logoLabel);

        // Nút bấm ở sidebar
        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnTeam = createSidebarButton("Đội Bóng");
        JButton btnPlayer = createSidebarButton("Cầu Thủ");
        JButton btnMarket = createSidebarButton("Chợ Chuyển Nhượng");
        JButton btnTactics = createSidebarButton("Sơ Đồ Chiến Thuật");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(btnTeam);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(btnPlayer);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(btnMarket);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(btnTactics);

        add(sidebar, BorderLayout.WEST);

        // --- Content Area (CardLayout) ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Thêm các panel con vào cardLayout
        mainContentPanel.add(new DashboardPanel(), "Dashboard");
        mainContentPanel.add(new TeamPanel(), "Team");
        mainContentPanel.add(new PlayerPanel(), "Player");
        mainContentPanel.add(new TransferMarketPanel(), "Market");
        mainContentPanel.add(new TacticsPanel(), "Tactics");

        add(mainContentPanel, BorderLayout.CENTER);

        // --- Action Listeners for sidebar buttons ---
        btnDashboard.addActionListener(e -> cardLayout.show(mainContentPanel, "Dashboard"));
        btnTeam.addActionListener(e -> cardLayout.show(mainContentPanel, "Team"));
        btnPlayer.addActionListener(e -> cardLayout.show(mainContentPanel, "Player"));
        btnMarket.addActionListener(e -> cardLayout.show(mainContentPanel, "Market"));
        btnTactics.addActionListener(e -> cardLayout.show(mainContentPanel, "Tactics"));
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(250, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(26, 188, 156));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        return button;
    }
}
