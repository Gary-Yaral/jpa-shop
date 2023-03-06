package com.web.idao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.web.dao.UsuarioDao;
import com.web.model.JPAUtil;
import com.web.model.Usuario;

public class UsuarioDaoImp implements UsuarioDao{
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	@Override
	public void crear(Usuario usuario) {
		// TODO Auto-generated method stub
		entity.getTransaction().begin();
		entity.persist(usuario);
		entity.getTransaction().commit();
	}

	@Override
	public List<Usuario> obtenerTodos() {
		// TODO Auto-generated method stub
		List<Usuario> usuarios = new ArrayList<Usuario>();
		return usuarios;
	}
	
	@Override
	public void guardar(Usuario usuario) {
		entity.getTransaction().begin();
		entity.persist(usuario);
		entity.getTransaction().commit();
		// JPAUtil.shutdown();
	}
	
	@Override
	public Usuario acceder(String email, String password) {
		TypedQuery<Usuario> query = entity.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.password=:password", Usuario.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<Usuario> usuarios = query.getResultList();
        if (usuarios.isEmpty()) {
            return null;
        } else {
            return usuarios.get(0);
        }
	}
	
}
