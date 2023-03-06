package com.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.web.idao.BrandDaoImp;
import com.web.model.Brand;

@ManagedBean(name = "brandController")
@RequestScoped
public class BrandController {
	
	private int rowIndex = 1;

	private String statusToAdd;

	public String getStatusToAdd() {
		return statusToAdd;
	}

	public void setStatusToAdd(String statusToAdd) {
		this.statusToAdd = statusToAdd;
	}

	public int getRowIndex() {
	    return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
	    this.rowIndex = rowIndex;
	}
	
	public List<Brand> getAll() {
		BrandDaoImp brandDAO = new BrandDaoImp();
		return brandDAO.getAllBrands();
	}
	
	public void remove(Long id) {
		BrandDaoImp brandDAO = new BrandDaoImp();
		if(brandDAO.remove(id)) {
			System.out.println("Se eliminó");
		} else {
			System.out.println("Tiene elementos vinculados");
		}	
	}
	
	public String goUpdate(Brand brand) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("brand", brand);
	    context.getExternalContext().redirect("brandEdit.jsf");
	    return "brandEdit.jsf";

	}
	
	public String update(Brand brand) throws IOException {
		BrandDaoImp brandDAO = new BrandDaoImp();
		if(brandDAO.update(brand)) {
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    return "dashboard.jsf";
		} else {
			return "";
		}
	}
	
	public String create() throws IOException {
		Brand brand = new Brand();
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("brand", brand);
	    context.getExternalContext().redirect("brandNew.jsf");
	    return "brandNew.jsf";
	}
	
	public String addNew(Brand brand) throws IOException {
		brand.setStatus(statusToAdd);
		BrandDaoImp brandDAO = new BrandDaoImp();
		if(brandDAO.add(brand)) {
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    return "dashboard.jsf";
		} else {
			return "";
		}
	}
	

}
