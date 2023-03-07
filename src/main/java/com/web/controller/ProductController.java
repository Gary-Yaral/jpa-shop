package com.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import com.web.idao.BrandDaoImp;
import com.web.idao.CategoryDaoImp;
import com.web.idao.ProductDaoImp;
import com.web.idao.UploadFile;
import com.web.model.Brand;
import com.web.model.Category;
import com.web.model.Product;
import com.web.model.SelectItem;

@ManagedBean(name = "productController")
@RequestScoped
public class ProductController{
	CategoryDaoImp categoryDAO = new CategoryDaoImp();
	BrandDaoImp brandDAO = new BrandDaoImp();
	List<SelectItem> categorySelect = new ArrayList<SelectItem>();
	List<SelectItem> brandSelect = new ArrayList<SelectItem>();	
	List<SelectItem> brands, categories;
	private int total; 
	private Part selectedImage;
	private String generatedError;
	private Product productSave = new Product();
	private static Product product = new Product();
	private static String errorMessage;
	private static String selectedBrand;
	private static String selectedCategory;
	private static String selectedStatus = "Activo";
	
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
		
		String error = ProductController.errorMessage;
		setGeneratedError(error);
		Product loaded = ProductController.product;
		setProductSave(loaded);
		setTotal(this.getAll().size());
	} 
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public Product getProductSave() {
		return productSave;
	}

	public void setProductSave(Product productSave) {
		this.productSave = productSave;
	}
	
	public static Product getProduct() {
		return product;
	}

	public static void setProduct(Product product) {
		ProductController.product = product;
	}
	
	public String getGeneratedError() {
		return generatedError;
	}
	
	public void setGeneratedError(String generatedError) {
		this.generatedError = generatedError;
	}
	
	public static String getErrorMessage() {
		return errorMessage;
	}
	
	public static void setErrorMessage(String errorMessage) {
		ProductController.errorMessage = errorMessage;
	}
	
	public Part getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Part selectedImage) {
		this.selectedImage = selectedImage;
	}
	
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
	private int rowIndex = 0;

	public int getRowIndex() {
	    return rowIndex;
	}
	
	public int increment() {
		setRowIndex(this.rowIndex + 1);
		return this.rowIndex;
	}
	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
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

	
	// Obtenemos lista de productos
	public List<Product> getAll() {
		ProductDaoImp productDAO = new ProductDaoImp();
		return productDAO.getAllProducts();
	}
	
	// eliminamos un producto
	public void remove(Long id, String name) {
		String path =FacesContext.getCurrentInstance().getExternalContext()+"/resources/img/"+name;
		File archivo = new File(path);
		ProductDaoImp productDAO = new ProductDaoImp();
		try {
			productDAO.remove(id);
			archivo.delete();
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
		setProduct(product);
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("product", product);
	    context.getExternalContext().redirect("productEdit.jsf");
	    return "productEdit.jsf";
	}
	
	// Actualiza un producto
	public String update(Product product) throws IOException {
		if(validateFormUpdate(product)) {	
			ProductDaoImp productDAO = new ProductDaoImp();
			UploadFile upFile = new UploadFile();
			Brand brand = brandDAO.searchOne((long)Integer.parseInt(selectedBrand));
			Category category = categoryDAO.searchOne((long)Integer.parseInt(selectedCategory));
			product.setBrand(brand);
			product.setCategory(category);
			product.setStatus(selectedStatus);
			
			System.out.println("Si valida1");

			if(productDAO.update(product)) {
				System.out.println("Si valida2");
			    if(selectedImage != null) {
			    	System.out.println("Si valida3");
			    	System.out.println(product.getImage());
			    	if(upFile.updateFile(selectedImage, product.getImage())) {
			    		System.out.println("Si valida4");
			    		setErrorMessage(null);
			    		goToDashboard();
			    		return null;
			    	} else {
			    		setErrorMessage("Error al actualizar la imagen. Solo se permiten imagenes jpg, jpeg y png.");
			    		return reloaded();
			    	} 
			    } else {
			    	setErrorMessage(null);
			    	goToDashboard(); 
			    	return null;	
			    }			    
			    
			} else {
				setErrorMessage("Error al guardar el producto");
				return reloaded();
			}
		}

		return reloaded();
	}
	
	public String goToDashboard() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("dashboard.jsf");
	    return "dashboard.jsf";
	}
	
	// Redirecciona a la ventana de crear nuevo producto
	public String create() throws IOException {
		Product product = new Product();
		setProduct(product);
		setSelectedStatus("Activo");
		setSelectedBrand(null);
		setSelectedCategory(null);
		setErrorMessage(null);
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("product", product);
	    context.getExternalContext().redirect("productNew.jsf");
	    return "productNew.jsf";
	}
	
	// Agrega nuevo producto
	public String addNew() throws IOException {
		if(validateForm(product)) {	
			ProductDaoImp productDAO = new ProductDaoImp();
			UploadFile upFile = new UploadFile();
			if(upFile.saveFile(selectedImage)) {		
				Brand brand = brandDAO.searchOne((long)Integer.parseInt(selectedBrand));
				Category category = categoryDAO.searchOne((long)Integer.parseInt(selectedCategory));
				product.setBrand(brand);
				product.setCategory(category);
				product.setStatus(selectedStatus);
				product.setDate(new Date());
				product.setImage(upFile.getFileName());
				
				if(productDAO.add(product)) {
					setErrorMessage(null);
					setSelectedBrand(null);
					setSelectedCategory(null);
					FacesContext context = FacesContext.getCurrentInstance();
					context.getExternalContext().redirect("dashboard.jsf");
				} else {
					setErrorMessage("Error al guardar el producto");
					return reloaded();
				}
				
			} else {
				setErrorMessage("Solo se admiten imagen jpg, jpeg y png");
				return reloaded();
			}
			
		}
		
		return reloaded();
		
	}
	
	public void resetProductForm() {
		setProduct(new Product());
		setSelectedBrand(null);
		setSelectedCategory(null);
	}
	
	public boolean validateForm(Product product) {
		if(product.getName().isEmpty()) {
			setErrorMessage("Ingresa el nombre");			
			return false;
		}
		
		if(product.getDescription().isEmpty()) {
			setErrorMessage("Ingresa la descripción");			
			return false;
		}
				
		if(product.getPrice().isEmpty()) {
			setErrorMessage("Ingresa el precio");			
			return false;
		}
		
		if(!isDouble(product.getPrice())) {
			setErrorMessage("Ingrese un precio válido. Formato 0.00");
			return false;
		}
		
		if(product.getStock().isEmpty()) {
			setErrorMessage("Ingresa el stock");			
			return false;
		}
		
		
		if(!isInteger(product.getStock())) {
			setErrorMessage("Solo se admiten números enteros en el stock");
			return false;
		}
		
		System.out.println(selectedBrand);
		
		if(selectedBrand.isEmpty()) {
			setErrorMessage("Debes seleccionar una marca");
			return false;
		}
		
		if(selectedCategory.isEmpty()) {
			setErrorMessage("Debes seleccionar una categoría");
			return false;
		}
		
		if(selectedImage == null) {
			setErrorMessage("Debes seleccionar una imagen");
			return false;
		}
		
		setErrorMessage(null);
		return true;
	}
	
	public boolean validateFormUpdate(Product product) {
		if(product.getName().isEmpty()) {
			setErrorMessage("Ingresa el nombre");			
			return false;
		}
		
		if(product.getDescription().isEmpty()) {
			setErrorMessage("Ingresa la descripción");			
			return false;
		}
				
		if(product.getPrice().isEmpty()) {
			setErrorMessage("Ingresa el precio");			
			return false;
		}
		
		if(!isDouble(product.getPrice())) {
			setErrorMessage("Ingrese un precio válido. Formato 0.00");
			return false;
		}
		
		if(product.getStock().isEmpty()) {
			setErrorMessage("Ingresa el stock");			
			return false;
		}
		
		
		if(!isInteger(product.getStock())) {
			setErrorMessage("Solo se admiten números enteros en el stock");
			return false;
		}
		
		System.out.println(selectedBrand);
		
		if(selectedBrand.isEmpty()) {
			setErrorMessage("Debes seleccionar una marca");
			return false;
		}
		
		if(selectedCategory.isEmpty()) {
			setErrorMessage("Debes seleccionar una categoría");
			return false;
		}
		
		setErrorMessage(null);
		return true;
	}
	
	public boolean isDouble(String str) {
		boolean converted = false;
		try {
		    Double.parseDouble(str);
		    converted = true;
		} catch (NumberFormatException e) {
		    converted = false;
		}
		
		return converted;
	}
	
	public boolean isInteger(String str) {
		boolean converted = false;
		try {
		    Integer.parseInt(str);
		    converted = true;
		} catch (NumberFormatException e) {
		    converted = false;
		}
		
		return converted;
	}
	
	public String reloaded() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	        ec.redirect(ec.getRequestContextPath() + ec.getRequestServletPath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
}
