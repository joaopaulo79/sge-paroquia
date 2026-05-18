package com.paroquiaTeam.sgeParoquia.controller;

import java.util.EnumMap;
import java.util.Map;

import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;
import com.paroquiaTeam.sgeParoquia.view.TipoPrecificacaoViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ConfiguracaoSistemaController {
	private final Map<TipoPrecificacaoViewModel, PainelPrecificacao> paineis = new EnumMap<>(TipoPrecificacaoViewModel.class);
	
	@FXML ChoiceBox<TipoPrecificacaoViewModel> escolhaTipoPrecionamento;
	@FXML VBox containerCamposPrecificacao;
	
	@FXML
	public void initialize() {
		for (TipoPrecificacaoViewModel tipo : TipoPrecificacaoViewModel.values()) {
			paineis.put(tipo, new PainelPrecificacao(tipo));
		}
		
		escolhaTipoPrecionamento.getItems().addAll(TipoPrecificacaoViewModel.values());
		escolhaTipoPrecionamento.setValue(TipoPrecificacaoViewModel.FRACIONADA);
		containerCamposPrecificacao.getChildren().setAll(paineis.get(TipoPrecificacaoViewModel.FRACIONADA));
		
		escolhaTipoPrecionamento.valueProperty().addListener((observer, valorAntigo, valorNovo) -> {
			containerCamposPrecificacao.getChildren().setAll(paineis.get(valorNovo));
		});
	};
}
