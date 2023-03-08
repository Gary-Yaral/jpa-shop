package com.web.controller;

import java.io.IOException;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import com.web.dao.UsuarioDao;
import com.web.idao.EmailValidator;
import com.web.idao.UsuarioDaoImp;
import com.web.model.Usuario;

@ManagedBean(name = "usuarioController")
@RequestScoped
public class UsuarioController {
	private Usuario usuario = new Usuario();
	private EmailValidator validator = new EmailValidator();
	private String passwordCompare;
	private String generatedError;
	private static Usuario usuarioRespaldo = new Usuario();
	private static String view="index";
	private static String errorMessage;
	
	public UsuarioController() {
		String error = UsuarioController.errorMessage;
		setGeneratedError(error);
		
		Usuario usuarioCargado = UsuarioController.usuarioRespaldo;
		setUsuario(usuarioCargado);
	}
	
	public static Usuario getUsuarioRespaldo() {
		return usuarioRespaldo;
	}
	
	public static void setUsuarioRespaldo(Usuario usuarioRespaldo) {
		UsuarioController.usuarioRespaldo = usuarioRespaldo;
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
		UsuarioController.errorMessage = errorMessage;
	}
	
	public String getPasswordCompare() {
		return passwordCompare;
	}
	public void setPasswordCompare(String passwordCompare) {
		this.passwordCompare = passwordCompare;
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
	
	public void userExists(ComponentSystemEvent event) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    if (session != null) {
	    	if(session.getAttribute("usuario") != null) {
	    		context.getExternalContext().redirect("dashboard.jsf");	    		
	    	}
	    	
	    	if(session.getAttribute("cliente") != null) {
	    		context.getExternalContext().redirect("shopping.jsf");	    		
	    	}
	    } 
	} 
	
	public void userNotExists(ComponentSystemEvent event) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    if (session == null || session.getAttribute("usuario") == null) {
	    	context.getExternalContext().redirect("index.jsf");
	    } 
	} 
	
	public void clienteNotExists(ComponentSystemEvent event) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
	    if (session != null) {
	    	if(session.getAttribute("cliente") == null && session.getAttribute("usuario") == null) {
	    		context.getExternalContext().redirect("index.jsf");	    		
	    	}
	    } else {
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
	
	public String acceder() {
		UsuarioDao usuarioDAO = new UsuarioDaoImp();
		Usuario searched = usuarioDAO.acceder(this.usuario.getEmail(), this.usuario.getPassword());
		
		if(searched == null) {
			System.out.println("Error de usuario");
			setErrorMessage("Usuario o contraseña incorrecto"); 
			return reloaded();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		if(searched.getRol().equals("1")) {
			sessionMap.put("usuario", searched);
			setErrorMessage("");
			try {
				setUsuarioRespaldo(new Usuario());
				context.getExternalContext().redirect("dashboard.jsf");
			} catch (IOException e) {
				setErrorMessage("Error de redirección al acceder");
				e.printStackTrace();
				return reloaded();
			}
			return null;
		}
		
		if(searched.getRol().equals("2")) {
			sessionMap.put("cliente", searched);
			setErrorMessage("");
			try {
				setUsuarioRespaldo(new Usuario());
				context.getExternalContext().redirect("shopping.jsf");
			} catch (IOException e) {
				setErrorMessage("Error de redirección al acceder");
				e.printStackTrace();
				return reloaded();
			}
			return null;
		}
		
		return reloaded();
	    
	}
	
	public String cerrarSesion() throws IOException {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("index.jsf");
	    return "index.jsf";
	}
	
	public String eliminarSesionCliente() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		requestMap.remove("cliente");
		context.getExternalContext().invalidateSession();
	    context.getExternalContext().redirect("index.jsf");
	    return "index.jsf";
	}
	
	public void registrarse() throws IOException {
		setUsuarioRespaldo(new Usuario());
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("register.jsf");
	}
	
	public String goToDashboard() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("dashboard.jsf");
	    return "dashboard.jsf";
	}
	
	public String goToPage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("shopping.jsf");
	    return "shopping.jsf";
	}
	
	public String goToLogin() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.getExternalContext().redirect("index.jsf");
	    return null;
	}
	
	public String registerNew() throws IOException {
		setUsuarioRespaldo(usuario);
		setErrorMessage(null);
		if(validateRegister(usuario)) {
			UsuarioDao usuarioDAO = new UsuarioDaoImp();
			if(usuarioDAO.guardar(usuario)) {
				setErrorMessage(null);
				setUsuarioRespaldo(new Usuario());
				return goToLogin();
			} else {
				setErrorMessage("Error al guardar el usuario");
				return reloaded();
			}
		}
		
		return reloaded();
	}
	
	public boolean validateRegister(Usuario usuario) {
		if(usuario.getNombre().isEmpty()) {
			setErrorMessage("Por favor ingresa el nombre");
			return false;
		}
		
		if(usuario.getApellido().isEmpty()) {
			setErrorMessage("Por favor ingresa el apellido");
			return false;
		}
		
		if(usuario.getEmail().isEmpty()) {
			setErrorMessage("Por favor ingresa el email");
			return false;
		}
		
		if(!validator.validate(usuario.getEmail())) {
			setErrorMessage("Por favor ingresa un email valido");
			return false;
		}
		
		if(usuario.getPassword().isEmpty()) {
			setErrorMessage("Por favor ingresa la contraseña");
			return false;
		}
		
		if(passwordCompare.isEmpty()) {
			setErrorMessage("Por favor repite la contraseña");
			return false;
		}
		
		if(!usuario.getPassword().equals(passwordCompare)) {
			setErrorMessage("Las contraseñas no coinciden");
			return false;
		}
		
		usuario.setRol("2");
		setErrorMessage(null);
		return true;
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
