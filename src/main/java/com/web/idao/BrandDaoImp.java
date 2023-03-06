package com.web.idao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.web.model.Brand;
import com.web.model.JPAUtil;

public class BrandDaoImp {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	public List<Brand> getAllBrands() {
	    TypedQuery<Brand> query = entity.createQuery("SELECT p FROM Brand p", Brand.class);
	    List<Brand> brands = query.getResultList();
	    return brands;
	}
	
	public Brand searchOne(Long idBrand) {
	    Brand brand = entity.find(Brand.class, idBrand);
	    return brand;
	}
	
	public boolean remove(Long id) {
		Brand brandToDelete = entity.find(Brand.class, id);
		
		if (brandToDelete == null) {
	        return false;
	    }
		
		try {
			entity.getTransaction().begin();
			entity.remove(brandToDelete);
			entity.getTransaction().commit();
		} catch (Exception e) {
	        entity.getTransaction().rollback();
	        return false;
		}
	    return true;
	}
	
	public boolean update(Brand brand) {
	    EntityTransaction transaction = entity.getTransaction();
	    boolean result = true;
	    try {
	        transaction.begin();
	        entity.merge(brand);
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
	
	public boolean add(Brand brand) {
	    boolean result = true;
	    EntityTransaction transaction = entity.getTransaction();
	    try {
	        transaction.begin();
	        entity.persist(brand);
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
