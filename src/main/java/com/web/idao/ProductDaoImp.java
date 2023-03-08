package com.web.idao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import com.web.model.Category;
import com.web.model.JPAUtil;
import com.web.model.Product;

public class ProductDaoImp {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	public String getNextIndex() {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
		int total = getAllProducts().size();
		if(total == 0) {
			return "1";
		} else {
			Query query = entity.createQuery("SELECT e.id FROM Product e ORDER BY e.id DESC");
			query.setMaxResults(1);
			String lastIndex = String.valueOf((Long) query.getSingleResult());
			int nextIndex = Integer.parseInt(lastIndex) + 1;
			entity.close();
			return String.valueOf(nextIndex);			
		}
		
	}
	
	public List<Product> getAllProducts() {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    TypedQuery<Product> query = entity.createQuery("SELECT p FROM Product p", Product.class);
	    List<Product> products = query.getResultList();
	    entity.close();
	    return products;
	}
	
	public boolean remove(Long id) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
		Product productToDelete = entity.find(Product.class, id);
		
		if (productToDelete == null) {
	        return false;
	    }
		
		try {
			entity.getTransaction().begin();
			entity.remove(productToDelete);
			entity.getTransaction().commit();
		} catch (Exception e) {
	        entity.getTransaction().rollback();
	        return false;
		} finally {
			entity.close();
		}
	    return true;
	}
	
	public boolean update(Product product) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    EntityTransaction transaction = entity.getTransaction();
	    boolean result = true;
	    try {
	        transaction.begin();
	        entity.merge(product);
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
	
	public boolean add(Product product) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    boolean result = true;
	    EntityTransaction transaction = entity.getTransaction();
	    try {
	        transaction.begin();
	        entity.persist(product);
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
	
	public List<Product> getFiltered(Long categoryId) {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		 List<Product> productList = new ArrayList<Product>();
	    try {
	        entityManager.getTransaction().begin();
	        Query query = entityManager.createNativeQuery("SELECT * FROM product WHERE category_id = :categoryId", Product.class);
	        query.setParameter("categoryId", categoryId);
	        productList = query.getResultList();
	        entityManager.getTransaction().commit();
	        return productList;
	    } catch (Exception e) {
	        entityManager.getTransaction().rollback();
	    } finally {
	        entityManager.close();
	    }
	    
	    return productList;
	}
}
