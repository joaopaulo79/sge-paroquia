package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Tela;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.service.CaixaService;
import com.paroquiaTeam.sgeParoquia.view.factory.CaixaCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaCaixaController {
	CaixaService service = new CaixaService();
	
	@FXML GridPane gridPrincipal;
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;
	
	@FXML
	private void initialize() {
		btnCadastrar.setVisible(false);
		btnCadastrar.setManaged(false);
		btnCadastrar.setDisable(true);
		
        gridPrincipal.getColumnConstraints().remove(2); 
		
		hBoxLabels.getChildren().addAll(
				new Label("Abertura"),
				new Label("Fechamento"),
				new Label(""));
		
		carregarCaixas();
	}
	
	private void carregarCaixas() {
		vBoxCards.getChildren().clear();
		List<Caixa> caixas = service.obterTodos();
		
		for (Caixa c : caixas) {
			vBoxCards.getChildren().add(new CaixaCardFactory().criarCard(c, this::exibirTelaMovimentos));
		}
	}
	
	private void exibirTelaMovimentos(Long idCaixa) {
		NavegacaoManager.getInstancia().navegarPara(Tela.MOVIMENTOS, idCaixa);
	}
}
