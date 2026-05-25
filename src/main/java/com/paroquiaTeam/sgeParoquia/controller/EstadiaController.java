package com.paroquiaTeam.sgeParoquia.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.Estadia;
import com.paroquiaTeam.sgeParoquia.view.factory.EstadiaCardFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstadiaController {
    
    // Enum para deixar o código mais limpo
    public enum TipoOperacao { ENTRADA, SAIDA }
    
    private TipoOperacao operacao;
    private Estadia estadia;
    private EstadiaDAO dao = new EstadiaDAO();
    
    @FXML VBox containerCards;
    @FXML Button btnCancelar;

    public boolean configurarEValidar(TipoOperacao operacao, String placa) {
        this.operacao = operacao;
        
        if (operacao == TipoOperacao.SAIDA && !validarEstadiaAtiva(placa)) {
        	return false;
        }
        
        if (operacao == TipoOperacao.ENTRADA) {
            this.estadia = new Estadia();
            this.estadia.setPlacaVeiculo(placa);
            this.estadia.setDataHoraEntrada(LocalDateTime.now());
        } 
        
    	criarCards();
    	btnCancelar.setOnAction(e -> ((Stage) btnCancelar.getScene().getWindow()).close());
    	return true;
    }
    
    private void criarCards() {
    	containerCards.getChildren().add(
	    	switch (operacao) {
	    		case ENTRADA -> new EstadiaCardFactory().criarCardEntrada(estadia);
	    		case SAIDA -> new EstadiaCardFactory().criarCardSaida(estadia);
	    	}
    	);
    }

    private boolean validarEstadiaAtiva(String placa) {
        Optional<Estadia> busca = dao.getLastByPlaca(placa);
        if (busca.isPresent() && busca.get().getDataHoraSaida() == null) {
            this.estadia = busca.get();
            return true;
        } else {
        	return false;
        }
    }
}
