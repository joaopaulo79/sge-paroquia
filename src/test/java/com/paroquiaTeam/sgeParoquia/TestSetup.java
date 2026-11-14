package com.paroquiaTeam.sgeParoquia;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;

import com.paroquiaTeam.sgeParoquia.database.AppConfig;

public class TestSetup {
	protected static SessionFactory testSessionFactory;

	@BeforeAll
	public static void init() {
		AppConfig.carregarConfiguracoes("config-test.properties");
		testSessionFactory = AppConfig.buildSessionFactory("hibernate-test.cfg.xml");
		AppConfig.setSessionFactory(testSessionFactory);
	}

}
