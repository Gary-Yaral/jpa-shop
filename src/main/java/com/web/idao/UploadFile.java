package com.web.idao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

public class UploadFile {
	
	private String fileName;

	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public boolean saveFile(Part selectedImage) {
		HashMap<String, String> fileTypes = new HashMap<String, String>();
		fileTypes.put("jpg", "jpg");
		fileTypes.put("jpeg", "jpeg");
		fileTypes.put("png", "png");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
		String absolutePath = servletContext.getRealPath("/");
		String path = absolutePath + "/resources/img/";

		System.out.println(absolutePath);
		
		ProductDaoImp productDAO = new ProductDaoImp();
		String lastIndex = productDAO.getNextIndex();
		String fileName = "";
		 boolean wasInserted = false; 
		
		  try {		  
			  fileName = extractFileName(selectedImage);
			  String[] fullName = fileName.split("\\.");
			  String extension = fullName[fullName.length-1];
			  if(fileTypes.get(extension) != null) {
				  String name = "Image"+lastIndex+".jpg";
				  setFileName(name);
				  selectedImage.write(path+name);
				  wasInserted = true;				  
			  } else {
				  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de formato", "El formato de los datos es incorrecto."));
			  }              
	        } catch (IOException ex) {
	            System.out.println(ex);
	            wasInserted = false;
	            
	        }
		  
		  return wasInserted;
		  
	}
	
	public boolean updateFile(Part selectedImage, String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
		String absolutePath = servletContext.getRealPath("/");
		String path = absolutePath + "/resources/img/"+name;
		File image = new File(path);
		if(image.delete()) {
			System.out.println("La elimino");
			return saveFileUpdate(selectedImage, name);
		} else {
			System.out.println("No pudo eliminarla");
			return false;
		}
		  
	}
	
	public boolean saveFileUpdate(Part selectedImage, String name) {
	HashMap<String, String> fileTypes = new HashMap<String, String>();
	fileTypes.put("jpg", "jpg");
	fileTypes.put("jpeg", "jpeg");
	fileTypes.put("png", "png");
	FacesContext facesContext = FacesContext.getCurrentInstance();
	ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
	String absolutePath = servletContext.getRealPath("/");
	String path = absolutePath + "/resources/img/";
	boolean wasInserted = false; 
    try {		  
	    String fileName = extractFileName(selectedImage);
	    String[] fullName = fileName.split("\\.");
	    String extension = fullName[fullName.length-1];
	    if(fileTypes.get(extension) != null) {
		  setFileName(name);
		  selectedImage.write(path+name);
		  wasInserted = true;				  
	    }               
    } catch (IOException ex) {
        System.out.println(ex);
        wasInserted = false;            
    }
	  
	return wasInserted;
		  
	}
	
	public static String extractFileName(Part filePart) {
		String fileName = filePart.getSubmittedFileName();
        String regex = "^(.*?)(\\.[^.]*$|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        }
        return fileName;
    }
	
  }
	