package com.paroquiaTeam.sgeParoquia.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.Estadia;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstadiaController {
    
    // Enum para deixar o código mais limpo
    public enum TipoOperacao { ENTRADA, SAIDA }
    
    private TipoOperacao operacao;
    private Estadia estadia;
    private EstadiaDAO dao = new EstadiaDAO();

    public void inicializar(TipoOperacao operacao, String placa) {
        this.operacao = operacao;
        
        if (operacao == TipoOperacao.ENTRADA) {
            this.estadia = new Estadia();
            this.estadia.setPlacaVeiculo(placa);
            this.estadia.setDataHoraEntrada(LocalDateTime.now());
            carregarTela();
        } else {
            buscarEstadiaAtiva(placa);
        }
    }
    
    private void carregarTela() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/estadia/estadia.fxml"));
			Parent root = loader.load();
			
			Stage modal = new Stage();
	        modal.initModality(Modality.APPLICATION_MODAL);
	        modal.setScene(new Scene(root));
	        modal.setTitle("Registro de Veículo");
	        modal.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void buscarEstadiaAtiva(String placa) {
        Optional<Estadia> busca = dao.getLastByPlaca(placa);
        if (busca.isPresent() && busca.get().getDataHoraSaida() == null) {
            this.estadia = busca.get();
            carregarTela();
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Erro");
        	alert.setHeaderText("Placa informada não possuí entrada!");
        	alert.showAndWait();
        }
    }
}
