package com.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import com.web.idao.CategoryDaoImp;
import com.web.model.Category;

@ManagedBean(name = "categoryController")
@RequestScoped
public class CategoryController {
	
	private int rowIndex = 1;
	public int getRowIndex() {
	    return rowIndex;
	}
	
	private String statusToAdd;

	public String getStatusToAdd() {
		return statusToAdd;
	}

	public void setStatusToAdd(String statusToAdd) {
		this.statusToAdd = statusToAdd;
	}

	public void setRowIndex(int rowIndex) {
	    this.rowIndex = rowIndex;
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
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("category", category);
	    context.getExternalContext().redirect("categoryEdit.jsf");
	    return "categoryEdit.jsf";

	}
	
	public String update(Category category) throws IOException {
		UsuarioController userController = new UsuarioController();
		category.setStatus(statusToAdd);
		CategoryDaoImp categoryDAO = new CategoryDaoImp();
		if(categoryDAO.update(category)) {
			userController.setView("categories");
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    return "dashboard.jsf";
		} else {
			return "";
		}
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
