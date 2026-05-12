package com.paroquiaTeam.sgeParoquia;

import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.Estacionamento;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.Usuario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX iniciou");
        
        System.out.println("\nInicializando valores cruciais...");
        initialize();
        System.out.println("Sucesso!");
        
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
    
    
    private void initialize() {
    	
    	
    	EstacionamentoDAO estacionamentoDao = new EstacionamentoDAO();    	
    	if (!estacionamentoDao.exists()) {
    		Estacionamento estacionamento = new Estacionamento(0, TipoPrecificacao.FRACIONADA);
    		estacionamentoDao.save(estacionamento);
    	}
    	
    	UsuarioDAO usuarioDao = new UsuarioDAO();
    	if (!usuarioDao.exists(1)) {
    		Usuario usuario = new Usuario(
    				"root", "root", "root4paroquia", true,  TipoUsuario.ADMINISTRADOR);
    		usuarioDao.save(usuario);
    	}
    	
    	
    }

    public static void main(String[] args) {
    	launch(args);
    }
}