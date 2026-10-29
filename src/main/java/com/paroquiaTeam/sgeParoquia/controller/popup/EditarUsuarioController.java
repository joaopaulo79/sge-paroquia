package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosUsuario;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.service.UsuarioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditarUsuarioController {
	Long id;
	Usuario usuario;
	public EditarUsuarioController(Long id) {
		this.id = id;
		usuario = service.obterPorId(id);
	}
	
	UsuarioService service = new UsuarioService();
	
	@FXML TextField textFieldNome;
	@FXML TextField textFieldLogin;
	
	@FXML VBox vBoxCamposSenha;
	
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
		
		vBoxCamposSenha.setManaged(false);
		vBoxCamposSenha.setVisible(false);
		vBoxCamposSenha.setDisable(true);
		
		radioOperador.setUserData(TipoUsuario.OPERADOR);
		radioAdministrador.setUserData(TipoUsuario.ADMINISTRADOR);

		textFieldNome.setText(usuario.getNome());
		textFieldLogin.setText(usuario.getLogin());

		if (usuario.getTipo() == TipoUsuario.OPERADOR) {
			radioOperador.setSelected(true);
		} else {
			radioAdministrador.setSelected(true);
		}
		
		btnSalvar.setOnAction(e -> {
			try {
				TipoUsuario tipo = obterTipoUsuarioSelecionado();
				
				DadosUsuario dadosNovos = new DadosUsuario(
						textFieldNome.getText().trim(),
						textFieldLogin.getText().trim(),
						"", // Não importa. Senha não é alterada no método 'atualizar()'
						usuario.isStatus(),
						tipo
						);
				
				service.atualizar(id, dadosNovos);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Editado com sucesso!");
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
