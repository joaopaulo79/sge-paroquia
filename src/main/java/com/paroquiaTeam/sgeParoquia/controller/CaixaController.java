package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.dao.CaixaDAO;
import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CaixaController {
	private DashboardController dashboardController;
	
	@FXML TextField campoFundoInicial;
	@FXML Button btnAbrirCaixa;
	@FXML Label labelOperador;
	@FXML Label labelHora;
	
	private CaixaDAO caixaDao = new CaixaDAO();
	
	public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
	
	private void definirDadosNaTela(Usuario usuario, LocalDateTime horaData) {
        if (labelOperador != null && usuario != null && 
        		labelHora != null && horaData != null) {
        	labelOperador.setText(usuario.getNome()); 
        	labelHora.setText(horaData.getHour()+":"+horaData.getMinute());
        }
    }
	
	public void abrirCaixa() {
		String valorInserido = campoFundoInicial.getText();
		double valorInicial;
		if (valorInserido.isBlank()) {
			valorInicial = 0;
		} else {
			valorInicial = Double.parseDouble(valorInserido);
		}
		Usuario user = SessaoSistema.getInstancia().getUserLogado();
		Caixa caixa = new Caixa(LocalDateTime.now(), null, valorInicial, user);
		caixaDao.save(caixa);
		
		SessaoSistema.getInstancia().setCaixa(caixa);
		
		dashboardController.carregarPainel(true);
		definirDadosNaTela(user, caixa.getDataHoraAbertura());
	}
	
	public void fecharCaixa() {
		Caixa caixaAtual = SessaoSistema.getInstancia().getCaixa();
		
		caixaAtual.setDataHoraFechamento(LocalDateTime.now());
		caixaDao.update(caixaAtual);
		
		SessaoSistema.getInstancia().setCaixa(null);

		
		dashboardController.carregarPainel(false);
	}
}
