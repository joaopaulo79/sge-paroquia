package com.paroquiaTeam.sgeParoquia.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

    static {
        sessionFactory = buildSessionFactory("hibernate.cfg.xml");
    }
    
    public static SessionFactory buildSessionFactory(String config) {
    	try {
    		return new Configuration()
                    .configure(config)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Hibernate init failed: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void setSessionFactory(SessionFactory sf) {
        if (sessionFactory != null && sessionFactory.isOpen()) {
        	sessionFactory.close();
        }
        sessionFactory = sf;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
