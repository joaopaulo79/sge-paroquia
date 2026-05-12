package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Vaga;

public class VagaDAO {
	public boolean exists(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Vaga> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.id = ?1";
			return sessao.createSelectionQuery(query, Vaga.class)
						.setParameter(1, id)
						.uniqueResultOptional();
					
		}
	}
	
	public List<Vaga> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v";
			return sessao.createSelectionQuery(query, Vaga.class)
						.getResultList();
					
		}
	}
	
	public void save(Vaga vaga) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(vaga);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void update(Vaga vaga) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(vaga);
				t.commit();
			} catch (Exception e) {
				System.out.println("Atualização falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Vaga vaga = sessao.get(Vaga.class, id);
				if (vaga != null) {					
					sessao.remove(vaga);
				}
				t.commit();
			} catch (Exception e) {
				System.out.println("Remoção falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
