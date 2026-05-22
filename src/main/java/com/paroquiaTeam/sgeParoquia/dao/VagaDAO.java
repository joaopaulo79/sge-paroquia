package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;
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
	
	public Vaga getFirstLivre(TipoVaga tipo, TipoReservaVaga reserva) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2";
			return sessao.createSelectionQuery(query, Vaga.class)
						.setParameter(1, tipo)
						.setParameter(2, reserva)
						.getSingleResultOrNull();			
		}
	}
	
	public void save(Vaga vaga) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(vaga);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
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
				t.rollback();
				throw e;
			}
		}
	}
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Vaga vaga = sessao.get(Vaga.class, id);
				if (vaga == null) {					
					throw new IllegalArgumentException("Vaga com id especificado não encontrada");
				}
				sessao.remove(vaga);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;			
			}
		}
	}
	
	public long countAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v";
			Long quantidade = sessao.createQuery(query, Long.class)
					.uniqueResult();			
			return quantidade;
		}
	}
	
	public long count(TipoVaga tipoVaga, TipoReservaVaga tipoReserva) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, tipoVaga)
					.setParameter(2, tipoReserva)
					.uniqueResult();			
			return quantidade;
		}
	}
	
	public long countComStatus(TipoVaga tipoVaga, TipoReservaVaga tipoReserva, boolean ocupacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, tipoVaga)
					.setParameter(2, tipoReserva)
					.uniqueResult();			
			return quantidade;
		}
	}
}
