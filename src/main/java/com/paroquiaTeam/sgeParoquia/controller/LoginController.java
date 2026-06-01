package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	private Runnable onSucessoLogin;
	
	public void setOnSucessoLogin(Runnable onSucessoLogin) {
		this.onSucessoLogin = onSucessoLogin;
	}

	@FXML TextField campoLogin;
	@FXML TextField campoSenha;
	@FXML Button botaoLogin;
	@FXML Label textoStatus;
	
	@FXML
	public void onBotaoLoginAction() {
		UsuarioDAO dao = new UsuarioDAO();
		if (!dao.autenticar(campoLogin.getText(), campoSenha.getText())) {
			textoStatus.setVisible(true);
		} else {	
			Usuario usuario = dao.getByLogin(campoLogin.getText()).get();
			SessaoSistema.getInstancia().setUserLogado(usuario);
			
			if (onSucessoLogin != null) {
				onSucessoLogin.run();
			}
		}		
	}
}
