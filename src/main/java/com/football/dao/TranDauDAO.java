package com.football.dao;

import com.football.entity.TranDau;
import com.football.entity.DoiBong;
import com.football.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TranDauDAO extends BaseDAO<TranDau> {
    public TranDauDAO() {
        super(TranDau.class);
    }

    // LOGIC: Cập nhật kết quả và tự động tính điểm BXH
    public boolean capNhatKetQua(TranDau tran) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 1. Lưu/Cập nhật thông tin trận đấu (Tỉ số)
            session.saveOrUpdate(tran);

            // 2. Lấy 2 thực thể đội bóng từ Database lên để sửa điểm
      
            DoiBong chuNha = tran.getDoiChuNha(); 
            DoiBong khach = tran.getDoiKhach();

            // 3. Tính hiệu số (Goal Difference)
            int banNha = tran.getBanThangDoiNha();
            int banKhach = tran.getBanThangDoiKhach();
            
          
            chuNha.setHieuSo(chuNha.getHieuSo() + (banNha - banKhach));
            khach.setHieuSo(khach.getHieuSo() + (banKhach - banNha));

            // 4. Tính điểm (Points)
            if (banNha > banKhach) {
                chuNha.setDiem(chuNha.getDiem() + 3); // Đội nhà thắng
            } else if (banNha < banKhach) {
                khach.setDiem(khach.getDiem() + 3); // Đội khách thắng
            } else {
                chuNha.setDiem(chuNha.getDiem() + 1); // Hòa
                khach.setDiem(khach.getDiem() + 1);
            }

            // 5. Cập nhật lại 2 đội bóng
            session.update(chuNha);
            session.update(khach);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}