package com.paroquiaTeam.sgeParoquia;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;

public class TestSetup {
	protected static SessionFactory testSessionFactory;

	@BeforeAll
	public static void init() {
		testSessionFactory = HibernateUtil.buildSessionFactory("hibernate-test.cfg.xml");
		HibernateUtil.setSessionFactory(testSessionFactory);
	}

}
