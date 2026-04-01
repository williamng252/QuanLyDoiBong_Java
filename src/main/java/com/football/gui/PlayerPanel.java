package com.football.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class PlayerPanel extends JPanel {
    public static PlayerPanel instance;
    private DefaultTableModel tableModel;

    public PlayerPanel() {
        instance = this;
        setLayout(new BorderLayout());

        // Header Title
        JLabel lblTitle = new JLabel("QUẢN LÝ CẦU THỦ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);

        // --- Table Panel ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Tên Cầu Thủ", "Đội Bóng", "Vị Trí", "Chỉ số (OVR)", "Giá trị" };
        Object[][] data = {
                { "1", "Nguyễn Văn Quyết", "Hà Nội FC", "ST", "75", "$500,000" },
                { "2", "Nguyễn Tuấn Anh", "HAGL", "CM", "72", "$400,000" },
                { "3", "Đoàn Văn Hậu", "CAHN", "LB", "74", "$600,000" }
        };
        SharedData.totalPlayers = data.length;

        tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new MigLayout("wrap 2", "[right][grow,fill]", "[]15[]"));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        JTextField txtId = new JTextField(15);
        JTextField txtName = new JTextField(15);
        JComboBox<String> cbTeam = new JComboBox<>();
        refreshTeamCombo(cbTeam);
        SharedData.teamListeners.add(() -> refreshTeamCombo(cbTeam));

        JComboBox<String> cbPosition = new JComboBox<>(
                new String[] { "GK", "CB", "LB", "RB", "CDM", "CM", "CAM", "LW", "RW", "ST" });
        JSpinner spinOvr = new JSpinner(new SpinnerNumberModel(70, 40, 99, 1));
        JTextField txtValue = new JTextField(15);

        formPanel.add(new JLabel("Mã cầu thủ:"));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên cầu thủ:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Đội bóng:"));
        formPanel.add(cbTeam);

        formPanel.add(new JLabel("Vị trí:"));
        formPanel.add(cbPosition);

        formPanel.add(new JLabel("Chỉ số (OVR):"));
        formPanel.add(spinOvr);

        formPanel.add(new JLabel("Giá trị thị trường:"));
        formPanel.add(txtValue);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Mới");
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                cbTeam.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                cbPosition.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                spinOvr.setValue(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
                txtValue.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String team = cbTeam.getSelectedItem() != null ? cbTeam.getSelectedItem().toString() : "";
            String pos = (String) cbPosition.getSelectedItem();
            String ovr = spinOvr.getValue().toString();
            String value = txtValue.getText().trim();

            if (!id.isEmpty() && !name.isEmpty()) {
                tableModel.addRow(new Object[] { id, name, team, pos, ovr, value });
                updatePlayerCount();
                SharedData.logActivity("Thêm cầu thủ: " + name);
                clearFields(txtId, txtName, txtValue);
                if (cbTeam.getItemCount() > 0)
                    cbTeam.setSelectedIndex(0);
                cbPosition.setSelectedIndex(0);
                spinOvr.setValue(70);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất Mã cầu thủ & Tên cầu thủ!", "Lỗi nhập liệu",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnUpdate = new JButton("Cập Nhật");
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String oldName = tableModel.getValueAt(row, 1).toString();
                tableModel.setValueAt(txtId.getText().trim(), row, 0);
                tableModel.setValueAt(txtName.getText().trim(), row, 1);
                tableModel.setValueAt(cbTeam.getSelectedItem(), row, 2);
                tableModel.setValueAt(cbPosition.getSelectedItem(), row, 3);
                tableModel.setValueAt(spinOvr.getValue().toString(), row, 4);
                tableModel.setValueAt(txtValue.getText().trim(), row, 5);
                SharedData.logActivity("Cập nhật cầu thủ: " + oldName);
                JOptionPane.showMessageDialog(this, "Cập nhật cầu thủ thành công!");
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
                updatePlayerCount();
                SharedData.logActivity("Xóa cầu thủ: " + name);
                clearFields(txtId, txtName, txtValue);
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

    private void refreshTeamCombo(JComboBox<String> cbTeam) {
        Object selected = cbTeam.getSelectedItem();
        cbTeam.removeAllItems();
        for (String t : SharedData.teams) {
            cbTeam.addItem(t);
        }
        cbTeam.addItem("Tự do");
        if (selected != null) {
            cbTeam.setSelectedItem(selected);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields)
            f.setText("");
    }

    public void addPlayerFromMarket(String name, String position, String ovr, String value) {
        String newId = String.valueOf(tableModel.getRowCount() + 1);
        tableModel.addRow(new Object[] { newId, name, "Tự do", position, ovr, value });
        updatePlayerCount();
        SharedData.logActivity("Chiêu mộ cầu thủ: " + name);
    }

    private void updatePlayerCount() {
        SharedData.totalPlayers = tableModel.getRowCount();
        SharedData.notifyDashboardChanged();
    }
}
