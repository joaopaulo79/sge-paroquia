package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoFracionada;

public class PrecificacaoFracionadaDAO {
	
	public boolean exists() { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(p) FROM PrecificacaoFracionada p WHERE p.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
		
	}
	
	public Optional<PrecificacaoFracionada> get() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM PrecificacaoFracionada p WHERE p.id = 1";
			return sessao.createSelectionQuery(query, PrecificacaoFracionada.class)
						.uniqueResultOptional();
					
		}
	}
	
	public void save(PrecificacaoFracionada precificacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(precificacao);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public void update(PrecificacaoFracionada precificacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(precificacao);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
