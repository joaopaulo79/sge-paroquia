package com.paroquiaTeam.sgeParoquia.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class DashboardController {
	@FXML StackPane containerAcaoCaixa;
	
	private CaixaController controllerCaixa = new CaixaController();
	
	boolean caixaAberto = false;
	
	@FXML
	private void initialize() {
		controllerCaixa.setDashboardController(this);
		carregarPainel();
	}
	
	public void carregarPainel() {
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
