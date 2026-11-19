package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDate;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.service.RelatoriosService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class RelatoriosController {
	@FXML Button btnEscolherCaixa;
	
	@FXML DatePicker dateMovimentosInicio;
	@FXML DatePicker dateMovimentosFim;
	@FXML Button btnRelatorioMovimentos;

	@FXML DatePicker dateExtratoInicio;
	@FXML DatePicker dateExtratoFim;
	@FXML Button btnExtratoCaixas;
	
	@FXML DatePicker dateFluxoInicio;
	@FXML DatePicker dateFluxoFim;
	@FXML Button btnFluxoVeiculos;
	
	@FXML
	private void initialize() {
		btnEscolherCaixa.setOnAction(e -> NavegacaoManager.getInstancia().abrirModal(Popup.SELECIONARCAIXA));
		
		btnRelatorioMovimentos.setOnAction(e -> {
			LocalDate inicio = dateMovimentosInicio.getValue();
			LocalDate fim = dateMovimentosFim.getValue();
			if (inicio == null || fim == null) {
				NavegacaoManager.getInstancia().abrirAlertaErro("Insira data de início e fim");
			} 
			else {
				new RelatoriosService().gerarRelatorioMovimentos(inicio.atStartOfDay(), fim.atTime(23, 59));
			}
		});
		
		btnExtratoCaixas.setOnAction(e -> {
			LocalDate inicio = dateExtratoInicio.getValue();
			LocalDate fim = dateExtratoFim.getValue();
			if (inicio == null || fim == null) {
				NavegacaoManager.getInstancia().abrirAlertaErro("Insira data de início e fim");
			} 
			else {
				new RelatoriosService().gerarRelatorioExtrato(inicio.atStartOfDay(), fim.atTime(23, 59));
			}
		});
		
		btnFluxoVeiculos.setOnAction(e -> {
			LocalDate inicio = dateFluxoInicio.getValue();
			LocalDate fim = dateFluxoFim.getValue();
			if (inicio == null || fim == null) {
				NavegacaoManager.getInstancia().abrirAlertaErro("Insira data de início e fim");
			} 
			else {
				new RelatoriosService().gerarRelatorioFluxo(inicio.atStartOfDay(), fim.atTime(23, 59));
			}
		});
	}
}
