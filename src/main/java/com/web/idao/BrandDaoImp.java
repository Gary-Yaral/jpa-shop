package com.web.idao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.web.model.Brand;
import com.web.model.JPAUtil;

public class BrandDaoImp {
	
	public List<Brand> getAllBrands() {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
		List<Brand> brands = new ArrayList<Brand>();
		try {
			TypedQuery<Brand> query = entity.createQuery("SELECT p FROM Brand p", Brand.class);
			brands = query.getResultList();			
		} catch (Exception e) {
			
		} finally {
			entity.close();
		}
		
	    return brands;
	}
	
	public Brand searchOne(Long idBrand) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    Brand brand = entity.find(Brand.class, idBrand);
	    return brand;
	}
	
	public boolean remove(Long id) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();

		try {
			if(hasProducts(id)) return false;
			Brand brandToDelete = entity.find(Brand.class, id);
			if (brandToDelete == null) return false;
			entity.getTransaction().begin();
			entity.remove(brandToDelete);
			entity.getTransaction().commit();
			return true;
		} catch (Exception e) {
	        entity.getTransaction().rollback();
	        return false;
		} finally {
			entity.close();
		}
	}
	
	public boolean update(Brand brand) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
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
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
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
	    } finally {
	    	entity.close();
	    }
	    return result;
	}
	
	public boolean hasProducts(long id) {
		EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	    String jpql = "SELECT COUNT(p) FROM Product p WHERE p.brand.id = :brandId";
	    TypedQuery<Long> query = entity.createQuery(jpql, Long.class);
	    query.setParameter("branId", id);
	    Long count = query.getSingleResult();
	    return count > 0;
	}

}
