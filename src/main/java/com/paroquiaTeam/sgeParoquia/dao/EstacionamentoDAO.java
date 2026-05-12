package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Estacionamento;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;

public class EstacionamentoDAO {
	
	public boolean exists() { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(e) FROM Estacionamento e WHERE e.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Estacionamento> get() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Estacionamento e WHERE e.id = 1";
			return sessao.createSelectionQuery(query, Estacionamento.class).uniqueResultOptional();
					
		}
	}
	
	public void save(Estacionamento estacionamento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(estacionamento);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void update(Estacionamento estacionamento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(estacionamento);
				t.commit();
			} catch (Exception e) {
				System.out.println("Atualização falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void updatePrecificacao(TipoPrecificacao precificacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Estacionamento estacionamento = sessao.get(Estacionamento.class, (long) 1);
				estacionamento.setPrecificacao(precificacao);
				sessao.merge(estacionamento);
				t.commit();
			} catch (Exception e) {
				System.out.println("Atualização falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
