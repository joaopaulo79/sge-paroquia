package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class DashboardController {
	@FXML StackPane containerCaixa;

	@FXML Label labelPlaca;
	@FXML TextField campoPlaca;
	@FXML Button btnMovimentoVeiculo;
	@FXML Button btnEntradaVeiculo;
	@FXML Button btnSaidaVeiculo;
	
	@FXML
	private void initialize() {
		CaixaController caixaController = new CaixaController(containerCaixa);
		
		caixaController.setOnStatusCaixaAlterado(this::toggleControlesEstadia);
		caixaController.carregarPainel();
		
		btnEntradaVeiculo.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirAlertaErro("Botao sendo cortado");
		});

		btnSaidaVeiculo.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirAlertaErro("Botao sendo cortado");
		});



		btnMovimentoVeiculo.setOnAction(e -> {
			try {
				String placa = campoPlaca.getText();
				
				if (placa == null || placa.isBlank()) {
					throw new IllegalArgumentException("Insira uma placa");
				}
				
				new EstadiaController().handleEstadia(placa);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getLocalizedMessage());
				e2.printStackTrace();
			}
		});
	}
	
/*
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
	*/
	
	public void toggleControlesEstadia(boolean caixaAberto) {
		try {
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
