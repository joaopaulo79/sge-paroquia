package com.paroquiaTeam.sgeParoquia.dao;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.paroquiaTeam.sgeParoquia.TestSetup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;

public class BaseDAOTest extends TestSetup {
	@BeforeAll
	public static void init() {
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
