package com.paroquiaTeam.sgeParoquia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX iniciou");
        
        try {
			Parent parent = FXMLLoader.load(getClass().getResource("/fxml/TesteView.fxml"));
			Scene scene = new Scene(parent);
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