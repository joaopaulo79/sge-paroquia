package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.controller.records.AcoesUsuario;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UsuarioCardFactory {
	public CardGenerico criarCardExpandivel(Usuario usuario, AcoesUsuario acoes) {
		//Criando header
		CardHeader header = new CardHeader(usuario.getId());

		if (usuario.isStatus()) {
			header.getStyleClass().add("card-header-ativo");
		} else {
			header.getStyleClass().add("card-header-inativo");
		}
		
		

		// Montando botões da linha principal
		Button btnEditar = new Button("Editar");
		Button btnAlterarStatus;
		
		if (!usuario.isStatus()) {
			btnAlterarStatus = new Button("Ativar");
			btnAlterarStatus.setOnAction(e -> acoes.onAtivar().accept(usuario.getId()) );
		} else {
			btnAlterarStatus = new Button("Desativar");
			btnAlterarStatus.setOnAction(e -> acoes.onDesativar().accept(usuario.getId()) );
		}

		btnEditar.setOnAction(e -> acoes.onEditar().accept(usuario.getId()));

		btnEditar.setAlignment(Pos.CENTER_RIGHT);
		btnAlterarStatus.setAlignment(Pos.CENTER_RIGHT);

		//Montando linha principal
		HBox linhaPrincipal = new HBox();
		linhaPrincipal.getChildren().add(new Label(usuario.getNome()));
		linhaPrincipal.getChildren().add(new Label(usuario.getTipo().toString()));
		linhaPrincipal.getChildren().addAll(btnEditar, btnAlterarStatus);
		
		// Montando botões da linha secundária
		Button btnExcluir = new Button("Excluir");
		Button btnAlterarSenha = new Button("Alterar Senha");
		
		btnExcluir.setOnAction(e -> acoes.onExcluir().accept(usuario.getId()));
		btnAlterarSenha.setOnAction(e -> acoes.onAlterarSenha().accept(usuario.getId()));
		
		VBox linhaSecundaria = new VBox();
		HBox acoesSecundarias = new HBox();
		
		acoesSecundarias.getChildren().addAll(btnExcluir, btnAlterarSenha);
		linhaSecundaria.getChildren().addAll(acoesSecundarias);

		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(linhaPrincipal)
				.adicionarLinhaExtra(linhaSecundaria)
				.build();

		return card;
	}
}
