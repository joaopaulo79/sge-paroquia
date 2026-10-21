package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.controller.records.AcoesConvenio;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
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
		
		AcoesConvenio acoes = new AcoesConvenio(
				this::editar,
				this::excluir,
				this::ativar,
				this::desativar,
				this::registrarPagamento
				);
		
		for (Convenio c : convenios) {
			vBoxCards.getChildren().add(criarCard(c, acoes));
		}
	}
	
	private void editar(Long id) {
		NavegacaoManager.getInstancia().abrirModal(Popup.EDITARCONVENIO, id);
		carregarConvenios();
	}
	
	private void ativar(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação", 
				"Tem certeza que deseja ativar o convênio?", 
				AlertType.CONFIRMATION)) {
			try {
				convenioService.ativar(id);
			} catch (Exception e2) {
				if (NavegacaoManager.getInstancia().abrirAlerta(
						"Convênio atrasado", 
						"O convênio não pode ser ativado, pois está com atraso em pagamento. Deseja marcar convênio como atrasado?", 
						AlertType.CONFIRMATION)) {
					convenioService.marcarAtraso(id);
				}
			}
		}
		
		carregarConvenios();
	}
	
	private void desativar(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação", 
				"Tem certeza que deseja desativar o convênio?", 
				AlertType.CONFIRMATION)) {
			convenioService.desativar(id);
		}
		
		carregarConvenios();
	}

	private void excluir(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação", 
				"Tem certeza que deseja excluir o convênio?", 
				AlertType.CONFIRMATION)) {
			try {
				convenioService.excluir(id);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		}
		
		carregarConvenios();
	}

	private void registrarPagamento(Long idConvenio) {
		NavegacaoManager.getInstancia().abrirModal(Popup.REGISTRARMOVIMENTO, TipoMovimento.ENTRADA);
		
		// TODO: ver a melhor forma de adiar a data de vencimento
		
		carregarConvenios();
	}
	// MOCK
	private VBox criarCard(Convenio c, AcoesConvenio acoes) {
		VBox card = new VBox();
		
		HBox linhaPrincipal = new HBox();
		HBox linhaSecundaria = new HBox();
		
		Button btnEditar = new Button("Editar");
		Button btnAlterarStatus;
		Button btnPagamento = new Button("Realizar Pagamento");
		Button btnExcluir = new Button("Excluir");

		btnEditar.setOnAction(e -> acoes.onEditar().accept(c.getId()));
		
		if (c.isDesativado()) {	
			btnAlterarStatus = new Button("Ativar");
			btnAlterarStatus.setOnAction(e -> acoes.onAtivar().accept(c.getId()) );
		} else {
			btnAlterarStatus = new Button("Desativar");
			btnAlterarStatus.setOnAction(e -> acoes.onDesativar().accept(c.getId()) );

		}

		btnPagamento.setOnAction(e -> acoes.onRegistrarPagamento().accept(c.getId()) );
		btnExcluir.setOnAction(e -> acoes.onExcluir().accept(c.getId()) );
		
		linhaPrincipal.getChildren().addAll(
				new Label(c.getNome()), 
				new Label(" "+(c.getVagasCarro()+c.getVagasMoto())),
				btnEditar,
				btnAlterarStatus
				);
		
		linhaSecundaria.getChildren().addAll(
				btnPagamento,
				btnExcluir
				);
		
		card.getChildren().addAll(linhaPrincipal, linhaSecundaria);
		return card;
	}
}
