package com.paroquiaTeam.sgeParoquia.controller.popup;

import java.util.List;
import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.controller.records.DadosSelecaoConvenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelecaoConvenioController {
	ConvenioService convenioService = new ConvenioService();

	@FXML GridPane gridPrincipal;
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;

	DadosSelecaoConvenio dadosConvenio = null;
	
	@FXML
	private void initialize() {
		btnCadastrar.setVisible(false);
		btnCadastrar.setManaged(false);
		btnCadastrar.setDisable(true);
		
        gridPrincipal.getColumnConstraints().remove(2); 

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
			vBoxCards.getChildren().add(criarCard(c, this::selecionar));
		}
	}

	private void selecionar(DadosSelecaoConvenio dados) {
		this.dadosConvenio = dados;
		
		Stage stage = (Stage) vBoxCards.getScene().getWindow();
		stage.close();
	}
	
	public DadosSelecaoConvenio getDadosConvenio() {
		return dadosConvenio;
	}

	// MOCK
	private VBox criarCard(Convenio c, Consumer<DadosSelecaoConvenio> onSelecionar) {
		VBox card = new VBox();

		HBox linhaPrincipal = new HBox();

		Button btnSelecionar = new Button("Selecionar");

		btnSelecionar.setOnAction(e -> {
			DadosSelecaoConvenio dados = new DadosSelecaoConvenio(
					c.getId(),
					c.getCnpj(),
					c.getNome(),
					c.getCobrancaIndividual());
			
			onSelecionar.accept(dados);
		});

		
		linhaPrincipal.getChildren().addAll(
				new Label(c.getNome()),
				new Label(" "+(c.getVagasCarro()+c.getVagasMoto())),
				btnSelecionar
				);

		card.getChildren().add(linhaPrincipal);
		return card;
	}
}
