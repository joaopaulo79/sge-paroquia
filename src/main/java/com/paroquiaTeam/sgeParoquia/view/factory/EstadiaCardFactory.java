package com.paroquiaTeam.sgeParoquia.view.factory;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EstadiaCardFactory {
	public CardGenerico criarCardEntrada(Estadia estadia) {
		CardHeader header = new CardHeader(null);
		header.getStyleClass().add("card-header-primario");
		
		VBox conteudo = new VBox();
		
		HBox linhaPlaca = new HBox();
		linhaPlaca.getChildren().addAll(new Label("Placa:"), new Label(estadia.getPlacaVeiculo()));

		HBox linhaHoraEntrada = new HBox();
		String horaEntrada = (estadia.getDataHoraEntrada().getHour()+":"+estadia.getDataHoraEntrada().getMinute());
		linhaHoraEntrada.getChildren().addAll(new Label("Hora de entrada:"), new Label(horaEntrada));
		
		conteudo.getChildren().addAll(linhaPlaca, linhaHoraEntrada);

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
				criarLinhaInfo("Placa:", estadia.getPlacaVeiculo()),
		        criarLinhaInfo("Data de entrada:", formatarData(estadia.getDataHoraEntrada())),
		        criarLinhaInfo("Hora de entrada:", formatarHora(estadia.getDataHoraEntrada())),
		        criarLinhaInfo("Data de saída:", formatarData(estadia.getDataHoraSaida())),
		        criarLinhaInfo("Hora de saída:", formatarHora(estadia.getDataHoraSaida())),
		        criarLinhaInfo("Valor:", String.format("R$ %.2f", estadia.getValor()))
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
	
	private HBox criarLinhaInfo(String label, String valor) {
	    return new HBox(new Label(label), new Label(valor));
	}
}
