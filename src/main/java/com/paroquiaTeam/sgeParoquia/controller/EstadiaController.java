package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;
import com.paroquiaTeam.sgeParoquia.view.factory.EstadiaCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EstadiaController {
    
    // Enum para deixar o código mais limpo
    public enum TipoOperacao { ENTRADA, SAIDA }
    
    private TipoOperacao operacao;
    private Estadia estadia;
    private EstadiaDAO dao = new EstadiaDAO();
    
    @FXML VBox containerCards;
    @FXML Button btnConfirmar;
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
    	btnConfirmar.setOnAction(e -> {
    		if (this.operacao == TipoOperacao.ENTRADA) { 
    			this.dao.save(estadia);
    		} else if (this.operacao == TipoOperacao.SAIDA) {
    			this.dao.update(estadia);
    		}
    		((Stage) btnConfirmar.getScene().getWindow()).close();
    	});
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
            this.estadia.setDataHoraSaida(LocalDateTime.now());
            PrecificacaoService precificacaoService = new PrecificacaoService();
            this.estadia.setValor(precificacaoService.calcular(
            		TempoUtils.calcularDiferencaMinutos(
            				estadia.getDataHoraEntrada(), 
            				estadia.getDataHoraSaida()),
            		false));
            
            return true;
        } else {
        	return false;
        }
    }
}
