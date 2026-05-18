package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
	@FXML TextField campoLogin;
	@FXML TextField campoSenha;
	@FXML Button botaoLogin;
	@FXML Text textoStatus;
	
	@FXML
	public void onBotaoLoginAction() {
		if (!new UsuarioDAO().autenticar(campoLogin.getText(), campoSenha.getText())) {
			textoStatus.setVisible(true);
		} else {			
			BaseController controller = BaseController.getInstance();
			controller.habilitarSideBar();
			controller.mostrarTela("testes", "/fxml/TesteView.fxml");
		}
	}
}
