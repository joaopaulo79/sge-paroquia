package com.paroquiaTeam.sgeParoquia.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AppConfig {
	private static SessionFactory sessionFactory;
    private static Properties props = new Properties();

    static {
        carregarConfiguracoes();
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory("hibernate.cfg.xml");
        }
    }
    
    public static void carregarConfiguracoes(String caminhoArquivo) {
        File arquivoConfig = new File(caminhoArquivo);
        if (arquivoConfig.exists()) {
            try (FileInputStream fis = new FileInputStream(arquivoConfig)) {
                props.clear();
                props.load(fis);
                System.out.println("Configurações carregadas de: " + caminhoArquivo);
            } catch (IOException e) {
                System.err.println("Erro ao ler " + caminhoArquivo + ": " + e.getMessage());
            }
        }
    }

    private static void carregarConfiguracoes() {
        File arquivoConfig = new File("config.properties");
        if (arquivoConfig.exists()) {
            try (FileInputStream fis = new FileInputStream(arquivoConfig)) {
                props.load(fis);
            } catch (IOException e) {
                System.err.println("Erro ao ler config.properties: " + e.getMessage());
            }
        }
    }

    public static SessionFactory buildSessionFactory(String config) {
        try {
            Configuration configuration = new Configuration().configure(config);

            String host = props.getProperty("db.host");
            if (host != null && !host.isEmpty()) {
                String port = props.getProperty("db.port", "3306");
                String dbName = props.getProperty("db.name", "sgeParoquia");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");
                
                String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName 
                           + "?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";

                configuration.setProperty("hibernate.connection.url", url);
                configuration.setProperty("hibernate.connection.username", user);
                configuration.setProperty("hibernate.connection.password", pass);
            }

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Erro ao iniciar o Hibernate: " + e);
        }
    }

    public static void setSessionFactory(SessionFactory sf) {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
        sessionFactory = sf;
    }
    
    // Método genérico para buscar qualquer propriedade (Banco, Impressora, etc.)
    public static String get(String chave, String valorPadrao) {
        return props.getProperty(chave, valorPadrao);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
