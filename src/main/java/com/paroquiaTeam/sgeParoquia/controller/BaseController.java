package com.paroquiaTeam.sgeParoquia.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class BaseController {
	@FXML private AnchorPane contentArea;
	@FXML private VBox sideBar;
	
	// nav buttons
	@FXML private ToggleButton btnNavConfig;
	@FXML private ToggleButton btnNavVagas;
	
	private final Map<String, Parent> cacheScreens = new HashMap<String, Parent>();
	
	private static BaseController instance;
	
	@FXML
	private void initialize() {
		ToggleGroup grupoNav = new ToggleGroup();
		btnNavConfig.setToggleGroup(grupoNav);
		btnNavVagas.setToggleGroup(grupoNav);
				
		btnNavConfig.setOnAction(event -> navegar(event, "configuracaoSistema", "/screens/configuracaoSistema/configuracaoSistema.fxml"));
		btnNavVagas.setOnAction(event -> navegar(event, "listaVagas", "/screens/listaVagas/listaVagas.fxml"));

		instance = this;
	}
	
	public static BaseController getInstance() {
		return instance;
	}
	
	private void navegar(ActionEvent e, String id, String caminho) {
		ToggleButton btn = (ToggleButton) e.getSource();
		if (!btn.isSelected()) {
			e.consume();
			btn.setSelected(true);
			return;
		}
		mostrarTela(id, caminho);
	}
	
	public void mostrarTela(String id, String caminho) {
		Parent tela = cacheScreens.computeIfAbsent(caminho, k -> {
			try {
				return FXMLLoader.load(getClass().getResource(caminho));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		contentArea.getChildren().setAll(tela);
		
		AnchorPane.setTopAnchor(tela, 0.0);    
		AnchorPane.setBottomAnchor(tela, 0.0);    
		AnchorPane.setLeftAnchor(tela, 0.0);    
		AnchorPane.setRightAnchor(tela, 0.0);
	}
	
	public void habilitarSideBar() {
		sideBar.setManaged(true);
		sideBar.setVisible(true);
	}
}
