package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class DashboardController {
	@FXML StackPane containerAcaoCaixa;
	
	@FXML TextField campoPlaca;
	@FXML Button btnEntradaVeiculo;
	@FXML Button btnSaidaVeiculo;
	
	private CaixaController controllerCaixa = new CaixaController();
		
	@FXML
	private void initialize() {
		controllerCaixa.setDashboardController(this);
		
		boolean caixaAberto = SessaoSistema.getInstancia().isCaixaAberto();
		
		btnEntradaVeiculo.setOnAction(e -> {
			handleEstadia(EstadiaController.TipoOperacao.ENTRADA);
		});
		
		btnSaidaVeiculo.setOnAction(e -> {
			handleEstadia(EstadiaController.TipoOperacao.SAIDA);
		});
		
		carregarPainel(caixaAberto);
	}
	
	private void handleEstadia(EstadiaController.TipoOperacao operacao) {
		String placa = campoPlaca.getText();
		new EstadiaController().inicializar(operacao, placa);
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

	        campoPlaca.setDisable(!caixaAberto);
	        btnEntradaVeiculo.setDisable(!caixaAberto);
	        btnSaidaVeiculo.setDisable(!caixaAberto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
