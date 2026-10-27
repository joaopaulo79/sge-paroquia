package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;
import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.service.VeiculoService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaVeiculoController {
	VeiculoService service = new VeiculoService();
	
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;
	
	@FXML 
	private void initialize() {
		btnCadastrar.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirModal(Popup.CADASTRARVEICULO);
			carregarVeiculos();
		});

		hBoxLabels.getChildren().addAll(
				new Label("Placa"),
				new Label("Marca/Modelo"),
				new Label("Ações"));
		
		carregarVeiculos();
	}
	
	private void onEditar(Long id) {
		NavegacaoManager.getInstancia().abrirModal(Popup.EDITARVEICULO, id);
		carregarVeiculos();
	}
	
	private void onExcluir(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação",
				"Tem certeza que deseja excluir o Veículo?",
				AlertType.CONFIRMATION)) {
			try {
				service.excluir(id);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		}
		carregarVeiculos();
	}
	
	private void carregarVeiculos() {
		vBoxCards.getChildren().clear();
		List<Veiculo> veiculos = service.obterTodos();
		
		for (Veiculo v : veiculos) {
			vBoxCards.getChildren().add(criarCard(v, this::onEditar, this::onExcluir));
		}
	}
	
	private VBox criarCard(Veiculo v, Consumer<Long> onEditar, Consumer<Long> onExcluir) {
		VBox card = new VBox();
		HBox linhaPrincipal = new HBox();

		Button btnEditar = new Button("Editar");
		Button btnExcluir = new Button("Excluir");

		btnEditar.setOnAction(e -> onEditar.accept(v.getId()));
		btnExcluir.setOnAction(e -> onExcluir.accept(v.getId()));

		linhaPrincipal.getChildren().addAll(
				new Label(v.getPlaca()),
				new Label(" "+v.getMarca()+"/"+v.getModelo()),
				btnEditar,
				btnExcluir
				);
		
		card.getChildren().add(linhaPrincipal);
		return card;
	}
}
