package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;

public class UsuarioDAO {
	
	public boolean exists(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(u) FROM Usuario u WHERE u.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Usuario> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.caixas WHERE u.id = :id";
			return sessao.createQuery(query, Usuario.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Usuario> getByLogin(String login) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.caixas WHERE u.login = :login";
			return sessao.createQuery(query, Usuario.class)
						.setParameter("login", login)
						.uniqueResultOptional();
					
		}
	}
	
	
	public List<Usuario> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT u FROM Usuario u LEFT JOIN FETCH u.caixas";
			return sessao.createQuery(query, Usuario.class)
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
				t.rollback();
				throw e;
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
				t.rollback();
				throw e;
			}
		}
	}
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Usuario usuario = sessao.get(Usuario.class, id);
				if (usuario == null) {					
					throw new IllegalArgumentException("Usuário com id especificado não encontrado");
				}
				sessao.remove(usuario);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public boolean autenticar(String login, String senha) {
		Optional<Usuario> talvezUsuario = getByLogin(login);
		if (talvezUsuario.isEmpty()) {
			return false;
		}
		return SenhaUtil.verificar(senha, talvezUsuario.get().getSenha());
	}
}
