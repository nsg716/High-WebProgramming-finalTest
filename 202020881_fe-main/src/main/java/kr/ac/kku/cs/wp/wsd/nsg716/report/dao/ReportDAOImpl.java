/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.report.dao;

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
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Report;
/**
 * ReportDAOImpl
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Repository
public class ReportDAOImpl implements ReportDAO {

    private static final Logger logger = LogManager.getLogger(ReportDAOImpl.class);

    @Override
    public Report getReportById(String reportId) {
    	logger.entry();
        Report report = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            report = session.get(Report.class, reportId);
        } catch (HibernateException e) {
            logger.error("Error fetching report by ID: {}", e.getMessage(), e);
        }
        return report;
    }

    @Override
    public Report createReport(Report report) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(report);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error creating report: {}", e.getMessage(), e);
            throw new MessageException("Error creating report: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return report;
    }

    @Override
    public Report updateReport(Report report) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(report);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error updating report: {}", e.getMessage(), e);
            throw new MessageException("Error updating report: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return report;
    }

    @Override
    public void deleteReport(Report report) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.remove(report);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error deleting report: {}", e.getMessage(), e);
            throw new MessageException("Error deleting report: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        
    }
    

    @Override
    public List<Report> getReportsByAdminId(String adminId) {
    	logger.entry();
        List<Report> reports = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Report> criteria = cb.createQuery(Report.class);
            Root<Report> root = criteria.from(Report.class);
            criteria.select(root).where(cb.equal(root.get("adminId"), adminId));
            reports = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching reports by admin ID: {}", e.getMessage(), e);
        }
        return reports;
    }

    @Override
    public List<Report> getReportsByModelId(String modelId) {
    	logger.entry();
        List<Report> reports = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Report> criteria = cb.createQuery(Report.class);
            Root<Report> root = criteria.from(Report.class);
            criteria.select(root).where(cb.equal(root.get("modelId"), modelId));
            reports = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching reports by model ID: {}", e.getMessage(), e);
        }
        return reports;
    }
    
    @Override
    public List<Report> getReports(Report report) {
    	logger.entry();
        List<Report> reports = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Report> criteria = cb.createQuery(Report.class);
            Root<Report> root = criteria.from(Report.class);
            criteria.select(root);
            List<Predicate> predicates = new ArrayList<>();

            if (report != null) {
                // Add predicates for each field that might be used for filtering
                if (report.getReportId() != null && !report.getReportId().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("reportId")), "%" + report.getReportId().toLowerCase() + "%"));
                }

                if (report.getModel() != null && !report.getModel().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("model")), "%" + report.getModel().toLowerCase() + "%"));
                }

                if (report.getAdmin() != null && !report.getAdmin().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("admin")), "%" + report.getAdmin().toLowerCase() + "%"));
                }

                if (report.getUser() != null && !report.getUser().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("user")), "%" + report.getUser().toLowerCase() + "%"));
                }

                if (report.getReportName() != null && !report.getReportName().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("reportName")), "%" + report.getReportName().toLowerCase() + "%"));
                }

                if (report.getModelName() != null && !report.getModelName().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("modelName")), "%" + report.getModelName().toLowerCase() + "%"));
                }

                // For numeric and date fields, you might want to add more specific filtering
                if (report.getAccuracy() != null) {
                    predicates.add(cb.equal(root.get("accuracy"), report.getAccuracy()));
                }

                if (report.getTotalProcessingAmount() != null) {
                    predicates.add(cb.equal(root.get("totalProcessingAmount"), report.getTotalProcessingAmount()));
                }

                if (report.getUsageDate() != null) {
                    predicates.add(cb.equal(root.get("usageDate"), report.getUsageDate()));
                }

                if (!predicates.isEmpty()) {
                    criteria.select(root).where(cb.or(predicates.toArray(new Predicate[0])));
                }
            } else {
                criteria.select(root); // No filters, select all
            }

            reports = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching reports: {}", e.getMessage(), e);
        }

        return reports;
    }
    
    

    @Override
	public boolean isReportIdDuplicate(String reportId) {
    	  logger.entry();
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
              CriteriaBuilder cb = session.getCriteriaBuilder();
              CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
              Root<Report> root = criteria.from(Report.class);

              // Count users with the given ID
              criteria.select(cb.count(root)).where(cb.equal(root.get("reportId"), reportId));
              Long count = session.createQuery(criteria).getSingleResult();
              return count > 0;
          } catch (HibernateException e) {
              e.printStackTrace();
          }
          return false;
	}

	@Override
    public List<Report> getAllReports() {
        logger.entry();
        List<Report> reports = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Report> criteria = cb.createQuery(Report.class);
            Root<Report> root = criteria.from(Report.class);
            criteria.select(root);
            reports = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching all reports: {}", e.getMessage(), e);
        }
        return reports;
    }

    @Override
    public List<Report> filterReports(String adminId, String modelId, String status, String dateFrom, String dateTo) {
        logger.entry();
        List<Report> filteredReports = new ArrayList<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Report> criteria = cb.createQuery(Report.class);
            Root<Report> root = criteria.from(Report.class);

            // Create a list of predicates for filtering
            List<Predicate> predicates = new ArrayList<>();

            // Add filters based on provided criteria
            if (adminId != null && !adminId.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("adminId"), adminId));
            }
            
            if (modelId != null && !modelId.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("modelId"), modelId));
            }
            
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
            }
            
            // You might need to adjust date filtering based on your exact date type and format
            // This is a placeholder and might need modification based on your specific date handling
            if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                // Assuming there's a 'reportDate' field
                predicates.add(cb.greaterThanOrEqualTo(root.get("reportDate"), dateFrom));
            }
            
            if (dateTo != null && !dateTo.trim().isEmpty()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("reportDate"), dateTo));
            }

            // Combine predicates
            if (!predicates.isEmpty()) {
                criteria.where(predicates.toArray(new Predicate[0]));
            }

            // Execute query
            filteredReports = session.createQuery(criteria).getResultList();

        } catch (HibernateException e) {
            logger.error("Error filtering reports: {}", e.getMessage(), e);
        }

        return filteredReports;
    }
}