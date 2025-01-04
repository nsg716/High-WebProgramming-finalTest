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
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import kr.ac.kku.cs.wp.wsd.nsg716.support.sql.HibernateUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.tools.message.MessageException;
import kr.ac.kku.cs.wp.wsd.nsg716.tools.secure.CryptoUtil;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;

/**
 * UserDAOImpl
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Repository
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public User getUserById(String userId) {
        logger.entry();
        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, userId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(user); // User 객체를 데이터베이스에 저장
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback(); // 트랜잭션 롤백
            }
            e.printStackTrace();
        }
    }

	@Override
    public User getUser(User user) {
        logger.entry();
        Transaction tx = null;
        Session session = null;
        if (user != null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                user = session.get(User.class, user.getUserId());
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
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(user);
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
        return user;
    }

    @Override
    public void deleteUser(User user) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.remove(user);
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
    /* 새로운 사용자를 생성합니다.
     * 사용자 ID 중복, 불가능한 ID(예: "admin"), 비밀번호 유효성 검사 후에 사용자 정보를 해시화하여 저장합니다.
     * 
     * */
    @Override
    public User createUser(User user) {
        logger.entry();
        User rtn = null;
        Session session = null;
        Transaction tx = null;

        // 사용자 ID 중복 확인
        if (isUserIdDuplicate(user.getUserId())) {
            throw new MessageException("User ID is already in use");
        }

        // 생성 불가능한 ID 확인
        if (isInvalidUserId(user.getUserId())) {
            throw new MessageException("User ID cannot be 'admin' or other reserved keywords");
        }

        // 비밀번호 유효성 확인
        if (!isPasswordValid(user.getPassword())) {
            throw new MessageException("Password does not meet security requirements");
        }

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // 비밀번호 해시화
            user.setPassword(CryptoUtil.hash(user.getPassword(), CryptoUtil.genSalt()));
            
            // 사용자 정보 저장
            session.persist(user);
            tx.commit();

            // 저장된 사용자 정보 반환
            rtn = session.get(User.class, user.getUserId());
        } catch (HibernateException | NoSuchAlgorithmException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException(e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return rtn;
    }
    
    
    /*
    * 조건에 맞는 사용자 목록을 조회합니다.
    * Criteria API를 사용하여 다양한 필터링 조건을 적용하여 사용자 리스트를 반환합니다.
	*/
    @Override
    public List<User> getUsers(User user) {
        logger.entry();
        List<User> users = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = cb.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root);
            List<Predicate> cds = new ArrayList<>();
            if (user != null) {
                if (user.getUserId() != null && !user.getUserId().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("id")), "%" + user.getUserId().toLowerCase() + "%"));

                if (user.getName() != null && !user.getName().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("name")), "%" + user.getName().toLowerCase() + "%"));

                if (user.getEmail() != null && !user.getEmail().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("email")), "%" + user.getEmail().toLowerCase() + "%"));

                if (user.getStatus() != null && !user.getStatus().isEmpty())
                    cds.add(cb.like(cb.lower(root.get("status")), "%" + user.getStatus().toLowerCase() + "%"));

                Predicate[] pArr = new Predicate[cds.size()];
                criteria.select(root).where(cb.or(cds.toArray(pArr)));
            }
            users = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /*
     * 사용자 ID와 비밀번호를 검증합니다.
	 * 입력된 비밀번호와 저장된 해시 비밀번호를 비교하여 인증합니다. 
     * */
    @Override
    public boolean validateUserCredentials(String userId, String password) {
        logger.entry();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                // 입력된 비밀번호와 저장된 해시를 비교
                return CryptoUtil.verify(password, user.getPassword());
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

    /* 생성 불가능한 ID 확인 메소드
     * 관리자 계정 생성 불가 
     * 
     * */ 

    private boolean isInvalidUserId(String userId) {
        return "admin".equalsIgnoreCase(userId) || userId == null || userId.trim().isEmpty();
    }

    /* 사용자 유효성 검사 
     * 사용자 중복 확인 메소그 
     * 
     * */
    @Override
    public boolean isUserIdDuplicate(String userId) {
        logger.entry();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<User> root = criteria.from(User.class);

            // Count users with the given ID
            criteria.select(cb.count(root)).where(cb.equal(root.get("userId"), userId));
            Long count = session.createQuery(criteria).getSingleResult();
            return count > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }
	/*
	 * 비밀번호 유효성 검사 
	 * 비밀번호 길이 대문자 소문자 숫자,특수문자를 포함하는지 체크하여 반환 
	 * **/
    @Override
    public boolean isPasswordValid(String password) {
        logger.entry();
        // DTO에서 정의한 정규 표현식 기준으로 확인
        if (password == null || password.length() < 8) {
            return false; // 최소 길이 요구사항
        }
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(c -> "~`!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?".indexOf(c) >= 0);

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

  
    @Override
    public List<User> getAllUsers() {
        logger.entry();
        List<User> users = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = cb.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root);

            users = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }
    
    
    
}
