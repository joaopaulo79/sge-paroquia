package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;


public class EstadiaDAO {
	
	public boolean exists(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(e) FROM Estadia e WHERE e.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Estadia> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT e FROM Estadia e WHERE e.id = :id";
			return sessao.createQuery(query, Estadia.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Estadia> getLastByPlaca(String placa) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT e FROM Estadia e WHERE e.placaVeiculo = :placa ORDER BY e.id DESC";
			return sessao.createQuery(query, Estadia.class)
						.setParameter("placa", placa)
						.setMaxResults(1)
						.uniqueResultOptional();
					
		}
	}
	
	
	public List<Estadia> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT e FROM Estadia e";
			return sessao.createQuery(query, Estadia.class)
						.getResultList();
					
		}
	}
	
	public void save(Estadia estadia) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(estadia);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public void update(Estadia estadia) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(estadia);
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
				Estadia estadia = sessao.get(Estadia.class, id);
				if (estadia == null) {					
					throw new IllegalArgumentException("Estadia com id especificado não encontrado");
				}
				sessao.remove(estadia);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
