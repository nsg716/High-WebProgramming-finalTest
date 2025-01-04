/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.model.dao;

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
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Model;

/**
 * ModelDAOImpl
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Repository
public class ModelDAOImpl implements ModelDAO {


    private static final Logger logger = LogManager.getLogger(ModelDAOImpl.class);

    @Override
    public Model getModelById(String modelId) {
    	logger.entry();
        Model model = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            model = session.get(Model.class, modelId);
        } catch (HibernateException e) {
            logger.error("Error fetching model by ID: {}", e.getMessage(), e);
        }
        return model;
    }
   

    @Override
    public Model getModel(Model model) {
        logger.entry();
        if (model == null || model.getModelId() == null) {
            logger.warn("Model or Model ID is null. Returning null.");
            return null;
        }
        
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            model = session.get(Model.class, model.getModelId());
            tx.commit();
        } catch (HibernateException e) {
            logger.error("Error fetching model: {}", e.getMessage(), e);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException("Error fetching model", e);
        }
        return model;
    }

	@Override
    public Model createModel(Model model) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        if (isModelIdDuplicate(model.getModelId())) {
            throw new MessageException("Model ID is already in use");
        }
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(model);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException("Error creating model: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return model;
    }
	 
	    
    @Override
    public Model updateModel(Model model) {
        logger.entry();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(model);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new MessageException("Error updating model: " + e.getMessage(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return model;
    }

    @Override
    public void deleteModel(Model model) {
    	 logger.entry();
         Session session = null;
         Transaction tx = null;
         try {
             session = HibernateUtil.getSessionFactory().openSession();
             tx = session.beginTransaction();
             session.remove(model);
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
    public List<Model> getModels(Model model) {
    	logger.entry();
        List<Model> models = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Model> criteria = cb.createQuery(Model.class);
            Root<Model> root = criteria.from(Model.class);
            criteria.select(root);
            List<Predicate> predicates = new ArrayList<>();
            if (model != null) {
                if (model.getModelId() != null && !model.getModelId().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("modelId")), "%" + model.getModelId().toLowerCase() + "%"));
                }

                if (model.getModelName() != null && !model.getModelName().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("modelName")), "%" + model.getModelName().toLowerCase() + "%"));
                }

                if (model.getVersion() != null && !model.getVersion().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("version")), "%" + model.getVersion().toLowerCase() + "%"));
                }
                
                if (model.getModelType() != null && !model.getModelType().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("modelType")), "%" + model.getModelType().toLowerCase() + "%"));
                }

                if (model.getFramework() != null && !model.getFramework().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("framework")), "%" + model.getFramework().toLowerCase() + "%"));
                }
               

                if (!predicates.isEmpty()) {
                    criteria.select(root).where(cb.or(predicates.toArray(new Predicate[0])));
                }
            } else {
                criteria.select(root); // No filters, select all
            }

            models = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching models: {}", e.getMessage(), e);
        }

        return models;
    }


	@Override
    public List<Model> getModelsByAdminId(String ID) {
        logger.entry();
        List<Model> models = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Model> criteria = cb.createQuery(Model.class);
            Root<Model> root = criteria.from(Model.class);
            criteria.select(root).where(cb.equal(root.get("ID"), ID));
            models = session.createQuery(criteria).getResultList();
        } catch (HibernateException e) {
            logger.error("Error fetching models by admin ID: {}", e.getMessage());
        }
        return models;
    }
	
    @Override
    public boolean isModelIdDuplicate(String modelId) {
        logger.entry();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<Model> root = criteria.from(Model.class);

            // Count users with the given ID
            criteria.select(cb.count(root)).where(cb.equal(root.get("modelId"), modelId));
            Long count = session.createQuery(criteria).getSingleResult();
            return count > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    

    
    @Override
    public List<Model> filterModels(String ID, String name, String version, String type) {
        logger.entry();
        List<Model> filteredModels = new ArrayList<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Model> criteria = cb.createQuery(Model.class);
            Root<Model> root = criteria.from(Model.class);

            // Create a list of predicates for filtering
            List<Predicate> predicates = new ArrayList<>();

            // Add admin ID filter (mandatory)
            predicates.add(cb.equal(root.get("ID"), ID));

            // Optional filters for name, version, and type
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("modelName")), "%" + name.toLowerCase() + "%"));
            }
            
            if (version != null && !version.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("version")), "%" + version.toLowerCase() + "%"));
            }
            
            if (type != null && !type.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("modelType")), "%" + type.toLowerCase() + "%"));
            }
            if (type != null && !type.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("framework")), "%" + type.toLowerCase() + "%"));
            }
            // Combine predicates
            criteria.where(predicates.toArray(new Predicate[0]));

            // Execute query
            filteredModels = session.createQuery(criteria).getResultList();

        } catch (HibernateException e) {
            logger.error("Error filtering models: {}", e.getMessage());
        }

        return filteredModels;
    }
    
    
    
    @Override
    public List<Model> getAllModels(){
	    logger.entry();
	    List<Model> models = null;
	
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
		    CriteriaBuilder cb = session.getCriteriaBuilder();
		    CriteriaQuery<Model> criteria = cb.createQuery(Model.class);
		    Root<Model> root = criteria.from(Model.class);
		    criteria.select(root);
		
		    models = session.createQuery(criteria).getResultList();
		} catch (HibernateException e) {
		    e.printStackTrace();
		}
	
	return models;

    }
}