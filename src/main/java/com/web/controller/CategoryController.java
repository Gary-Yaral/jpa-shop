package com.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.web.idao.CategoryDaoImp;
import com.web.model.Category;

@ManagedBean(name = "categoryController")
@RequestScoped
public class CategoryController {
	
	private int total;
	private int rowIndex = 0;
	private String statusToAdd;
	private static String errorMessage;
	private String generatedError;
	private static String statusLoaded;
	

	public CategoryController() {
		String error = CategoryController.errorMessage;
		setGeneratedError(error);
		String status = CategoryController.statusLoaded;
		setStatusToAdd(status);
		setTotal(this.getAll().size());
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public static String getStatusLoaded() {
		return statusLoaded;
	}
	
	public static void setStatusLoaded(String statusLoaded) {
		CategoryController.statusLoaded = statusLoaded;
	}
	
	public static String getErrorMessage() {
		return errorMessage;
	}

	public static void setErrorMessage(String errorMessage) {
		CategoryController.errorMessage = errorMessage;
	}


	public String getGeneratedError() {
		return generatedError;
	}


	public void setGeneratedError(String generatedError) {
		this.generatedError = generatedError;
	}


	public int getRowIndex() {
	    return rowIndex;
	}
	

	public String getStatusToAdd() {
		return statusToAdd;
	}

	public void setStatusToAdd(String statusToAdd) {
		this.statusToAdd = statusToAdd;
	}

	public void setRowIndex(int rowIndex) {
	    this.rowIndex = rowIndex;
	}
	
	public int increment() {
		setRowIndex(this.rowIndex + 1);
		return this.rowIndex;
	}
	
	public List<Category> getAll() {
		CategoryDaoImp categoryDAO = new CategoryDaoImp();
		return categoryDAO.getAllCategories();
	}
	
	public void remove(Long id) {
		CategoryDaoImp categoryDAO = new CategoryDaoImp();
		try {
			categoryDAO.remove(id);
			System.out.println("Se eliminó");
		} catch(Exception e) {
			System.out.println(e);			
		}
	}
	
	public String goUpdate(Category category) throws IOException {
		setErrorMessage(null);
		setStatusLoaded(category.getStatus());
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("category", category);
	    context.getExternalContext().redirect("categoryEdit.jsf");
	    return "categoryEdit.jsf";

	}
	
	public String update(Category category) throws IOException {
		if(category.getName().isEmpty()) {
			setErrorMessage("No has ingresado el nombre");
		} else {
			UsuarioController userController = new UsuarioController();
			category.setStatus(statusToAdd);
			CategoryDaoImp categoryDAO = new CategoryDaoImp();
			if(categoryDAO.update(category)) {
				setErrorMessage(null);
				setStatusLoaded(null);
				userController.setView("categories");
				FacesContext context = FacesContext.getCurrentInstance();
			    context.getExternalContext().redirect("dashboard.jsf");
			} else {
				setErrorMessage("No has ingresado el nombre");
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
		Category category = new Category();
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("category", category);
	    context.getExternalContext().redirect("categoryNew.jsf");
	    return "categoryNew.jsf";
	}
	
	public String addNew(Category category) throws IOException {
		category.setStatus(statusToAdd);
		CategoryDaoImp categoryDAO = new CategoryDaoImp();
		if(categoryDAO.add(category)) {
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    return "dashboard.jsf";
		} else {
			return "";
		}
	}
	

}
