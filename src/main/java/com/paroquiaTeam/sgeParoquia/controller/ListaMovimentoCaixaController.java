package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.service.CaixaService;
import com.paroquiaTeam.sgeParoquia.service.MovimentoCaixaService;
import com.paroquiaTeam.sgeParoquia.view.factory.MovimentoCaixaCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaMovimentoCaixaController {
	MovimentoCaixaService service = new MovimentoCaixaService();
	
	Long idCaixa;
	Caixa caixa;
	boolean isCaixaAberto;

	public ListaMovimentoCaixaController(Long idCaixa) {
		this.idCaixa = idCaixa;
		caixa = new CaixaService().obterPorId(idCaixa);
		isCaixaAberto = caixa.getDataHoraFechamento() == null;
	}
	
	@FXML GridPane gridPrincipal;
	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;
	
	@FXML
	private void initialize() {
		if (isCaixaAberto) {
			btnCadastrar.setOnAction(e -> {
				NavegacaoManager.getInstancia().abrirModal(Popup.REGISTRARMOVIMENTO);
				carregarMovimentos();
			});
		} 
		else {
			btnCadastrar.setVisible(false);
			btnCadastrar.setManaged(false);
			btnCadastrar.setDisable(true);
			
	        gridPrincipal.getColumnConstraints().remove(2); 
		} 
		
		hBoxLabels.getChildren().addAll(
				new Label("Valor"),
				new Label("Tipo Movimento"),
				new Label(""));
		
		carregarMovimentos();
	}
	
	private void carregarMovimentos() {
		vBoxCards.getChildren().clear();
		List<MovimentoCaixa> movimentos = service.buscarPorCaixa(idCaixa);
		
		for (MovimentoCaixa m : movimentos) {
			vBoxCards.getChildren().add(new MovimentoCaixaCardFactory().
					criarCardExpandivel(m, this::onExcluir, isCaixaAberto));
		}
	}
	
	private void onExcluir(Long idMovimento) {
		if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
			NavegacaoManager.getInstancia().abrirAlertaSemAutorizacao();
		} 
		else {
			if (NavegacaoManager.getInstancia().abrirAlertaConfirmacao("Tem certeza que deseja excluir este movimento?")) {
				service.excluir(idMovimento);
				carregarMovimentos();
			}
		}
	}
}
