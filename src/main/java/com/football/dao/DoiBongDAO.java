package com.football.dao;

import com.football.entity.DoiBong;
import com.football.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class DoiBongDAO extends BaseDAO<DoiBong> {
    
    public DoiBongDAO() {
        super(DoiBong.class);
    }

    
   // MỤC 2: TÍNH NĂNG BẢNG XẾP HẠNG (LEAGUE TABLE)
   
    
    public List<DoiBong> getBangXepHang() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Thuật toán: 
            // 1. Ưu tiên 1: Sắp xếp theo Điểm Số giảm dần (DESC)
            // 2. Ưu tiên 2: Nếu bằng điểm, tự động xét Hiệu Số giảm dần
            String hql = "FROM DoiBong d ORDER BY d.diem DESC, d.hieuSo DESC";
            
            Query<DoiBong> query = session.createQuery(hql, DoiBong.class);
            return query.list();
            
        } catch (Exception e) {
            System.err.println(">>> Lỗi truy vấn BXH: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}