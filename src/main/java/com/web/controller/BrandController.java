package com.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.web.idao.BrandDaoImp;
import com.web.model.Brand;

@ManagedBean(name = "brandController")
@RequestScoped
public class BrandController {
	
	private int total;
	private int rowIndex = 0;
	private static String errorMessage;
	private String generatedError;
	private static String statusLoaded;
	private String statusToAdd;
	private List<Brand> list = new ArrayList<Brand>();
	private Brand brand = new Brand();
	private static Brand brandLoaded = new Brand();

	public BrandController() {
		String error = getErrorMessage();
		setGeneratedError(error);
		String status = BrandController.statusLoaded;
		setStatusToAdd(status);
		setTotal(this.getAll().size());
		setList(getAll());
		Brand loaded = BrandController.brandLoaded;
		setBrand(loaded);
	}
	
	public static Brand getBrandLoaded() {
		return brandLoaded;
	}

	public static void setBrandLoaded(Brand brandLoaded) {
		BrandController.brandLoaded = brandLoaded;
	}
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	public List<Brand> getList() {
		return list;
	}

	public void setList(List<Brand> list) {
		this.list = list;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	
	public String getGeneratedError() {
		return generatedError;
	}

	public void setGeneratedError(String generateError) {
		this.generatedError = generateError;
		
	}

	public static String getErrorMessage() {
		return errorMessage;
	}

	public static void setErrorMessage(String errorMessage) {
		BrandController.errorMessage = errorMessage;
	}
	
	public String stringError() {
		String error = BrandController.errorMessage;
		return error;
	}

	public static String getStatusLoaded() {
		return statusLoaded;
	}

	public static void setStatusLoaded(String statusLoaded) {
		BrandController.statusLoaded = statusLoaded;		
	}


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
	
	public int increment() {
		setRowIndex(this.rowIndex + 1);
		return this.rowIndex;
	}
	
	public List<Brand> getAll() {
		BrandDaoImp brandDAO = new BrandDaoImp();
		return brandDAO.getAllBrands();
	}
	
	public String remove(Long id) {
		BrandDaoImp brandDAO = new BrandDaoImp();
		brandDAO.remove(id);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	        ec.redirect(ec.getRequestContextPath() + ec.getRequestServletPath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public String goUpdate(Brand brand) throws IOException {
		setErrorMessage(null);
		setStatusLoaded(brand.getStatus());
		setBrandLoaded(brand);
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("brand", brand);
	    context.getExternalContext().redirect("brandEdit.jsf");
	    return "brandEdit.jsf";

	}
	
	public String update() throws IOException {
		if(brand.getName().isEmpty()) {
			setErrorMessage("No has ingresado el nombre");
		} else {
			BrandDaoImp brandDAO = new BrandDaoImp();
			brand.setStatus(statusToAdd);
			if(brandDAO.update(brand)) {
				setErrorMessage(null);
				setStatusLoaded(null);
				FacesContext context = FacesContext.getCurrentInstance();
			    context.getExternalContext().redirect("dashboard.jsf");
			    return null;
			} else {
				setErrorMessage("No se ha podido actualizar la marca");
				return "";
			}
		}
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	        ec.redirect(ec.getRequestContextPath() + ec.getRequestServletPath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public String create() throws IOException {
		setErrorMessage(null);
		setBrandLoaded(new Brand());
		FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("brandNew.jsf");
	    return "brandNew.jsf";
	}
	
	public String addNew() throws IOException {
		if(brand.getName().isEmpty()) {
			setErrorMessage("No has ingresado el nombre");
		} else {
			brand.setStatus(statusToAdd);
			BrandDaoImp brandDAO = new BrandDaoImp();
			if(brandDAO.add(brand)) {
				setErrorMessage(null);
				FacesContext context = FacesContext.getCurrentInstance();
			    context.getExternalContext().redirect("dashboard.jsf");
			} else {
				setErrorMessage("No se ha podido guardar la marca");
			}
		}
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	        ec.redirect(ec.getRequestContextPath() + ec.getRequestServletPath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return null;
	}

}
