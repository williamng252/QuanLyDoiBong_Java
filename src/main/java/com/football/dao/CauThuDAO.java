package com.football.dao;

import java.util.List;

import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.football.entity.CauThu;
import com.football.entity.DoiBong;
import com.football.util.HibernateUtil;

public class CauThuDAO extends BaseDAO<CauThu> {
    
    public CauThuDAO() {
        super(CauThu.class);
    }

    // 1. TÌM KIẾM THEO TÊN (Like Search)
    public List<CauThu> timKiemTheoTen(String tuKhoa) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM CauThu c WHERE c.ten LIKE :keyword";
            Query<CauThu> query = session.createQuery(hql, CauThu.class);
            query.setParameter("keyword", "%" + tuKhoa + "%"); 
            return query.list();
        }
    }

    // 2. LỌC THEO QUỐC TỊCH (Dùng mã quốc tịch như VN, ENG)
    public List<CauThu> locTheoQuocTich(String maQT) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL cực hay: Truy cập thẳng vào thuộc tính quocTich của đối tượng CauThu
            String hql = "FROM CauThu c WHERE c.quocTich.maQT = :ma";
            Query<CauThu> query = session.createQuery(hql, CauThu.class);
            query.setParameter("ma", maQT);
            return query.list();
        }
    }

    // 3. LỌC THEO VỊ TRÍ (Dùng mã vị trí như TDO, HVE)
    public List<CauThu> locTheoViTri(String maVT) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM CauThu c WHERE c.viTri.maVT = :ma";
            Query<CauThu> query = session.createQuery(hql, CauThu.class);
            query.setParameter("ma", maVT);
            return query.list();
        }
    }

    // 4. LỌC THEO ĐỘI BÓNG (Xem danh sách cầu thủ của 1 đội)
    public List<CauThu> locTheoDoiBong(String maDoi) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM CauThu c WHERE c.doiBong.maDoi = :ma";
            Query<CauThu> query = session.createQuery(hql, CauThu.class);
            query.setParameter("ma", maDoi);
            return query.list();
        }
    }

    // 5. TÌM THEO SỐ ÁO (Kiểm tra xem số áo đó đã có ai mặc chưa)
    public List<CauThu> timTheoSoAo(int soAo, String maDoi) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM CauThu c WHERE c.soAo = :so AND c.doiBong.maDoi = :ma";
            Query<CauThu> query = session.createQuery(hql, CauThu.class);
            query.setParameter("so", soAo);
            query.setParameter("ma", maDoi);
            return query.list();
        }
    }
 // LOGIC: Thực hiện chuyển nhượng cầu thủ
    public boolean thucHienChuyenNhuong(String maCauThu, String maDoiMoi, com.football.entity.HopDong hopDongMoi) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 1. Lấy cầu thủ và đội bóng mới
            CauThu ct = session.get(CauThu.class, maCauThu);
            DoiBong doiMoi = session.get(DoiBong.class, maDoiMoi);

            if (ct == null || doiMoi == null) return false;

            // 2. Đổi đội bóng cho cầu thủ
            ct.setDoiBong(doiMoi);
            session.update(ct);

            // 3. Tạo record hợp đồng mới (Lưu ngày ký, mức lương...)
            hopDongMoi.setCauThu(ct);
            hopDongMoi.setDoiBong(doiMoi);
            session.save(hopDongMoi);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}