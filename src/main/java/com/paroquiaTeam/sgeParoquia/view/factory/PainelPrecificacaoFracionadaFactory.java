package com.paroquiaTeam.sgeParoquia.view.factory;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.controller.PrecificacaoController;
import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

public class PainelPrecificacaoFracionadaFactory {
	private final PrecificacaoController controller;
	
	public PainelPrecificacaoFracionadaFactory(PrecificacaoController controller) {
		this.controller = controller;
	}
	
	public PainelPrecificacao criar() {
		TextField campoTolerancia = new TextField();
		TextField campoMeiaHora = new TextField();
		TextField campoHora = new TextField();
		TextField campoDiaria = new TextField();
		TextField campoMeiaHoraMoto = new TextField();
		TextField campoHoraMoto = new TextField();
		TextField campoDiariaMoto = new TextField();
		
		controller.buscarFracionada().ifPresent((p) -> {
			campoTolerancia.setText(Integer.toString(p.getTolerancia()));
			campoMeiaHora.setText(Double.toString(p.getValorMeiaHora()));
			campoHora.setText(Double.toString(p.getValorHora()));
			campoDiaria.setText(Double.toString(p.getValorDiaria()));
			campoMeiaHoraMoto.setText(Double.toString(p.getValorMeiaHoraMoto()));
			campoHoraMoto.setText(Double.toString(p.getValorHoraMoto()));
			campoDiariaMoto.setText(Double.toString(p.getValorDiariaMoto()));
		});
		
		Button btnSalvar = new Button("Salvar");
		btnSalvar.setOnAction(e -> {
			int tolerancia = Integer.parseInt(campoTolerancia.getText());
			double valorMeiaHora = Double.parseDouble(campoMeiaHora.getText());
			double valorHora = Double.parseDouble(campoHora.getText());
			double valorDiaria = Double.parseDouble(campoDiaria.getText());
			double valorMeiaHoraMoto = Double.parseDouble(campoMeiaHoraMoto.getText());
			double valorHoraMoto = Double.parseDouble(campoMeiaHoraMoto.getText());
			double valorDiariaMoto = Double.parseDouble(campoDiariaMoto.getText());
			controller.salvarPrecificacaoFracionada(tolerancia, 
					valorMeiaHora, valorHora, valorDiaria, 
					valorMeiaHoraMoto, valorHoraMoto, valorDiariaMoto);
		});
		List<Node> elementos = List.of(
				new Label("Tolerância (minutos):"),
				campoTolerancia, 
				new Separator(),
				new Label("Preços para carros:"),
				new Label("Meia Hora:"),
				campoMeiaHora, 
				new Label("Hora:"),
				campoHora, 
				new Label("Diária"),
				campoDiaria,
				new Separator(),
				new Label("Preços para motos:"),
				new Label("Meia Hora:"),
				campoMeiaHoraMoto,
				new Label("Hora:"),
				campoHoraMoto, 
				new Label("Diária"),
				campoDiariaMoto);
		return new PainelPrecificacao(elementos, btnSalvar);
	}
}
