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
	public boolean modificar(Usuario usuario) {
		return false;
	}

	@Override
	public List<Usuario> obtenerTodos() {
		// TODO Auto-generated method stub
		List<Usuario> usuarios = new ArrayList<Usuario>();
		return usuarios;
	}
	
	@Override
	public boolean guardar(Usuario usuario) {
		boolean guardado = false;
		try {
			entity.getTransaction().begin();
			entity.persist(usuario);
			entity.getTransaction().commit();	
			guardado = true;
			
		} catch(Exception e) {
			System.out.println(e);
			guardado = false;
		}
		
		return guardado;
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
