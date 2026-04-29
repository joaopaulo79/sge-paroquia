package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.TestesDeEntidades;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TesteController {
	
	@FXML
	private Button btnTesta;
	
	@FXML
	public void onBtnTestaAction() {
		TestesDeEntidades.testaEntidades();
	}
}
