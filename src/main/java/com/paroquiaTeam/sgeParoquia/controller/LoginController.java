package com.paroquiaTeam.sgeParoquia.controller;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.service.CaixaService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
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

	    	ajustarCaixa(usuario);
	    	
			if (onSucessoLogin != null) {
				onSucessoLogin.run();
			}
		}
		
		
	}
	
	private void ajustarCaixa(Usuario u) {
		CaixaService caixaService = new CaixaService();
		// Só vamos fazer algo se último caixa não fechou
    	if (!caixaService.ultimoCaixaFechou()) {
    		
    		Optional<Caixa> talvezCaixa = caixaService.buscarUltimoComOperador();
    		
    		if (talvezCaixa.isPresent()) {
    			Caixa caixa = talvezCaixa.get();
    			
    			if (caixa.getOperador().getId() == u.getId()) {
    				SessaoSistema.getInstancia().setCaixa(caixa);
    				
    				String mensagem = "Seu último caixa não foi fechado antes de encerrar o sistema. O caixa será restaurado";
    				NavegacaoManager.getInstancia().abrirAlerta("Aviso", mensagem, AlertType.WARNING);
    			}
    		}
    	}
	}
}
