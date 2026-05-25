package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.model.Vaga;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class VagaCardFactory {
	public CardGenerico criarCard(Vaga vaga) {
		CardHeader header = new CardHeader(null);
		
		HBox conteudo = new HBox();
		Label label = new Label();
		conteudo.getChildren().add(label);
		
		javafx.scene.layout.VBox.setVgrow(conteudo, javafx.scene.layout.Priority.ALWAYS);
		
		if (vaga.isOcupada()) {
			header.getStyleClass().add("card-header-atencao");
			label.setText(vaga.getPlaca());
		} else {
			header.getStyleClass().add("card-header-primario");
		}
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();
		double largura = 150;
		double altura = 80;
		card.setMinWidth(largura);
		card.setMinHeight(altura);
		card.setPrefWidth(largura);
		card.setPrefHeight(altura);
		card.setMaxWidth(largura);
		card.setMaxHeight(altura);
		
		card.getStyleClass().add("card");
		return card;
	}
}
