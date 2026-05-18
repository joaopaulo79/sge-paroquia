package com.paroquiaTeam.sgeParoquia.view;

import java.util.ArrayList;
import java.util.List;

import com.paroquiaTeam.sgeParoquia.view.TipoPrecificacaoViewModel;

import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PainelPrecificacao extends VBox{
	private final TextField campoTolerancia = new TextField();
	private final List<TextField> camposCarro = new ArrayList<TextField>();
	private final List<TextField> camposMoto = new ArrayList<TextField>();
	
	public PainelPrecificacao(TipoPrecificacaoViewModel tipo) {		
		getChildren().addAll(new Label("Tolerância (minutos):"), campoTolerancia);
		getChildren().add(new Separator());
		
		getChildren().add(construirSecao("Preços para Carros:", tipo.labelsCampos, camposCarro));
		getChildren().add(new Separator());
		getChildren().add(construirSecao("Preços para Motos:", tipo.labelsCampos, camposMoto));
	}
	
	private VBox construirSecao(String titulo, String[] labels, List<TextField> campos) {
		VBox secao = new VBox();
		secao.getChildren().add(new Label(titulo));
		
		for (String label : labels) {
			TextField campo = new TextField();
			campos.add(campo);
			secao.getChildren().addAll(new Label(label), campo);
		}
		return secao;
	}
}
