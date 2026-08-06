package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.controller.UsuarioController;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class UsuarioCardFactory {
	private final UsuarioController controller;
	
	public UsuarioCardFactory(UsuarioController controller) {
		this.controller = controller;
	}
	
	public CardGenerico criarCardExpandivel(Usuario usuario) {
		//Criando header
		CardHeader header = new CardHeader(usuario.getId());
		
		if (usuario.isStatus()) {
			header.getStyleClass().add("card-header-ativo");
		} else {
			header.getStyleClass().add("card-header-inativo");
		}
		
		// Montando botões da linha principal
		Button btnEditar = new Button("Editar");
		Button btnDesativar = new Button("Desativar");
		
		btnEditar.setOnAction(e -> controller.editar(usuario));
		btnDesativar.setOnAction(e -> controller.desabilitar(usuario));
		
		btnEditar.setAlignment(Pos.CENTER_RIGHT);
		btnDesativar.setAlignment(Pos.CENTER_RIGHT);
		
		//Montando linha principal
		HBox linhaPrincipal = new HBox();
		linhaPrincipal.getChildren().add(new Label(usuario.getNome()));
		linhaPrincipal.getChildren().add(new Label(usuario.getTipo().toString()));
		linhaPrincipal.getChildren().addAll(btnEditar, btnDesativar);
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(linhaPrincipal)
				.build();
		
		return card;
	}
}
