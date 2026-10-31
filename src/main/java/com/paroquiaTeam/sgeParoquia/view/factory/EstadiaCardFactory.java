package com.paroquiaTeam.sgeParoquia.view.factory;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EstadiaCardFactory {
	public CardGenerico criarCardEntrada(Estadia estadia) {
		CardHeader header = new CardHeader(null);
		header.getStyleClass().add("card-header-primario");

		VBox conteudo = new VBox();

		conteudo.getChildren().addAll(
				new Label("Placa: " + estadia.getPlacaVeiculo()),
				new Label("Data de entrada: " + formatarData(estadia.getDataHoraEntrada())),
				new Label("Hora de entrada: " + formatarHora(estadia.getDataHoraEntrada()))
		);
		
		if (estadia.getValor() != null) {
			conteudo.getChildren().add(
					new Label("Valor: R$" + estadia.getValor()));
		}

		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();

		card.getStyleClass().add("card");
		return card;
	}

	public CardGenerico criarCardSaida(Estadia estadia) {
		CardHeader header = new CardHeader(null);

		VBox conteudo = new VBox();

		conteudo.getChildren().addAll(
				new Label("Placa: " + estadia.getPlacaVeiculo()),
				new Label("Data de entrada: " + formatarData(estadia.getDataHoraEntrada())),
				new Label("Hora de entrada: " + formatarHora(estadia.getDataHoraEntrada())),
				new Label("Data de saída: " + formatarData(estadia.getDataHoraSaida())),
				new Label("Hora de saída: " + formatarHora(estadia.getDataHoraSaida())),
				new Label("Valor: " + String.format("R$ %.2f", estadia.getValor()))
		);

		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(conteudo)
				.build();

		card.getStyleClass().add("card");
		return card;
	}

	private String formatarData(LocalDateTime dataHora) {
		return dataHora.getDayOfMonth()+"/"+dataHora.getMonthValue()+"/"+dataHora.getYear();
	}

	private String formatarHora(LocalDateTime dataHora) {
		return dataHora.getHour()+":"+dataHora.getMinute();
	}

//	private HBox criarLinhaInfo(String label, String valor) {
//	    return new HBox(new Label(label), new Label(valor));
//	}
}
