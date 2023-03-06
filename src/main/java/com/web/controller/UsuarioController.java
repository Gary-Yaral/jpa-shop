package com.web.controller;

import java.io.IOException;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.web.dao.UsuarioDao;
import com.web.idao.UsuarioDaoImp;
import com.web.model.Usuario;

@ManagedBean(name = "usuarioController")
@RequestScoped
public class UsuarioController {
	private Usuario usuario = new Usuario();
	private static String view = "";
	private String accessError = "";
	
	public String getAccessError() {
		return accessError;
	}
	public void setAccessError(String accessError) {
		this.accessError = accessError;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		UsuarioController.view = view;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void userExists() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    if (session != null && session.getAttribute("usuario") != null) {
	    	System.out.println("nos vamos al dashboard");
	    	context.getExternalContext().redirect("dashboard.jsf");
	    } 
	} 
	
	public void userNotExists() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    if (session == null || session.getAttribute("usuario") == null) {
	    	System.out.println("nos vamos al index");
	    	context.getExternalContext().redirect("index.jsf");
	    } 
	} 
	
	public String nuevo() {
		Usuario u = new Usuario();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("usuario", u);
		return "nuevo";
	} 
	
	public String guardar(Usuario usuario) {
		UsuarioDao usuarioDAO = new UsuarioDaoImp();
		usuario.setRol("2");
		usuarioDAO.guardar(usuario);
		return "index";
	}
	
	public void acceder() {
		UsuarioDao usuarioDAO = new UsuarioDaoImp();
		Usuario searched;
		searched = usuarioDAO.acceder(this.usuario.getEmail(), this.usuario.getPassword());
		
		if(searched != null) {
			FacesContext context = FacesContext.getCurrentInstance();
		    ExternalContext externalContext = context.getExternalContext();
		    Map<String, Object> sessionMap = externalContext.getSessionMap();
		    sessionMap.put("usuario", searched);
		    HttpSession session = (HttpSession) externalContext.getSession(false);
		    session.setAttribute("usuario_s", searched);
		    this.setAccessError("");
		    try {
	            context.getExternalContext().redirect("dashboard.jsf");
		    } catch (IOException e) {
		       e.printStackTrace();
		    }
		} else {
			this.setAccessError("error"); 
		}
	}
	
	public String cerrarSesion() throws IOException {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("index.jsf");
	    return "index.jsf";
	}

}
