package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoPorHora;

public class PrecificacaoPorHoraDAO {
	
	public boolean exists() { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(p) FROM PrecificacaoPorHora p WHERE p.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
		
	}
	
	public Optional<PrecificacaoPorHora> get() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM PrecificacaoPorHora WHERE p.id = 1";
			return sessao.createSelectionQuery(query, PrecificacaoPorHora.class)
						.uniqueResultOptional();
					
		}
	}
	
	public void save(PrecificacaoPorHora precificacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(precificacao);
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
	
	public void update(PrecificacaoPorHora precificacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(precificacao);
				t.commit();
			} catch (Exception e) {
				System.out.println("Atualização falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
