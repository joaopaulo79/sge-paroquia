package com.paroquiaTeam.sgeParoquia.controller.popup;


import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPagamento;
import com.paroquiaTeam.sgeParoquia.service.MovimentoCaixaService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RegistrarMovimentoCaixaController {
	private final DadosMovimentoCaixa dadosImutaveis;

	public RegistrarMovimentoCaixaController(DadosMovimentoCaixa dadosImutaveis) {
		this.dadosImutaveis = dadosImutaveis;
	}

	MovimentoCaixaService service = new MovimentoCaixaService();

	@FXML Spinner<Double> spinnerValor;
	@FXML ChoiceBox<TipoPagamento> choiceFormaPagamento;
	@FXML ChoiceBox<TipoMovimento> choiceTipoMovimento;
	@FXML TextArea textAreaDescricao;
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;

	@FXML
	private void initialize() {
		double valorInicial;

		if (dadosImutaveis.valor() == null) {
			valorInicial = 0;
			spinnerValor.setEditable(true);
		} else {
			valorInicial = dadosImutaveis.valor();
			spinnerValor.setDisable(true);
		}
		spinnerValor.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000000, 1000000, valorInicial, 0.01)
			);


		choiceFormaPagamento.getItems().addAll(TipoPagamento.values());
		if (dadosImutaveis.formaPagamento() == null) {
			choiceFormaPagamento.setValue(TipoPagamento.CREDITO);
		} else {
			choiceFormaPagamento.setValue(dadosImutaveis.formaPagamento());
			choiceFormaPagamento.setDisable(true);
		}


		choiceTipoMovimento.getItems().addAll(TipoMovimento.values());
		if (dadosImutaveis.tipoMovimento() == null) {
			choiceTipoMovimento.setValue(TipoMovimento.ENTRADA);
		} else {
			choiceTipoMovimento.setValue(dadosImutaveis.tipoMovimento());
			choiceTipoMovimento.setDisable(true);
		}

		if (dadosImutaveis.descricao() != null && !dadosImutaveis.descricao().isBlank()) {
			textAreaDescricao.setText(dadosImutaveis.descricao());
			textAreaDescricao.setDisable(true);
		}

		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});

		btnSalvar.setOnAction(e -> {
			try {
				if (!SessaoSistema.getInstancia().temCaixaAberto()) {
					throw new IllegalStateException("É necessário ter um caixa aberto para registrar um movimento");
				}

				DadosMovimentoCaixa dados = new DadosMovimentoCaixa(
						spinnerValor.getValue(),
						choiceFormaPagamento.getValue(),
						choiceTipoMovimento.getValue(),
						textAreaDescricao.getText(),
						SessaoSistema.getInstancia().getCaixa().getId(),
						null);

				service.salvar(dados);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Registrado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();

			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
}
