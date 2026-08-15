package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;

public class ClienteDAO {
	
	public boolean existsById(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(c) FROM Cliente c WHERE c.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public boolean existsByCpf(String cpf) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(c) FROM Cliente c WHERE c.cpf = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, cpf)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Cliente> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Cliente c WHERE c.id = :id";
			return sessao.createQuery(query, Cliente.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Cliente> getByCpf(String cpf) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Cliente c WHERE c.cpf = :cpf";
			return sessao.createQuery(query, Cliente.class)
						.setParameter("cpf", cpf)
						.uniqueResultOptional();
					
		}
	}
	
	
	public List<Cliente> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT c FROM Cliente c";
			return sessao.createQuery(query, Cliente.class)
						.getResultList();
					
		}
	}
	
	public void save(Cliente cliente) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(cliente);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public void update(Cliente cliente) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(cliente);
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
				Cliente cliente = sessao.get(Cliente.class, id);
				if (cliente == null) {					
					throw new IllegalArgumentException("Cliente com id especificado não encontrado");
				}
				sessao.remove(cliente);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
