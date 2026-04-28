package com.paroquiaTeam.sgeParoquia;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX iniciou");

        // Teste
        try (var sf = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addPackage("com.paroquiaTeam.sgeParoquia.model")
                .buildSessionFactory();
             var session = sf.openSession()) {
            System.out.println("Hibernate conectou");
       
            TestesDeEntidades.testaEntidades(session);
        } catch (Exception e) {
            System.out.println("Hibernate falhou: " + e.getMessage());
        }
                
        try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.setTitle("SGE Paróquia");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
    	launch(args);
    }
}