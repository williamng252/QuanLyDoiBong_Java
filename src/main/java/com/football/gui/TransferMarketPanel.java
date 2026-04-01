package com.football.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class TransferMarketPanel extends JPanel {
    public TransferMarketPanel() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CHỢ CHUYỂN NHƯỢNG CẦU THỦ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel budgetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        JLabel lblBudgetTitle = new JLabel("Ngân sách còn lại:");
        lblBudgetTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblBudget = new JLabel(String.format("$%,.0f", SharedData.userBudget));
        lblBudget.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBudget.setForeground(new Color(39, 174, 96));
        
        budgetPanel.add(lblBudgetTitle);
        budgetPanel.add(lblBudget);
        headerPanel.add(budgetPanel, BorderLayout.EAST);
        
        SharedData.dashboardListeners.add(() -> {
            lblBudget.setText(String.format("$%,.0f", SharedData.userBudget));
        });

        add(headerPanel, BorderLayout.NORTH);

        // --- Filter Panel ---
        JPanel filterPanel = new JPanel(new MigLayout("insets 10 20 10 20, gapx 15", "[] [] [] [] [] [grow, right]"));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Mũi nhọn Tìm kiếm & Thanh lọc"));
        
        filterPanel.add(new JLabel("Tên cầu thủ:"));
        filterPanel.add(new JTextField(12));
        
        filterPanel.add(new JLabel("Vị trí:"));
        filterPanel.add(new JComboBox<>(new String[]{"Tất cả", "Tiền đạo (FW)", "Tiền vệ (MF)", "Hậu vệ (DF)", "Thủ môn (GK)"}));
        
        filterPanel.add(new JLabel("Chỉ số (OVR) từ:"));
        filterPanel.add(new JSpinner(new SpinnerNumberModel(50, 0, 99, 1)));
        filterPanel.add(new JLabel("đến:"));
        filterPanel.add(new JSpinner(new SpinnerNumberModel(99, 0, 99, 1)));
        
        filterPanel.add(new JLabel("Giá tối đa ($):"));
        filterPanel.add(new JTextField(8));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(46, 204, 113));
        btnSearch.setForeground(Color.WHITE);
        filterPanel.add(btnSearch, "wrap");
        
        // --- Table Panel ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] columns = {"ID", "Tên Cầu Thủ", "Vị Trí", "Chỉ số (OVR)", "Giá trị", "Thể lực", "Hành động"};
        Object[][] data = {
            {"4", "Lionel Messi", "RW", "90", "$40,000,000", "80%"},
            {"5", "Kylian Mbappé", "ST", "91", "$180,000,000", "95%"},
            {"6", "Erling Haaland", "ST", "90", "$150,000,000", "90%"},
            {"7", "Jude Bellingham", "CM", "88", "$120,000,000", "99%"}
        };
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // --- Bottom action (Buy button) ---
        JPanel bottomActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBuy = new JButton("Mua Cầu Thủ Đã Chọn");
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuy.setBackground(new Color(52, 152, 219));
        btnBuy.setForeground(Color.WHITE);
        bottomActionPanel.add(btnBuy);

        btnBuy.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String name = table.getValueAt(row, 1).toString();
                String priceStr = table.getValueAt(row, 4).toString().replaceAll("[^0-9]", "");
                double price = Double.parseDouble(priceStr);
                
                if (price > SharedData.userBudget) {
                    JOptionPane.showMessageDialog(this, "Không đủ tiền!\nNgân sách của bạn là: $" + String.format("%,.0f", SharedData.userBudget), "Từ chối giao dịch", JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán $" + String.format("%,.0f", price) + " để mua " + name + "?\nNgân sách dư: $" + String.format("%,.0f", SharedData.userBudget), "Xác nhận Thanh toán", JOptionPane.OK_CANCEL_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        SharedData.userBudget -= price;
                        String pos = table.getValueAt(row, 2).toString();
                        String ovr = table.getValueAt(row, 3).toString();
                        String formattedPriceStr = "$" + String.format("%,.0f", price);
                        
                        if (PlayerPanel.instance != null) {
                            PlayerPanel.instance.addPlayerFromMarket(name, pos, ovr, formattedPriceStr);
                        }
                        
                        SharedData.logActivity("Thanh toán thành công " + formattedPriceStr + " để chiêu mộ " + name);
                        SharedData.notifyDashboardChanged();

                        ((DefaultTableModel) table.getModel()).removeRow(row);
                        JOptionPane.showMessageDialog(this, "Giao dịch thành công! " + name + " đã gia nhập đội bóng của bạn.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 cầu thủ để mua!");
            }
        });
        
        tableContainer.add(bottomActionPanel, BorderLayout.SOUTH);

        JPanel mainCenter = new JPanel(new BorderLayout());
        mainCenter.add(filterPanel, BorderLayout.NORTH);
        mainCenter.add(tableContainer, BorderLayout.CENTER);

        add(mainCenter, BorderLayout.CENTER);
    }
}
