package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosUsuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.service.UsuarioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CadastroUsuarioController {
	UsuarioService service = new UsuarioService();
	
	@FXML TextField textFieldNome;
	@FXML TextField textFieldLogin;
	@FXML TextField textFieldSenha;
	@FXML TextField textFieldSenhaRepetida;
	
	@FXML ToggleGroup grupoTipoUsuario;
	@FXML RadioButton radioOperador;
	@FXML RadioButton radioAdministrador;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});
		
		radioOperador.setUserData(TipoUsuario.OPERADOR);
		radioAdministrador.setUserData(TipoUsuario.ADMINISTRADOR);
		
		btnSalvar.setOnAction(e -> {
			try {
				TipoUsuario tipo = obterTipoUsuarioSelecionado();
				
				if (!textFieldSenha.getText().trim().equals(textFieldSenhaRepetida.getText().trim())) {
					throw new IllegalArgumentException("Senha e senha repetida não são iguais");
				}
				
				DadosUsuario dados = new DadosUsuario(
						textFieldNome.getText().trim(),
						textFieldLogin.getText().trim(),
						textFieldSenha.getText().trim(),
						false,
						tipo
						);
				
				service.salvar(dados);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
	
	private TipoUsuario obterTipoUsuarioSelecionado() {
	    Toggle selectedToggle = grupoTipoUsuario.getSelectedToggle();
	    
	    if (selectedToggle != null && selectedToggle.getUserData() instanceof TipoUsuario) {
	        return (TipoUsuario) selectedToggle.getUserData();
	    }
	    
	    throw new IllegalStateException("Nenhum tipo de usuário foi selecionado.");
	}
}
