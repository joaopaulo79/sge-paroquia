package com.paroquiaTeam.sgeParoquia.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PainelPrecificacao extends VBox{
	public PainelPrecificacao(List<Node> campos, Button btn) {
		
		setSpacing(15);
        setPadding(new Insets(10));
        VBox.setMargin(btn, new Insets(20, 0, 0, 0));
        
		getChildren().addAll(campos);
		getChildren().add(btn);
	}
}
