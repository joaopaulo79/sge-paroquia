package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusConvenio;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaConvenioController {
	ConvenioService convenioService = new ConvenioService();
	
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;
	
	@FXML
	private void initialize() {
		btnCadastrar.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirModal(Popup.CADASTRARCONVENIO);
			carregarConvenios();
		});
		
		hBoxLabels.getChildren().addAll(
				new Label("Nome"),
				new Label("Vagas Contratadas"),
				new Label("Ações"));
		
		carregarConvenios();
	}
	
	private void carregarConvenios() {
		vBoxCards.getChildren().clear();
		
		List<Convenio> convenios = convenioService.obterTodos();
		for (Convenio c : convenios) {
			vBoxCards.getChildren().add(criarCard(c, this::carregarConvenios));
		}
	}
	
	// MOCK
	private HBox criarCard(Convenio c, Runnable onAcao) {
		HBox card = new HBox();
		Button btnEditar = new Button("Editar");
		btnEditar.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirModal(Popup.EDITARCONVENIO, c.getId());
			
			if (onAcao != null) {
				onAcao.run();
			}
		});
		
		card.getChildren().addAll(
				new Label(c.getNome()), 
				new Label(" "+(c.getVagasCarro()+c.getVagasMoto())),
				btnEditar
				);
		return card;
	}
}
