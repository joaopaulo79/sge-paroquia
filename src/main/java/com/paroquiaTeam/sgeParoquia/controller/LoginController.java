package com.paroquiaTeam.sgeParoquia.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML TextField campoLogin;
	@FXML TextField campoSenha;
	@FXML Button botaoLogin;
	
	@FXML
	public void onBotaoLoginAction() {
		BaseController controller = BaseController.getInstance();
		controller.habilitarSideBar();
		controller.mostrarTela("testes", "/fxml/TesteView.fxml");
	}
}
