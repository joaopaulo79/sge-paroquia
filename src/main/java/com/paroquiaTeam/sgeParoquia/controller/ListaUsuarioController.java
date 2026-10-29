package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.controller.records.AcoesUsuario;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.service.UsuarioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaUsuarioController {
	UsuarioService service = new UsuarioService();
	
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;
	
	@FXML 
	private void initialize() {
		btnCadastrar.setOnAction(e -> {
			try {
				if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
					throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
				}
				
				NavegacaoManager.getInstancia().abrirModal(Popup.CADASTRARUSUARIO);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
			carregarUsuarios();
		});

		hBoxLabels.getChildren().addAll(
				new Label("Nome"),
				new Label("Tipo"),
				new Label("Ações"));
		
		carregarUsuarios();
	}
	
	private void ativar(Long id) {
		try {
			if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
				throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
			}
			
			if (NavegacaoManager.getInstancia().abrirAlerta(
					"Confirmação",
					"Tem certeza que deseja ativar o usuário?",
					AlertType.CONFIRMATION)) {
				service.ativar(id);
			}
		} catch (Exception e2) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
		}
		

		carregarUsuarios();
	}

	private void desativar(Long id) {
		try {
			if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
				throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
			}
			
			if (NavegacaoManager.getInstancia().abrirAlerta(
					"Confirmação",
					"Tem certeza que deseja desativar o usuário?",
					AlertType.CONFIRMATION)) {
				service.desativar(id);
			} 
		} catch (Exception e2) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
		}

		carregarUsuarios();
	}
	
	private void editar(Long id) {
		try {
			if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
				throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
			}
			
			NavegacaoManager.getInstancia().abrirModal(Popup.EDITARUSUARIO, id);
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e.getMessage());
		}
		carregarUsuarios();
	}
	
	private void excluir(Long id) {
		try {
			if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
				throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
			}
			
			if (NavegacaoManager.getInstancia().abrirAlerta(
					"Confirmação",
					"Tem certeza que deseja excluir o usuário?",
					AlertType.CONFIRMATION)) {
					service.excluir(id);
			}
		} catch (Exception e2) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
		}
		carregarUsuarios();
	}
	
	private void alterarSenha(Long id) {
		try {
			if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
				throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
			}
			
			NavegacaoManager.getInstancia().abrirModal(Popup.ALTERARSENHAUSUARIO, id);
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e.getMessage());
			e.printStackTrace();
		}
		carregarUsuarios();
	}
	
	private void carregarUsuarios() {
		vBoxCards.getChildren().clear();
		List<Usuario> usuarios = service.obterTodos();
		
		AcoesUsuario acoes = new AcoesUsuario(
				this::editar,
				this::excluir,
				this::ativar,
				this::desativar,
				this::alterarSenha
				);
		
		for (Usuario u : usuarios) {
			vBoxCards.getChildren().add(criarCard(u, acoes));
		}
	}
	
	private VBox criarCard(Usuario u, AcoesUsuario acoes) {
		VBox card = new VBox();
		HBox linhaPrincipal = new HBox();
		HBox linhaSecundaria = new HBox();

		Button btnEditar = new Button("Editar");
		Button btnExcluir = new Button("Excluir");
		Button btnAlterarSenha = new Button("Alterar Senha");
		Button btnAlterarStatus;
		
		
		if (!u.isStatus()) {
			btnAlterarStatus = new Button("Ativar");
			btnAlterarStatus.setOnAction(e -> acoes.onAtivar().accept(u.getId()) );
		} else {
			btnAlterarStatus = new Button("Desativar");
			btnAlterarStatus.setOnAction(e -> acoes.onDesativar().accept(u.getId()) );
		}

		btnEditar.setOnAction(e -> acoes.onEditar().accept(u.getId()));
		btnExcluir.setOnAction(e -> acoes.onExcluir().accept(u.getId()));
		btnAlterarSenha.setOnAction(e -> acoes.onAlterarSenha().accept(u.getId()));
		
		linhaPrincipal.getChildren().addAll(
				new Label(u.getNome()),
				new Label(" "+u.getTipo().toString()),
				btnEditar,
				btnAlterarStatus
				);
		
		linhaSecundaria.getChildren().addAll(
				btnAlterarSenha,
				btnExcluir
				);
		
		card.getChildren().addAll(linhaPrincipal, linhaSecundaria);
		return card;
	}
}
