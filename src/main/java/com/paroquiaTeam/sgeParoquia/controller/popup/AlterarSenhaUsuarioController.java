package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.service.UsuarioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class AlterarSenhaUsuarioController {
	Long id;
	Usuario usuario;
	
	public AlterarSenhaUsuarioController(Long id) {
		this.id = id;
		usuario = service.obterPorId(id);
	}
	
	UsuarioService service = new UsuarioService();
	
	@FXML PasswordField passwordFieldSenhaAntiga;
	@FXML PasswordField passwordFieldSenha;
	@FXML PasswordField passwordFieldSenhaRepetida;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});
		
		
		btnSalvar.setOnAction(e -> {
			try {
				if (!passwordFieldSenha.getText().trim().equals(passwordFieldSenhaRepetida.getText().trim())) {
					throw new IllegalArgumentException("Senha e senha repetida não são iguais");
				}
				
				if (!service.autenticar(usuario.getLogin(), passwordFieldSenhaAntiga.getText().trim())) {
					throw new IllegalArgumentException("Senha antiga inválida");
				}
				
				service.alterarSenha(id, passwordFieldSenha.getText().trim());
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Senha atualizada com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
}
