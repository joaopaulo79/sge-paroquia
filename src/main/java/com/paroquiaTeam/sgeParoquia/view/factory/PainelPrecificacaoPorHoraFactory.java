package com.paroquiaTeam.sgeParoquia.view.factory;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.controller.PrecificacaoController;
import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

public class PainelPrecificacaoPorHoraFactory {
private final PrecificacaoController controller;
	
	public PainelPrecificacaoPorHoraFactory(PrecificacaoController controller) {
		this.controller = controller;
	}
	
	public PainelPrecificacao criar() {
		TextField campoTolerancia = new TextField();
		TextField campoEntrada = new TextField();
		TextField campoHora = new TextField();
		TextField campoDiaria = new TextField();
		TextField campoEntradaMoto = new TextField();
		TextField campoHoraMoto = new TextField();
		TextField campoDiariaMoto = new TextField();
		
		controller.buscarPorHora().ifPresent((p) -> {
			campoTolerancia.setText(Integer.toString(p.getTolerancia()));
			campoEntrada.setText(Double.toString(p.getValorEntrada()));
			campoHora.setText(Double.toString(p.getValorHora()));
			campoDiaria.setText(Double.toString(p.getValorDiaria()));
			campoEntradaMoto.setText(Double.toString(p.getValorEntradaMoto()));
			campoHoraMoto.setText(Double.toString(p.getValorHoraMoto()));
			campoDiariaMoto.setText(Double.toString(p.getValorDiariaMoto()));
		});
		
		Button btnSalvar = new Button("Salvar");
		btnSalvar.setOnAction(e -> {
			int tolerancia = Integer.parseInt(campoTolerancia.getText());
			double valorEntrada = Double.parseDouble(campoEntrada.getText());
			double valorHora = Double.parseDouble(campoHora.getText());
			double valorDiaria = Double.parseDouble(campoDiaria.getText());
			double valorEntradaMoto = Double.parseDouble(campoEntradaMoto.getText());
			double valorHoraMoto = Double.parseDouble(campoEntradaMoto.getText());
			double valorDiariaMoto = Double.parseDouble(campoDiariaMoto.getText());
			controller.salvarPrecificacaoPorHora(tolerancia, 
					valorEntrada, valorHora, valorDiaria, 
					valorEntradaMoto, valorHoraMoto, valorDiariaMoto);
		});
		List<Node> elementos = List.of(
				new Label("Tolerância (minutos):"),
				campoTolerancia, 
				new Separator(),
				new Label("Preços para carros:"),
				new Label("Entrada:"),
				campoEntrada, 
				new Label("Hora:"),
				campoHora, 
				new Label("Diária"),
				campoDiaria,
				new Separator(),
				new Label("Preços para motos:"),
				new Label("Entrada:"),
				campoEntradaMoto,
				new Label("Hora:"),
				campoHoraMoto, 
				new Label("Diária"),
				campoDiariaMoto);
		return new PainelPrecificacao(elementos, btnSalvar);
	}
}
