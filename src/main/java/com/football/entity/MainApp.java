package com.football.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Khởi tạo Hibernate: Lúc này nó sẽ đọc hibernate.cfg.xml và tạo ra toàn bộ 11 bảng
            SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            
            // Mở session để test kết nối
            Session session = factory.openSession();
            
            System.out.println("---------------------------------------------------------");
            System.out.println(">>> KẾT NỐI DATABASE THÀNH CÔNG!");
            System.out.println(">>> 11 BẢNG ĐÃ ĐƯỢC TỰ ĐỘNG TẠO TRONG WORKBENCH.");
            System.out.println("---------------------------------------------------------");
            
            session.close();
            factory.close();
        } catch (Exception e) {
            System.err.println(">>> LEADER ƠI : " + e.getMessage());
            e.printStackTrace();
        }
    }
}