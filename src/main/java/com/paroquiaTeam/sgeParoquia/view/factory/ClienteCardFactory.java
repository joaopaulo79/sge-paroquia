package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ClienteCardFactory {
	public CardGenerico criarCardInformacoes(Cliente cliente) {
		CardHeader header = new CardHeader(cliente.getId());
		
		VBox conteudo = new VBox();
		
		conteudo.getChildren().addAll(
				new Label("Cpf: "+cliente.getCpf()),
				new Label("Nome: "+cliente.getNome()),
				new Label("Tipo: "+cliente.getTipo().toString())
				);
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();
		return card;
	}
}
