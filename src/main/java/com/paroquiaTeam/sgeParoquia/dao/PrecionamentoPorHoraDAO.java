package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.PrecionamentoPorHora;

public class PrecionamentoPorHoraDAO {
	
	public boolean exists() { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(p) FROM PrecionamentoPorHora p WHERE p.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
		
	}
	
	public Optional<PrecionamentoPorHora> get() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM PrecionamentoPorHora WHERE p.id = 1";
			return sessao.createSelectionQuery(query, PrecionamentoPorHora.class)
						.uniqueResultOptional();
					
		}
	}
	
	public void save(PrecionamentoPorHora precionamento) {
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
	
	public void update(PrecionamentoPorHora precionamento) {
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
