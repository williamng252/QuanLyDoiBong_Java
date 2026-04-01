package com.football.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class TeamPanel extends JPanel {
    public TeamPanel() {
        setLayout(new BorderLayout());

        // Header Title
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỘI BÓNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Center split: Table on left, Form on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6); // 60% for table

        // --- Table Panel ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Tên Đội", "Nhà Tài Trợ", "Ngân Sách", "Sân Nhà" };
        Object[][] data = {
                { "1", "Hà Nội FC", "T&T Group", "$1,000,000", "Hàng Đẫy" },
                { "2", "HAGL", "Red Bull", "$800,000", "Pleiku" }
        };
        for (Object[] row : data) {
            SharedData.teams.add((String) row[1]);
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new net.miginfocom.swing.MigLayout("wrap 2", "[right][grow,fill]", "[]15[]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        JTextField txtId = new JTextField(15);
        JTextField txtName = new JTextField(15);
        JTextField txtSponsor = new JTextField(15);
        JTextField txtBudget = new JTextField(15);
        JTextField txtStadium = new JTextField(15);

        formPanel.add(new JLabel("Mã đội:"));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên đội bóng:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Nhà tài trợ:"));
        formPanel.add(txtSponsor);

        formPanel.add(new JLabel("Ngân sách chuyển nhượng:"));
        formPanel.add(txtBudget);

        formPanel.add(new JLabel("Tên sân nhà:"));
        formPanel.add(txtStadium);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Mới");
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtSponsor.setText(tableModel.getValueAt(row, 2).toString());
                txtBudget.setText(tableModel.getValueAt(row, 3).toString());
                txtStadium.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            if (!id.isEmpty() && !name.isEmpty()) {
                tableModel.addRow(new Object[] { id, name, txtSponsor.getText().trim(), txtBudget.getText().trim(),
                        txtStadium.getText().trim() });
                syncToSharedData(tableModel);
                SharedData.logActivity("Thêm đội bóng: " + name);
                clearFields(txtId, txtName, txtSponsor, txtBudget, txtStadium);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất Mã đội & Tên đội!");
            }
        });

        JButton btnUpdate = new JButton("Cập Nhật");
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String oldName = tableModel.getValueAt(row, 1).toString();
                tableModel.setValueAt(txtId.getText().trim(), row, 0);
                tableModel.setValueAt(txtName.getText().trim(), row, 1);
                tableModel.setValueAt(txtSponsor.getText().trim(), row, 2);
                tableModel.setValueAt(txtBudget.getText().trim(), row, 3);
                tableModel.setValueAt(txtStadium.getText().trim(), row, 4);
                syncToSharedData(tableModel);
                SharedData.logActivity("Cập nhật đội bóng: " + oldName);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để cập nhật!");
            }
        });

        JButton btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String name = tableModel.getValueAt(row, 1).toString();
                tableModel.removeRow(row);
                syncToSharedData(tableModel);
                SharedData.logActivity("Xóa đội bóng: " + name);
                clearFields(txtId, txtName, txtSponsor, txtBudget, txtStadium);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!");
            }
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        formPanel.add(btnPanel, "span 2, right");

        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(formPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private void syncToSharedData(DefaultTableModel model) {
        SharedData.teams.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            SharedData.teams.add(model.getValueAt(i, 1).toString());
        }
        SharedData.totalTeams = model.getRowCount();
        SharedData.notifyTeamsChanged();
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields)
            f.setText("");
    }
}
