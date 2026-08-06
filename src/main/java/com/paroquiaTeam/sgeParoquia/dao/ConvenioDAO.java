package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Convenio;

public class ConvenioDAO {
	
	public boolean existsById(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(c) FROM Convenio c WHERE c.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public boolean existsByCnpj(String cnpj) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(c) FROM Convenio c WHERE c.cnpj = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, cnpj)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Convenio> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Convenio c WHERE c.id = :id";
			return sessao.createQuery(query, Convenio.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Convenio> getByCnpj(String cnpj) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Convenio c WHERE c.cnpj = :cnpj";
			return sessao.createQuery(query, Convenio.class)
						.setParameter("cnpj", cnpj)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Convenio> getByNome(String nome) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Convenio c WHERE c.nome = :nome";
			return sessao.createQuery(query, Convenio.class)
						.setParameter("nome", nome)
						.uniqueResultOptional();
					
		}
	}
	
	public List<Convenio> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT c FROM Convenio c";
			return sessao.createQuery(query, Convenio.class)
						.getResultList();
					
		}
	}
	
	public void save(Convenio convenio) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(convenio);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public void update(Convenio convenio) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(convenio);
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
				Convenio convenio = sessao.get(Convenio.class, id);
				if (convenio == null) {					
					throw new IllegalArgumentException("Convênio com id especificado não encontrado");
				}
				sessao.remove(convenio);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
