package com.paroquiaTeam.sgeParoquia.controller;

import java.util.EnumMap;
import java.util.Map;

import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoFracionadaDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoPorHoraDAO;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;
import com.paroquiaTeam.sgeParoquia.view.TipoPrecificacaoViewModel;
import com.paroquiaTeam.sgeParoquia.view.factory.PainelPrecificacaoFracionadaFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.PainelPrecificacaoPorHoraFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ConfiguracaoSistemaController {
	private final Map<TipoPrecificacao, PainelPrecificacao> paineis = new EnumMap<>(TipoPrecificacao.class);
	
	@FXML ChoiceBox<TipoPrecificacao> escolhaTipoPrecificacao;
	@FXML VBox containerCamposPrecificacao;
	
	private final PrecificacaoController controllerPrecificacao = new PrecificacaoController();
	
	@FXML
	public void initialize() {
		escolhaTipoPrecificacao.getItems().addAll(TipoPrecificacao.values());
		escolhaTipoPrecificacao.setValue(TipoPrecificacao.FRACIONADA);
		
		escolhaTipoPrecificacao.setOnAction(e -> {
			trocarPainelPrecificacao();
		});
		
		// renderizando primeiro painel aqui
		trocarPainelPrecificacao(); 
	}	
	
	private void trocarPainelPrecificacao() {
	    TipoPrecificacao tipo = escolhaTipoPrecificacao.getValue();
	    
	    PainelPrecificacao painel = switch (tipo) {
	        case POR_HORA -> new PainelPrecificacaoPorHoraFactory(controllerPrecificacao).criar();
	        case FRACIONADA -> new PainelPrecificacaoFracionadaFactory(controllerPrecificacao).criar();
	    };
	    containerCamposPrecificacao.getChildren().setAll(painel);
	}
}
