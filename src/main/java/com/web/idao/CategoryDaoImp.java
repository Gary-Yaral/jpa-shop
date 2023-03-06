package com.web.idao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import com.web.model.Category;
import com.web.model.JPAUtil;

public class CategoryDaoImp {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	public List<Category> getAllCategories() {
	    TypedQuery<Category> query = entity.createQuery("SELECT p FROM Category p", Category.class);
	    List<Category> categories = query.getResultList();
	    return categories;
	}
	
	public Category searchOne(Long idCategory) {
	    Category category = entity.find(Category.class, idCategory);
	    return category;
	}
	
	public boolean remove(Long id) {
		Category categoryToDelete = entity.find(Category.class, id);
		
		if (categoryToDelete == null) {
	        return false;
	    }
		
		try {
			entity.getTransaction().begin();
			entity.remove(categoryToDelete);
			entity.getTransaction().commit();
		} catch (Exception e) {
	        entity.getTransaction().rollback();
	        return false;
		}
	    return true;
	}
	
	public boolean update(Category category) {
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
	    }
	    return result;
	}

}
