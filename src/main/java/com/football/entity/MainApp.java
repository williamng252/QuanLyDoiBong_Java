package com.football.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.football.entity.QuocTich;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Khởi tạo Hibernate
            SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            
            // Mở session để test tạo bảng
            Session session = factory.openSession();
            
            System.out.println("------------------------------------------");
            System.out.println(">>> CHÚC MỪNG ! KẾT NỐI DATABASE THÀNH CÔNG!");
            System.out.println(">>> BẢNG 'quoc_tich' ĐÃ ĐƯỢC TỰ ĐỘNG TẠO.");
            System.out.println("------------------------------------------");
            
            session.close();
            factory.close();
        } catch (Exception e) {
            System.err.println(">>> LỖI RỒI : " + e.getMessage());
            e.printStackTrace();
        }
    }
}