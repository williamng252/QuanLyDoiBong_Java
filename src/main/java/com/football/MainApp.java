package com.football;

import com.football.dao.*;
import com.football.entity.CauThu;
import com.football.entity.DoiBong;
import com.football.entity.HopDong;
import com.football.entity.TranDau;

import java.util.Date;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {

        // Khởi động Giao diện Swing (GUI)
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Tắt SystemLookAndFeel để các Nút bấm (Button) có thể hiển thị MÀU SẮC gốc
                java.awt.Color grayColor = new java.awt.Color(230, 230, 230);
                javax.swing.UIManager.put("control", grayColor);
                javax.swing.UIManager.put("Panel.background", grayColor);
                javax.swing.UIManager.put("ScrollPane.background", grayColor);
                javax.swing.UIManager.put("Viewport.background", grayColor);
                javax.swing.UIManager.put("SplitPane.background", grayColor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            com.football.view.MainFrame mainFrame = new com.football.view.MainFrame();
            mainFrame.setVisible(true);
            mainFrame.getContentPane().setBackground(new java.awt.Color(230, 230, 230));
        });
    }
}