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
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
            	session.createNativeMutationQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
                
            	session.createNativeMutationQuery("TRUNCATE TABLE Cliente");
            	session.createNativeMutationQuery("TRUNCATE TABLE Convenio");
            	session.createNativeMutationQuery("TRUNCATE TABLE Estacionamento");
            	session.createNativeMutationQuery("TRUNCATE TABLE Estadia");
            	session.createNativeMutationQuery("TRUNCATE TABLE MovimentoCaixa");
            	session.createNativeMutationQuery("TRUNCATE TABLE PrecificacaoFracionada");
            	session.createNativeMutationQuery("TRUNCATE TABLE PrecificacaoPorHora");
            	session.createNativeMutationQuery("TRUNCATE TABLE Usuario");
            	session.createNativeMutationQuery("TRUNCATE TABLE Vaga");

            	session.createNativeMutationQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
		HibernateUtil.shutdown();
		sessaoSistema.encerrarSessao();
	}
}
