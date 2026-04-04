package com.football.view;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.net.URL;

public class ImageHelper {
    public static ImageIcon createIcon(String fileName) {
        // Dùng ImageHelper.class để lấy resources từ mọi nơi
        java.net.URL imgURL = ImageHelper.class.getResource("/images/" + fileName);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        return null;
    }
}
