package com.paroquiaTeam.sgeParoquia;

import com.paroquiaTeam.sgeParoquia.controller.BaseController;
import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.Estacionamento;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;

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
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/shared/layoutBase/layoutBase.fxml"));
			Parent root = loader.load();
			
			BaseController controller = loader.getController();
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			controller.mostrarTela("login", "/screens/login/login.fxml");
			
			primaryStage.setTitle("SGE Paróquia");
			primaryStage.setMaximized(true);
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
    		String senha = SenhaUtil.hash("root");
    		Usuario usuario = new Usuario(
    				"root", "root", senha, true,  TipoUsuario.ADMINISTRADOR);
    		usuarioDao.save(usuario);
    	}
    	
    	
    }

    public static void main(String[] args) {
    	launch(args);
    }
}