package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VeiculoCardFactory {

	public CardGenerico criarCardInformacoes(Veiculo veiculo) {
		CardHeader header = new CardHeader(veiculo.getId());
		
		VBox conteudo = new VBox();
		
		conteudo.getChildren().addAll(
				new Label("Marca: "+veiculo.getMarca()),
				new Label("Modelo: "+veiculo.getModelo()),
				new Label("Cor: "+veiculo.getCor()));
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();
		return card;
	}
}
