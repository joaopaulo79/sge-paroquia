package com.paroquiaTeam.sgeParoquia.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class BaseController {
	@FXML private AnchorPane contentArea;
	@FXML private VBox sideBar;
	
	private final Map<String, Parent> cacheScreens = new HashMap<String, Parent>();
	
	private static BaseController instance;
	
	@FXML
	private void initialize() {
		instance = this;
	}
	
	public static BaseController getInstance() {
		return instance;
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
