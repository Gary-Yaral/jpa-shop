package com.web.dao;

import com.web.model.Usuario;

import java.util.List;

public interface UsuarioDao {
	public boolean guardar(Usuario usuario);
	public boolean modificar(Usuario usuario);
	public Usuario acceder(String email, String password);
	public List<Usuario> obtenerTodos();
}
