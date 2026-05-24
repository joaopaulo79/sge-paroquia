package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class DashboardController {
	@FXML StackPane containerAcaoCaixa;
	
	
	private CaixaController controllerCaixa = new CaixaController();
		
	@FXML
	private void initialize() {
		controllerCaixa.setDashboardController(this);
		
		boolean caixaAberto = SessaoSistema.getInstancia().isCaixaAberto();
		
		carregarPainel(caixaAberto);
	}
	
	public void carregarPainel(boolean caixaAberto) {
		try {
	        containerAcaoCaixa.getChildren().clear();
	        String fxmlPath = caixaAberto ? 
	        		"/screens/dashboard/acoes-caixa-aberto.fxml" : 
	        		"/screens/dashboard/acoes-caixa-fechado.fxml";

	        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
	        
	        loader.setController(controllerCaixa);
	        
	        Node painel = loader.load();	        
	        containerAcaoCaixa.getChildren().add(painel);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
