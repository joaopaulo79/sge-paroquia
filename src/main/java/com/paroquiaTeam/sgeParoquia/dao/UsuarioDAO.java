package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Usuario;

public class UsuarioDAO {
	
	public Optional<Usuario> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Usuario u JOIN FETCH u.caixas WHERE u.id = ?1";
			return sessao.createSelectionQuery(query, Usuario.class)
						.setParameter(1, id)
						.uniqueResultOptional();
					
		}
	}
	
	public List<Usuario> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Usuario u JOIN FETCH u.caixas";
			return sessao.createSelectionQuery(query, Usuario.class)
						.getResultList();
					
		}
	}
	
	public void save(Usuario user) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(user);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void update(Usuario user) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(user);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Usuario usuario = sessao.get(Usuario.class, id);
				if (usuario != null) {					
					sessao.remove(usuario);
				}
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
