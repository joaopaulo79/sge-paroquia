package com.paroquiaTeam.sgeParoquia;

import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroPrecificacaoInicialController;
import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroUsuarioBaseController;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Estacionamento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX iniciou");

        System.out.println("\nInicializando valores cruciais...");
        
        if (!initialize()) {
        	NavegacaoManager.getInstancia().abrirAlertaErro("Valores iniciais não foram inicializados. Encerrando...");
        	System.out.println("Falha ao inicializar valores cruciais");
        } else {
        	System.out.println("Sucesso!");

            try {
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("/shared/layoutBase/layoutBase.fxml"));
    			Parent root = loader.load();

    			Scene scene = new Scene(root);
    			primaryStage.setScene(scene);

    			primaryStage.setTitle("SGE Paróquia");
    			primaryStage.setMaximized(true);
    			primaryStage.show();
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
        }
    }


    private boolean initialize() {
    	boolean sucesso = true;
    	
    	UsuarioDAO usuarioDao = new UsuarioDAO();
    	if (!usuarioDao.existsById(1)) {
    		NavegacaoManager.getInstancia().abrirAlerta("Alerta", 
    				"Não há nenhum usuário cadastrado.\nIniciando cadastro de usuário base", 
    				AlertType.INFORMATION);
    		
    		CadastroUsuarioBaseController cUBC = NavegacaoManager
    				.getInstancia().abrirModal(Popup.CADASTRARUSUARIOBASE);
    		
    		if (!cUBC.getCadastrou()) sucesso = false;
    	}
    	
    	EstacionamentoDAO estacionamentoDao = new EstacionamentoDAO();
    	if (!estacionamentoDao.exists()) {
    		Estacionamento estacionamento = new Estacionamento(0, TipoPrecificacao.FRACIONADA);
    		estacionamentoDao.save(estacionamento);
    	}
    	
    	PrecificacaoService precServ = new PrecificacaoService();
    	if (precServ.buscarPrecificacaoFracionada().isEmpty()) {
    		NavegacaoManager.getInstancia().abrirAlerta("Alerta", 
    				"Não há nenhuma precificação cadastrada.\nIniciando cadastro de precificação inicial", 
    				AlertType.INFORMATION);
    		
    		CadastroPrecificacaoInicialController cPIC = NavegacaoManager
    				.getInstancia().abrirModal(Popup.CADASTRARPRECIFICACAOINICIAL);
    		
    		if (!cPIC.getCadastrou()) sucesso = false;
    	}
    	
    	return sucesso;
    }

    public static void main(String[] args) {
    	launch(args);
    }
}