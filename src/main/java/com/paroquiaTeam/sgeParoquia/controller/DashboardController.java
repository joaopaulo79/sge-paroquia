package com.paroquiaTeam.sgeParoquia.controller;

import java.io.IOException;

import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController {
	@FXML StackPane containerAcaoCaixa;
	
	@FXML Label labelPlaca;
	@FXML TextField campoPlaca;
	@FXML Button btnMovimentoVeiculo;
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
		
		
		
		btnMovimentoVeiculo.setOnAction(e -> {
			System.out.println("APERTOUUUUUUUUUUUUUU");
		});
		
		carregarPainel(caixaAberto);
	}
	
	private void handleEstadia(EstadiaController.TipoOperacao operacao) {
		String placa = campoPlaca.getText();
		
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/estadia/estadia.fxml"));
			Parent root = loader.load();
			
			EstadiaController estadiaController = loader.getController();
			
			if (estadiaController.configurarEValidar(operacao, placa)) {
				Stage modal = new Stage();
		        modal.initModality(Modality.APPLICATION_MODAL);
		        modal.setScene(new Scene(root));
		        modal.setTitle("Registro de Veículo");
		        modal.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Erro");
		    	alert.setHeaderText("Placa informada não possuí entrada!");
		    	alert.showAndWait();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	
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

	        labelPlaca.setDisable(!caixaAberto);
	        campoPlaca.setDisable(!caixaAberto);
	        btnMovimentoVeiculo.setDisable(!caixaAberto);
	        btnEntradaVeiculo.setDisable(!caixaAberto);
	        btnSaidaVeiculo.setDisable(!caixaAberto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
