package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	private VBox criarCard(Convenio c, Runnable onAcao) {
		VBox card = new VBox();
		
		HBox linhaPrincipal = new HBox();
		Button btnEditar = new Button("Editar");
		btnEditar.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirModal(Popup.EDITARCONVENIO, c.getId());
			
			if (onAcao != null) {
				onAcao.run();
			}
		});
		
		// ativar/desativar
		Button btnAcao;
		if (c.isDesativado()) {
			btnAcao = new Button("Ativar");
			
			btnAcao.setOnAction(e -> {
				if (NavegacaoManager.getInstancia().abrirAlerta(
						"Confirmação", 
						"Tem certeza que deseja ativar o convênio?", 
						AlertType.CONFIRMATION)) {
					try {
						convenioService.ativar(c);
					} catch (Exception e2) {
						if (NavegacaoManager.getInstancia().abrirAlerta(
								"Convênio atrasado", 
								"O convênio não pode ser ativado, pois está com atraso em pagamento. Deseja marcar convênio como atrasado?", 
								AlertType.CONFIRMATION)) {
							convenioService.marcarAtraso(c);
						}
					}
				}
				
				onAcao.run();
			});
		} else {
			btnAcao = new Button("Desativar");
			
			btnAcao.setOnAction(e -> {
				if (NavegacaoManager.getInstancia().abrirAlerta(
						"Confirmação", 
						"Tem certeza que deseja desativar o convênio?", 
						AlertType.CONFIRMATION)) {
					convenioService.desativar(c);
				}
				onAcao.run();
			});
		}
		
		HBox linhaSecundaria = new HBox();
		Button btnPagamento = new Button("Realizar Pagamento");
		
		
		
		Button btnExcluir = new Button("Excluir");
		btnExcluir.setOnAction(e -> {
			if (NavegacaoManager.getInstancia().abrirAlerta(
					"Confirmação", 
					"Tem certeza que deseja excluir o convênio?", 
					AlertType.CONFIRMATION)) {
				try {
					convenioService.excluir(c.getId());
				} catch (Exception e2) {
					NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
				}
			}
			onAcao.run();
		});
		
		linhaPrincipal.getChildren().addAll(
				new Label(c.getNome()), 
				new Label(" "+(c.getVagasCarro()+c.getVagasMoto())),
				btnEditar,
				btnAcao
				);
		
		linhaSecundaria.getChildren().addAll(
				btnPagamento,
				btnExcluir
				);
		
		card.getChildren().addAll(linhaPrincipal, linhaSecundaria);
		return card;
	}
}
