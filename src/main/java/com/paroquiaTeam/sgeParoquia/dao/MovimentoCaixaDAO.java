package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.TipoPagamento;


public class MovimentoCaixaDAO {
	
	public boolean exists(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(m) FROM MovimentoCaixa m WHERE m.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<MovimentoCaixa> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT m FROM MovimentoCaixa m WHERE m.id = :id";
			return sessao.createQuery(query, MovimentoCaixa.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public List<MovimentoCaixa> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT m FROM MovimentoCaixa m";
			return sessao.createQuery(query, MovimentoCaixa.class)
						.getResultList();
					
		}
	}
	
	public void save(MovimentoCaixa movimento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(movimento);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public List<MovimentoCaixa> getByCaixa(Long idCaixa) {
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        String query = "SELECT m FROM MovimentoCaixa m WHERE m.caixa.id = :idCaixa";
	        return sessao.createQuery(query, MovimentoCaixa.class)
	                    .setParameter("idCaixa", idCaixa)
	                    .getResultList();
	    }
	}
	
	public List<MovimentoCaixa> getByEstadia(Long idEstadia) {
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        String query = "SELECT m FROM MovimentoCaixa m WHERE m.estadia.id = :idEstadia";
	        return sessao.createQuery(query, MovimentoCaixa.class)
	                    .setParameter("idEstadia", idEstadia)
	                    .getResultList();
	    }
	}
	
	public Double getTotalPorCaixaEPagamento(Long idCaixa, TipoPagamento tipoPagamento) {
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        String query = "SELECT SUM(m.valor) " +
	                       "FROM MovimentoCaixa m " +
	                       "WHERE m.caixa.id = :idCaixa " +
	                       "AND m.formaPagamento = :tipoPagamento";
	                       
	        Double total = sessao.createQuery(query, Double.class)
	                    .setParameter("idCaixa", idCaixa)
	                    .setParameter("tipoPagamento", tipoPagamento)
	                    .uniqueResult();
	                    
	        // Retorna o total ou 0.0 caso não exista nenhum movimento com esse filtro
	        return total != null ? total : 0.0;
	    }
	}
	
	public void update(MovimentoCaixa movimento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(movimento);
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
				MovimentoCaixa movimento = sessao.get(MovimentoCaixa.class, id);
				if (movimento == null) {					
					throw new IllegalArgumentException("Movimento de caixa com id especificado não encontrado");
				}
				sessao.remove(movimento);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
