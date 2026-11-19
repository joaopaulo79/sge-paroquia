package com.paroquiaTeam.sgeParoquia.view.factory;

import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CaixaCardFactory {
	public CardGenerico criarCard(Caixa c, Consumer<Long> onVerMovimentos, Consumer<Long> onGerarRelatorio) {
		CardHeader header = new CardHeader(c.getId());


		String labelFechamento = (c.getDataHoraFechamento() == null)
				? "Ainda aberto"
				: c.getDataHoraFechamento().toString();

		HBox linhaPrincipal = new HBox();
		linhaPrincipal.getChildren().add(new Label(c.getDataHoraAbertura().toString()));
		linhaPrincipal.getChildren().add(new Label(labelFechamento));
		
		if (onVerMovimentos != null) {
			Button btnVerMovimentos = new Button("Movimentos");
			btnVerMovimentos.setOnAction(e -> onVerMovimentos.accept(c.getId()));
			linhaPrincipal.getChildren().add(btnVerMovimentos);
		}
		
		if (onGerarRelatorio != null) {
			Button btnGerarRelatorio = new Button("Gerar Relatório");
			btnGerarRelatorio.setOnAction(e -> onGerarRelatorio.accept(c.getId()));
			linhaPrincipal.getChildren().add(btnGerarRelatorio);
		}

		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(linhaPrincipal)
				.build();

		return card;
	}
}
