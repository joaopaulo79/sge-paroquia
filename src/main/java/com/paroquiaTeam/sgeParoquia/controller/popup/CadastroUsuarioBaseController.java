package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosUsuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.service.UsuarioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroUsuarioBaseController {
	UsuarioService service = new UsuarioService();
	boolean cadastrou = false;
	
	@FXML TextField textFieldNome;
	@FXML TextField textFieldLogin;
	@FXML PasswordField passwordFieldSenha;
	@FXML PasswordField passwordFieldSenhaRepetida;
	
	@FXML RadioButton radioOperador;
	@FXML RadioButton radioAdministrador;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		btnCancelar.setVisible(false);
		btnCancelar.setManaged(false);
		btnCancelar.setDisable(true);
		
		radioOperador.setDisable(true);
		radioAdministrador.setDisable(true);
		radioAdministrador.setSelected(true);
		
		btnSalvar.setOnAction(e -> {
			try {				
				if (!passwordFieldSenha.getText().trim().equals(passwordFieldSenhaRepetida.getText().trim())) {
					throw new IllegalArgumentException("Senha e senha repetida não são iguais");
				}
				
				DadosUsuario dados = new DadosUsuario(
						textFieldNome.getText().trim(),
						textFieldLogin.getText().trim(),
						passwordFieldSenha.getText().trim(),
						true,
						TipoUsuario.ADMINISTRADOR
						);
				
				service.salvar(dados);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				cadastrou = true;
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}

	
	public boolean getCadastrou() {
		return cadastrou;
	}
}
