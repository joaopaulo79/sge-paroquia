package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ConvenioCardFactory {
	public CardGenerico criarCardInformacoes(Convenio convenio) {
		CardHeader header = new CardHeader(convenio.getId());
		
		VBox conteudo = new VBox();
		
		conteudo.getChildren().addAll(
				new Label("CNPJ: "+convenio.getCnpj()),
				new Label("Nome: "+convenio.getNome()),
				new Label("Cobrança individual: R$"+convenio.getCobrancaIndividual())
				);
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();
		return card;
	}
}
