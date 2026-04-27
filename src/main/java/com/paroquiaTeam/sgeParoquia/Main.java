package com.paroquiaTeam.sgeParoquia;

import org.hibernate.Transaction;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.paroquiaTeam.sgeParoquia.model.Usuario;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX iniciou");

        // Teste
        try (var sf = new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
             var session = sf.openSession()) {
            System.out.println("Hibernate conectou");
            
            Transaction t = session.beginTransaction();

            Usuario user = new Usuario();
            user.setNome("Teste");
            user.setLogin("Teste");
            user.setSenha("Teste");
            user.setTipo("Teste");
            
            session.persist(user);
            t.commit();
            
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