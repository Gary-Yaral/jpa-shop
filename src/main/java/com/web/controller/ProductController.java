package com.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.web.idao.BrandDaoImp;
import com.web.idao.CategoryDaoImp;
import com.web.idao.ProductDaoImp;
import com.web.model.Brand;
import com.web.model.Category;
import com.web.model.Product;
import com.web.model.SelectItem;

@ManagedBean(name = "productController")
@RequestScoped
public class ProductController {
	CategoryDaoImp categoryDAO = new CategoryDaoImp();
	BrandDaoImp brandDAO = new BrandDaoImp();
	List<SelectItem> categorySelect = new ArrayList<SelectItem>();
	List<SelectItem> brandSelect = new ArrayList<SelectItem>();	
	List<SelectItem> brands, categories;
	private static String selectedBrand;
	private static String selectedCategory;
	private static String selectedStatus = "Activo";
	
	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		ProductController.selectedStatus = selectedStatus;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		ProductController.selectedCategory = selectedCategory;
	}

	public ProductController() {
		List<Brand> allBrands = brandDAO.getAllBrands();
		List<Category> allCategories = categoryDAO.getAllCategories();
		brands = new ArrayList<SelectItem>();
		categories = new ArrayList<SelectItem>();
		for (Brand brand : allBrands) {
			SelectItem item = new SelectItem();
			item.setId(String.valueOf(brand.getId()));
			item.setName(brand.getName());
			brands.add(item);
		}
		
		for (Category category : allCategories) {
			SelectItem item = new SelectItem();
			item.setId(String.valueOf(category.getId()));
			item.setName(category.getName());
			categories.add(item);
		}
	}
	
	public String getSelectedBrand() {
		return selectedBrand;
	}

	public void setSelectedBrand(String selectedBrand) {
		ProductController.selectedBrand = selectedBrand;
	}

	public List<SelectItem> getCategories() {
		return categories;
	}
	
	public List<SelectItem> getBrands() {
		return brands;
	}

	
	// Index de la tabla
	private int rowIndex = 1;

	public int getRowIndex() {
	    return rowIndex;
	}
	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	private String brandAdd;
	
	// Sirve para guardar el indice de la marca que se selecciona
	public String getBrandAdd() {
		return brandAdd;
	}

	public void setBrandAdd(String brandAdd) {
		this.brandAdd = brandAdd;
	}
	
	// Obtenemos lista de productos
	public List<Product> getAll() {
		ProductDaoImp productDAO = new ProductDaoImp();
		return productDAO.getAllProducts();
	}
	
	// eliminamos un producto
	public void remove(Long id) {
		ProductDaoImp productDAO = new ProductDaoImp();
		try {
			productDAO.remove(id);
			System.out.println("Error");			
		} catch(Exception e) {
		}
		if(productDAO.remove(id)) {
			System.out.println("Se eliminó");
		} else {
		}
	}
	
	// Redirecciona a la ventana de actualizacion
	public String goUpdate(Product product) throws IOException {
		String brandId = String.valueOf(product.getBrand().getId());
		String categoryId = String.valueOf(product.getCategory().getId());
		setSelectedStatus(product.getStatus());
		setSelectedBrand(brandId);
		setSelectedCategory(categoryId);
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("product", product);
	    context.getExternalContext().redirect("productEdit.jsf");
	    return "productEdit.jsf";
	}
	
	// Actualiza un producto
	public String update(Product product) throws IOException {
		Brand brand = brandDAO.searchOne((long)Integer.parseInt(selectedBrand));
		Category category = categoryDAO.searchOne((long)Integer.parseInt(selectedCategory));
		product.setBrand(brand);
		product.setCategory(category);
		product.setStatus(selectedStatus);
		System.out.println(selectedBrand);
		ProductDaoImp productDAO = new ProductDaoImp();
		if(productDAO.update(product)) {
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    return "dashboard.jsf";
		} else {
			return "";
		}
	}
	
	// Redirecciona a la ventana de crear nuevo producto
	public String create() throws IOException {
		Product product = new Product();
		setSelectedStatus("Activo");
		setSelectedBrand(null);
		setSelectedCategory(null);
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("product", product);
	    context.getExternalContext().redirect("productNew.jsf");
	    return "productNew.jsf";
	}
	
	// Agrega nuevo producto
	public String addNew(Product product) throws IOException {
		Brand brand = brandDAO.searchOne((long)Integer.parseInt(selectedBrand));
		Category category = categoryDAO.searchOne((long)Integer.parseInt(selectedCategory));
		product.setBrand(brand);
		product.setCategory(category);
		product.setStatus(selectedStatus);
		product.setDate(new Date());
		ProductDaoImp productDAO = new ProductDaoImp();
		if(productDAO.add(product)) {
			FacesContext context = FacesContext.getCurrentInstance();
		    context.getExternalContext().redirect("dashboard.jsf");
		    System.out.println("Se agregó");
		    return "dashboard.jsf";
		} else {
			System.out.println("Error");
			return "";
		}
	}

	public CategoryDaoImp getCategoryDAO() {
		return categoryDAO;
	}

	public void setCategoryDAO(CategoryDaoImp categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public BrandDaoImp getBrandDAO() {
		return brandDAO;
	}

	public void setBrandDAO(BrandDaoImp brandDAO) {
		this.brandDAO = brandDAO;
	}


}
