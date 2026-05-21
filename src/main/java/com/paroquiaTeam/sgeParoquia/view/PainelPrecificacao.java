package com.paroquiaTeam.sgeParoquia.view;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PainelPrecificacao extends VBox{
	public PainelPrecificacao(List<Node> campos, Button btn) {
		getChildren().addAll(campos);
		getChildren().add(btn);
	}
}
