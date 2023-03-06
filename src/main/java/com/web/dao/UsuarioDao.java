package com.web.dao;

import com.web.model.Usuario;

import java.util.List;

public interface UsuarioDao {
	public void guardar(Usuario usuario);
	public void crear(Usuario usuario);
	public Usuario acceder(String email, String password);
	public List<Usuario> obtenerTodos();
}
