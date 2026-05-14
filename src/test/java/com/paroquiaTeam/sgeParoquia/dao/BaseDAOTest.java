package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;

class BaseDAOTest {
	protected static SessionFactory testSessionFactory;

	@BeforeAll
	public static void init() {
		testSessionFactory = HibernateUtil.buildSessionFactory("hibernate-test.cfg.xml");
		HibernateUtil.setSessionFactory(testSessionFactory);
	}
	
	@AfterAll
	public static void cleanSf() {
		HibernateUtil.shutdown();
	}
}
