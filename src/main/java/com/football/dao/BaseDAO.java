package com.football.dao;

import com.football.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class BaseDAO<T> {
    private Class<T> entityClass;

    // Constructor:truyền tên Class vào (VD: QuocTich.class)
    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // 1.THÊM MỚI (INSERT)
    public boolean insert(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 2.CẬP NHẬT (UPDATE)
    public boolean update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 3.XÓA (DELETE)
    public boolean delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 4.LẤY TOÀN BỘ DANH SÁCH (SELECT ALL)
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Tự động tạo câu lệnh HQL: "FROM QuocTich" hoặc "FROM CauThu"
            String hql = "FROM " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 // 5. LẤY THEO ID (SELECT BY ID)
    public T getByID(java.io.Serializable id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}