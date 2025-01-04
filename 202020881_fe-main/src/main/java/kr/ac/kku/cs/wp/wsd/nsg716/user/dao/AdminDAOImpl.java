/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.dao;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.tools.message.MessageException;
import kr.ac.kku.cs.wp.wsd.nsg716.tools.secure.CryptoUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;

/**
 * AdminDAOImpl
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Repository
public class AdminDAOImpl implements AdminDAO {
  
	private static final Logger logger = LogManager.getLogger(AdminDAOImpl.class);

    @Override
    public Admin getAdminById(String adminId) {
        logger.entry();
        Admin admin = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            admin = session.get(Admin.class, adminId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public boolean validateAdminCredentials(String adminId, String password) {
        logger.entry();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Admin admin = session.get(Admin.class, adminId);
            if (admin != null) {
                return CryptoUtil.verify(password, admin.getPassword());
            }
        } catch (HibernateException e) {
            logger.error("Hibernate exception occurred: {}", e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm not found: {}", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isAdminIdDuplicate(String adminId) {
        logger.entry();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<Admin> root = criteria.from(Admin.class);

            criteria.select(cb.count(root)).where(cb.equal(root.get("adminId"), adminId));
            Long count = session.createQuery(criteria).getSingleResult();
            return count > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(admin);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException(e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return admin;
    }

    @Override
    public void deleteAdmin(Admin admin) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.remove(admin);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException(e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Admin> getAdmins(Admin admin) {
        logger.entry();
        List<Admin> admins = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Admin> criteria = cb.createQuery(Admin.class);
            Root<Admin> root = criteria.from(Admin.class);
            criteria.select(root);
            List<Predicate> cds = new ArrayList<>();
            if (admin != null) {
                if (admin.getId() != null && !admin.getId().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("adminId")), "%" + admin.getId().toLowerCase() + "%"));

                if (admin.getName() != null && !admin.getName().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("name")), "%" + admin.getName().toLowerCase() + "%"));

                if (admin.getEmail() != null && !admin.getEmail().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("email")), "%" + admin.getEmail().toLowerCase() + "%"));

                Predicate[] pArr = new Predicate[cds.size()];
                criteria.select(root).where(cb.or(cds.toArray(pArr)));
            }
            admins = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return admins;
    }
    
    @Override
  	public boolean isPasswordValid(String password) {
  		// TODO Auto-generated method stub
  		return false;
  	}

}
