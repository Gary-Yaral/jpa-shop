package com.web.idao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.web.model.JPAUtil;
import com.web.model.Product;

public class ProductDaoImp {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	public List<Product> getAllProducts() {
	    TypedQuery<Product> query = entity.createQuery("SELECT p FROM Product p", Product.class);
	    List<Product> products = query.getResultList();
	    entity.close();
	    return products;
	}
	
	public boolean remove(Long id) {
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
		}
	    return true;
	}
	
	public boolean update(Product product) {
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
	    }
	    return result;
	}
	
	public boolean add(Product product) {
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
	    }
	    return result;
	}

}
