package com.paroquiaTeam.sgeParoquia.controller.popup;

import java.util.List;
import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.controller.records.DadosSelecaoCliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.service.ClienteService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelecaoClienteController {
	DadosSelecaoCliente dadosCliente = null;
	
	ClienteService clienteService = new ClienteService();
	
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
        
//        ColumnConstraints colunaBotao = gridPrincipal.getColumnConstraints().get(2);
//		colunaBotao.setMinWidth(0);
//        colunaBotao.setPrefWidth(0);
//        colunaBotao.setMaxWidth(0);
//        colunaBotao.setHgrow(Priority.NEVER);
		
		hBoxLabels.getChildren().addAll(
				new Label("Nome"),
				new Label("Tipo"),
				new Label(""));

		carregarClientes();
	}

	private void carregarClientes() {
		vBoxCards.getChildren().clear();

		List<Cliente> clientes = clienteService.obterTodos();


		for (Cliente c : clientes) {
			vBoxCards.getChildren().add(criarCard(c, this::onSelecionar));
		}
	}
	
	private void onSelecionar(DadosSelecaoCliente dadosCliente) {
		this.dadosCliente = dadosCliente;
		
		Stage stage = (Stage) vBoxCards.getScene().getWindow();
		stage.close();
	}
	
	public DadosSelecaoCliente getDadosCliente() {
		return dadosCliente;
	}
	
	// MOCK
	private VBox criarCard(Cliente c, Consumer<DadosSelecaoCliente> onSelecionar) {
		VBox card = new VBox();

		HBox linhaPrincipal = new HBox();
		HBox linhaSecundaria = new HBox();

		Button btnSelecionar = new Button("Selecionar");
		btnSelecionar.setOnAction(e -> {
			DadosSelecaoCliente dados = new DadosSelecaoCliente(
					c.getId(),
					c.getCpf(),
					c.getNome(),
					c.getTipo()
					);
			
			onSelecionar.accept(dados);
		});
		
		

		linhaPrincipal.getChildren().addAll(
				new Label(c.getNome()),
				new Label(" "+(c.getTipo())),
				btnSelecionar
			);

		card.getChildren().addAll(linhaPrincipal, linhaSecundaria);
		return card;
	}
}
