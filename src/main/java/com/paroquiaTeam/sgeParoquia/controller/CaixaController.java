package com.paroquiaTeam.sgeParoquia.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CaixaController {
	private DashboardController dashboardController;
	
	@FXML Button btnAbrirCaixa;
	
	
	
	public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
	
	public void abrirCaixa() {
		dashboardController.caixaAberto = true;
		dashboardController.carregarPainel();
	}
	
	public void fecharCaixa() {
		dashboardController.caixaAberto = false;
		dashboardController.carregarPainel();
	}
}
