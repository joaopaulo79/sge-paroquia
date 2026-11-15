package com.paroquiaTeam.sgeParoquia.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.core.AppConfig;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoPorHora;

public class PrecificacaoPorHoraDAO {

	public boolean exists() {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "SELECT COUNT(p) FROM PrecificacaoPorHora p WHERE p.id = 1";
			Long quantidade = sessao.createQuery(query, Long.class).uniqueResult();
			return quantidade != null && quantidade > 0;
		}

	}

	public Optional<PrecificacaoPorHora> get() {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			String query = "FROM PrecificacaoPorHora p WHERE p.id = 1";
			return sessao.createSelectionQuery(query, PrecificacaoPorHora.class)
						.uniqueResultOptional();

		}
	}

	public void saveOrUpdate(PrecificacaoPorHora precificacao) {
		boolean existe = exists();
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				if (existe) {
					sessao.merge(precificacao);
				} else {
					sessao.persist(precificacao);
				}
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}

	public void save(PrecificacaoPorHora precificacao) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
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

	public void update(PrecificacaoPorHora precificacao) {
		try (Session sessao = AppConfig.getSessionFactory().openSession()){
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
