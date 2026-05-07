package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.PrecionamentoFracionado;

public class PrecionamentoFracionadoDAO {
	
	public boolean exists() { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(p) FROM PrecionamentoFracionado p WHERE p.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
		
	}
	
	public Optional<PrecionamentoFracionado> get() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM PrecionamentoFracionado p WHERE p.id = 1";
			return sessao.createSelectionQuery(query, PrecionamentoFracionado.class)
						.uniqueResultOptional();
					
		}
	}
	
	public void save(PrecionamentoFracionado precionamento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(precionamento);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void update(PrecionamentoFracionado precionamento) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(precionamento);
				t.commit();
			} catch (Exception e) {
				System.out.println("Atualização falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
