package com.football.dao;

import com.football.entity.ChiTietTranDau;
import com.football.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class ChiTietTranDauDAO extends BaseDAO<ChiTietTranDau> {
    
    public ChiTietTranDauDAO() {
        super(ChiTietTranDau.class);
    }

  
    // THUẬT TOÁN TÌM TOP GHI BÀN (VUA PHÁ LƯỚI)
  
    
    // Hàm này trả về List<Object[]>. Mỗi mảng Object[] chứa 2 phần tử:
    // Vị trí số 0: Thực thể CauThu
    // Vị trí số 1: Long (Tổng số bàn thắng)
    public List<Object[]> getTopGhiBan(int soLuongTop) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            // HQL: Chọn ra Cầu thủ và Tổng bàn thắng. 
            // Gom nhóm (GROUP BY) theo cầu thủ và Sắp xếp (ORDER BY) tổng bàn giảm dần
            String hql = "SELECT c.cauThu, SUM(c.banThang) " +
                         "FROM ChiTietTranDau c " +
                         "GROUP BY c.cauThu " +
                         "ORDER BY SUM(c.banThang) DESC";
            
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            
            // Muốn lấy Top 5 hay Top 10 thì truyền tham số vào đây
            query.setMaxResults(soLuongTop); 
            
            return query.list();
            
        } catch (Exception e) {
            System.err.println(">>> Lỗi truy vấn Vua Phá Lưới: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

 
    // TOP KIẾN TẠO (ASSISTS) 

    public List<Object[]> getTopKienTao(int soLuongTop) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            String hql = "SELECT c.cauThu, SUM(c.kienTao) " +
                         "FROM ChiTietTranDau c " +
                         "GROUP BY c.cauThu " +
                         "ORDER BY SUM(c.kienTao) DESC";
            
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setMaxResults(soLuongTop); 
            
            return query.list();
        }
    }
}