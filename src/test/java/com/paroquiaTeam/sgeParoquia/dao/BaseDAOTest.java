package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;
import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

class BaseDAOTest {
	protected static SessionFactory testSessionFactory;

	@BeforeAll
	public static void init() {
		testSessionFactory = HibernateUtil.buildSessionFactory("hibernate-test.cfg.xml");
		HibernateUtil.setSessionFactory(testSessionFactory);
		
		String senha = SenhaUtil.hash("senha");
		Usuario userLogadoTest = new Usuario("Logado", "logado", senha , true, TipoUsuario.ADMINISTRADOR);
		Caixa caixaAtualTest = new Caixa(LocalDateTime.now(), null, 0, userLogadoTest);
		
		try (Session session = testSessionFactory.openSession()) {
	        Transaction tx = session.beginTransaction();
	        session.persist(userLogadoTest);
	        session.persist(caixaAtualTest);
	        tx.commit();
	    }
		
		SessaoSistema sessao = SessaoSistema.getInstancia();
		sessao.setUserLogado(userLogadoTest);
		sessao.setCaixa(caixaAtualTest);
	}
	
	@AfterAll
	public static void limpar() {
		SessaoSistema sessaoSistema = SessaoSistema.getInstancia();
		try (Session session = testSessionFactory.openSession()) {
	        Transaction tx = session.beginTransaction();
	        session.remove(sessaoSistema.getCaixa());
	        session.remove(sessaoSistema.getUserLogado());
	        tx.commit();
	    }
		
		HibernateUtil.shutdown();
		sessaoSistema.encerrarSessao();
	}
}
