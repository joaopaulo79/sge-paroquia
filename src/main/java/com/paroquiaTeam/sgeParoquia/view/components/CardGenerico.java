package com.paroquiaTeam.sgeParoquia.view.components;

import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CardGenerico extends VBox{
	private final VBox conteudoExpandido = new VBox();
	private final HBox linhaPrincipal;
	private boolean expandido = false;
	
	// Sem expansão
	public CardGenerico(CardHeader header, HBox linhaPrincipal) {
		this.linhaPrincipal = linhaPrincipal;
		getChildren().addAll(header, linhaPrincipal);
	}
	
	
	// Expansível
	public CardGenerico(HBox header, HBox linhaPrincipal, List<VBox> linhasExtras) {
		this.linhaPrincipal = linhaPrincipal;
		
		header.setOnMouseClicked(e -> toggleExpansao());
		linhasExtras.forEach(linha -> conteudoExpandido.getChildren().add(linha));
		conteudoExpandido.setVisible(false);
		conteudoExpandido.setManaged(false);
		
		getChildren().addAll(header, linhaPrincipal, conteudoExpandido);
	}
	
	private void toggleExpansao() {
		expandido = !expandido;
		conteudoExpandido.setVisible(expandido);
		conteudoExpandido.setManaged(expandido);
		
		if (expandido) {
			linhaPrincipal.getStyleClass().add("linha-principal-expandida"); 
		} else {
			linhaPrincipal.getStyleClass().remove("linha-principal-expandida");
		}
	}
}
