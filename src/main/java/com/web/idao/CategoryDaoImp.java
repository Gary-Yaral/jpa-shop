package com.web.idao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import com.web.model.Category;
import com.web.model.JPAUtil;

public class CategoryDaoImp {
	
	public List<Category> getAllCategories() {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
		List<Category> categories = new ArrayList<Category>();
		try {
			TypedQuery<Category> query = entity.createQuery("SELECT p FROM Category p", Category.class);
			categories = query.getResultList();
			return categories;			
		} catch(Exception e) {
		} finally {
	    	entity.close();
	    }
	    return categories;
	}
	
	public Category searchOne(Long idCategory) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
		Category category = null;
		try {
			category = entity.find(Category.class, idCategory);			
		} catch(Exception e) {
		}finally {
	    	entity.close();
	    }
	    return category;
	}
	
	public boolean remove(Long id) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();

		try {
			if(hasProducts(id)) return false;
			Category categoryToDelete = entity.find(Category.class, id);
			if (categoryToDelete == null)return false;
			entity.getTransaction().begin();
			entity.remove(categoryToDelete);
			entity.getTransaction().commit();
			return true;
		} catch (Exception e) {
	        entity.getTransaction().rollback();
	        return false;
		} finally {
	    	entity.close();
	    }
	}
	
	public boolean update(Category category) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    EntityTransaction transaction = entity.getTransaction();
	    boolean result = true;
	    try {
	        transaction.begin();
	        entity.merge(category);
	        transaction.commit();
	    } catch (Exception e) {
	        result = false;
	        if (transaction.isActive()) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        entity.close();
	    }
	    return result;
	}
	
	public boolean add(Category category) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    boolean result = true;
	    EntityTransaction transaction = entity.getTransaction();
	    try {
	        transaction.begin();
	        entity.persist(category);
	        transaction.commit();
	    } catch (Exception e) {
	        result = false;
	        if (transaction.isActive()) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	    	entity.close();
	    }
	    return result;
	}
	
	public boolean hasProducts(long id) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    String jpql = "SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId";
	    TypedQuery<Long> query = entity.createQuery(jpql, Long.class);
	    query.setParameter("categoryId", id);
	    Long count = query.getSingleResult();
	    return count > 0;
	}


}
