package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Tela;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class BaseController {
	@FXML private AnchorPane contentArea;
	@FXML private VBox sideBar;
	
	// nav buttons
	@FXML private ToggleButton btnNavDashboard;
	@FXML private ToggleButton btnNavConfig;
	@FXML private ToggleButton btnNavVagas;
		
	private static BaseController instance;
	
	private Tela telaAtual;
	
	@FXML
	private void initialize() {
		NavegacaoManager.getInstancia().setContainerPrincipal(this.contentArea);
		
		LoginController loginController = NavegacaoManager.getInstancia().navegarPara(Tela.LOGIN);
		
		loginController.setOnSucessoLogin(() -> {
			sideBar.setVisible(true);
			sideBar.setManaged(true);
			
			NavegacaoManager.getInstancia().navegarPara(Tela.DASHBOARD);
		});
		
		ToggleGroup grupoNav = new ToggleGroup();
		btnNavDashboard.setToggleGroup(grupoNav);
		btnNavConfig.setToggleGroup(grupoNav);
		btnNavVagas.setToggleGroup(grupoNav);
		
		btnNavDashboard.setOnAction(event -> navegar(event, Tela.DASHBOARD));
		btnNavConfig.setOnAction(event -> navegar(event, Tela.CONFIGURACAO));
		btnNavVagas.setOnAction(event -> navegar(event, Tela.VAGAS));

		instance = this;
	}
	
	public static BaseController getInstance() {
		return instance;
	}
	
	private void navegar(ActionEvent e, Tela tela) {
		ToggleButton btn = (ToggleButton) e.getSource();

		if (this.telaAtual == tela) {
			e.consume();
			btn.setSelected(true);
			return;
		}
		
		NavegacaoManager.getInstancia().navegarPara(tela);
	}
	
	public void habilitarSideBar() {
		sideBar.setManaged(true);
		sideBar.setVisible(true);
	}
}
