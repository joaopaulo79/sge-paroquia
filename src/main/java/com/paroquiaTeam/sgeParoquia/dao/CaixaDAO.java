package com.paroquiaTeam.sgeParoquia.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.core.AppConfig;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;

public class CaixaDAO {
	public boolean exists(long id) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT COUNT(c) FROM Caixa c WHERE c.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();
			return quantidade != null && quantidade > 0;
		}
	}

	public Optional<Caixa> getById(Long id) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Caixa c WHERE c.id = :id";
			return sessao.createQuery(query, Caixa.class)
						.setParameter("id", id)
						.uniqueResultOptional();

		}
	}
	
	public Optional<Caixa> getByIdWithMovimentosAndOperador(Long id) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Caixa c "
					+ "LEFT JOIN FETCH c.movimentos "
					+ "LEFT JOIN FETCH c.operador "
					+ "WHERE c.id = :id";
			return sessao.createQuery(query, Caixa.class)
						.setParameter("id", id)
						.uniqueResultOptional();

		}
	}
	
	public List<Caixa> getByPeriodWithMovimentosAndOperador(LocalDateTime inicio, LocalDateTime fim) {
	    try (Session session = AppConfig.getSessionFactory().openSession()) {
	        String hql = "SELECT DISTINCT c FROM Caixa c " +
	                     "LEFT JOIN FETCH c.movimentos " +
	                     "LEFT JOIN FETCH c.operador " +
	                     "WHERE c.dataHoraAbertura BETWEEN :inicio AND :fim";
	                     
	        return session.createQuery(hql, Caixa.class)
	                      .setParameter("inicio", inicio)
	                      .setParameter("fim", fim)
	                      .list();
	    }
	}

	public List<Caixa> getAll() {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT c FROM Caixa c";
			return sessao.createQuery(query, Caixa.class)
						.getResultList();

		}
	}

	public Optional<Caixa> getLast() {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Caixa c ORDER BY c.id DESC";
			return sessao.createQuery(query, Caixa.class)
					.setMaxResults(1)
					.uniqueResultOptional();

		}
	}

	public Optional<Caixa> getLastWithOperador() {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT c FROM Caixa c LEFT JOIN FETCH c.operador ORDER BY c.id DESC";
			return sessao.createQuery(query, Caixa.class)
					.setMaxResults(1)
					.uniqueResultOptional();

		}
	}

	public void save(Caixa caixa) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(caixa);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}

	public void update(Caixa caixa) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(caixa);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}

	public void delete(Long id) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Caixa caixa = sessao.get(Caixa.class, id);
				if (caixa == null) {
					throw new IllegalArgumentException("Caixa com id especificado não encontrado");
				}
				sessao.remove(caixa);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
