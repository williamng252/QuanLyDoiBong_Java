package com.football.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class TacticsPanel extends JPanel {
    private PitchPanel pitchPanel;

    public TacticsPanel() {
        setLayout(new BorderLayout());

        // Header & Options Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("SƠ ĐỒ CHIẾN THUẬT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        comboPanel.add(new JLabel("Lựa chọn Sơ Đồ:"));
        JComboBox<String> cbFormation = new JComboBox<>(new String[]{"4-2-3-1", "4-4-2", "4-3-3", "3-5-2"});
        cbFormation.setSelectedIndex(0);
        comboPanel.add(cbFormation);
        JButton btnReset = new JButton("Khôi phục mặc định");
        comboPanel.add(btnReset);
        topPanel.add(comboPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        cbFormation.addActionListener(e -> {
            String formation = (String) cbFormation.getSelectedItem();
            pitchPanel.setFormation(formation);
        });

        btnReset.addActionListener(e -> {
            String formation = (String) cbFormation.getSelectedItem();
            pitchPanel.setFormation(formation);
        });
        
        pitchPanel = new PitchPanel();
        add(pitchPanel, BorderLayout.CENTER);
    }

    // Custom JPanel for Grass Pitch
    class PitchPanel extends JPanel {
        class PlayerNode {
            double rx, ry; // Tọa độ tương đối 0.0 -> 1.0 (Giúp giữ đúng vị trí khi resize theo ảnh)
            String pos;
            PlayerNode(double rx, double ry, String pos) {
                this.rx = rx;
                this.ry = ry;
                this.pos = pos;
            }
        }

        private ArrayList<PlayerNode> players = new ArrayList<>();
        private int draggedPlayerIndex = -1;

        public PitchPanel() {
            setFormation("4-2-3-1");

            // Mouse listeners for drag & drop
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int w = getWidth(), h = getHeight();
                    for (int i = 0; i < players.size(); i++) {
                        PlayerNode node = players.get(i);
                        int px = (int)(node.rx * w) - 15;
                        int py = (int)(node.ry * h) - 15;
                        if (e.getX() >= px && e.getX() <= px + 30 && e.getY() >= py && e.getY() <= py + 30) {
                            draggedPlayerIndex = i;
                            break;
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    draggedPlayerIndex = -1;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (draggedPlayerIndex != -1) {
                        PlayerNode node = players.get(draggedPlayerIndex);
                        node.rx = (double) e.getX() / getWidth();
                        node.ry = (double) e.getY() / getHeight();
                        repaint();
                    }
                }
            });
        }

        public void setFormation(String formation) {
            players.clear();

            // Mọi tọa độ RX, RY sẽ là phần trăm (0.0 -> 1.0) tính theo chiều dọc của sân bóng!
            players.add(new PlayerNode(0.5, 0.90, "GK")); // GK nằm sát khung thành dưới cùng

            if ("4-2-3-1".equals(formation)) {
                players.add(new PlayerNode(0.15, 0.65, "LB")); 
                players.add(new PlayerNode(0.38, 0.68, "LCB")); 
                players.add(new PlayerNode(0.62, 0.68, "RCB")); 
                players.add(new PlayerNode(0.85, 0.65, "RB")); 
                players.add(new PlayerNode(0.38, 0.52, "LDM")); 
                players.add(new PlayerNode(0.62, 0.52, "RDM")); 
                players.add(new PlayerNode(0.25, 0.35, "LAM")); 
                players.add(new PlayerNode(0.50, 0.40, "CAM")); 
                players.add(new PlayerNode(0.75, 0.35, "RAM")); 
                players.add(new PlayerNode(0.50, 0.20, "ST")); 
            } else if ("4-4-2".equals(formation)) {
                players.add(new PlayerNode(0.15, 0.65, "LB")); 
                players.add(new PlayerNode(0.38, 0.68, "LCB")); 
                players.add(new PlayerNode(0.62, 0.68, "RCB")); 
                players.add(new PlayerNode(0.85, 0.65, "RB")); 
                players.add(new PlayerNode(0.20, 0.45, "LM")); 
                players.add(new PlayerNode(0.40, 0.45, "LCM")); 
                players.add(new PlayerNode(0.60, 0.45, "RCM")); 
                players.add(new PlayerNode(0.80, 0.45, "RM")); 
                players.add(new PlayerNode(0.40, 0.25, "LS")); 
                players.add(new PlayerNode(0.60, 0.25, "RS")); 
            } else if ("4-3-3".equals(formation)) {
                players.add(new PlayerNode(0.15, 0.65, "LB")); 
                players.add(new PlayerNode(0.38, 0.68, "LCB")); 
                players.add(new PlayerNode(0.62, 0.68, "RCB")); 
                players.add(new PlayerNode(0.85, 0.65, "RB")); 
                players.add(new PlayerNode(0.40, 0.48, "LCM")); 
                players.add(new PlayerNode(0.50, 0.42, "CAM")); 
                players.add(new PlayerNode(0.60, 0.48, "RCM")); 
                players.add(new PlayerNode(0.25, 0.30, "LW")); 
                players.add(new PlayerNode(0.50, 0.22, "ST")); 
                players.add(new PlayerNode(0.75, 0.30, "RW")); 
            } else if ("3-5-2".equals(formation)) { // Thể hiện thành 3-5-1-1 như trong ảnh mẫu
                players.add(new PlayerNode(0.30, 0.68, "LCB")); 
                players.add(new PlayerNode(0.50, 0.68, "CB")); 
                players.add(new PlayerNode(0.70, 0.68, "RCB")); 
                players.add(new PlayerNode(0.18, 0.45, "LM")); 
                players.add(new PlayerNode(0.40, 0.53, "LDM")); 
                players.add(new PlayerNode(0.50, 0.46, "CM")); 
                players.add(new PlayerNode(0.60, 0.53, "RDM")); 
                players.add(new PlayerNode(0.82, 0.45, "RM")); 
                players.add(new PlayerNode(0.50, 0.35, "CF")); 
                players.add(new PlayerNode(0.50, 0.22, "ST")); 
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw Grass (Dark Green)
            g2.setColor(new Color(39, 174, 96));
            g2.fillRect(0, 0, width, height);
            
            // Draw Pitch Lines (White)
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            
            // Border
            g2.drawRect(20, 20, width - 40, height - 40);
            
            // Halfway line (Đường giữa sân dọc)
            g2.drawLine(20, height / 2, width - 20, height / 2);
            
            // Center circle
            g2.drawOval(width / 2 - 50, height / 2 - 50, 100, 100);
            g2.fillOval(width / 2 - 5, height / 2 - 5, 10, 10);
            
            // Penalty areas (Vòng cấm địa trên và dưới)
            g2.drawRect(width / 2 - 120, 20, 240, 100);
            g2.drawRect(width / 2 - 120, height - 120, 240, 100);

            // Goal areas (Vòng cầu môn 5m50 trên và dưới)
            g2.drawRect(width / 2 - 50, 20, 100, 40);
            g2.drawRect(width / 2 - 50, height - 60, 100, 40);

            // Draw Players (Nodes)
            for (int i = 0; i < players.size(); i++) {
                PlayerNode node = players.get(i);
                int px = (int)(node.rx * width) - 15;
                int py = (int)(node.ry * height) - 15;
                
                // Yellow Border ring
                g2.setColor(new Color(241, 196, 15)); 
                g2.fillOval(px - 3, py - 3, 36, 36);
                
                // Red Jersey
                g2.setColor(new Color(231, 76, 60)); 
                g2.fillOval(px, py, 30, 30);
                
                // Draw Name inside the circle
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                String posStr = node.pos;
                int textX = px + 15 - (fm.stringWidth(posStr) / 2);
                int textY = py + 15 + (fm.getAscent() / 2) - 2; // Approximate visual center
                
                g2.drawString(posStr, textX, textY);
            }
        }
    }
}
