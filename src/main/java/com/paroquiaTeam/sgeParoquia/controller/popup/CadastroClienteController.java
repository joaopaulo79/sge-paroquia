package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.controller.records.DadosSelecaoConvenio;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.service.ClienteService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CadastroClienteController {
	ClienteService service = new ClienteService();

	@FXML VBox campoDadosMensalista;
	@FXML VBox campoDadosConveniado;
	@FXML VBox vBoxDadosConvenio;
	
	@FXML TextField textFieldCpf;
	@FXML TextField textFieldNome;
	@FXML TextField textFieldTelefone;
	@FXML ChoiceBox<TipoCliente> choiceTipo;
	@FXML DatePicker pickerDataVencimento;
	@FXML Spinner<Double> spinnerMensalidade;
	@FXML Button btnSelecionarConvenio;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	Long idConv = null;

	@FXML
	private void initialize() {
		campoDadosMensalista.setVisible(false);
		campoDadosMensalista.setManaged(false);
		
		campoDadosConveniado.setVisible(false);
		campoDadosConveniado.setManaged(false);
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});

		choiceTipo.getItems().setAll(TipoCliente.values());
		choiceTipo.setValue(TipoCliente.AVULSO);
		choiceTipo.setOnAction(e -> onAlterarTipo());
		
		spinnerMensalidade.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01)
			);
		spinnerMensalidade.setEditable(true);
		
		btnSelecionarConvenio.setOnAction(e -> {
			SelecaoConvenioController sCC = NavegacaoManager.getInstancia().abrirModal(Popup.SELECAOCONVENIO);
			DadosSelecaoConvenio dados = sCC.getDadosConvenio();
			
			carregarConvenio(dados);
			
			Stage stage = (Stage) vBoxDadosConvenio.getScene().getWindow();
			Platform.runLater(stage::sizeToScene);
		});
		
		btnSalvar.setOnAction(e -> {
			try {
				if (idConv == null && choiceTipo.getValue() == TipoCliente.CONVENIADO) {
					throw new IllegalStateException("Selecione um convênio");
				}
				
				DadosCliente dados = new DadosCliente(
						textFieldNome.getText(),
						textFieldCpf.getText(),
						textFieldTelefone.getText(),
						choiceTipo.getValue(),
						StatusCliente.DESATIVADO,
						pickerDataVencimento.getValue(),
						spinnerMensalidade.getValue(),
						idConv);
				
				service.salvar(dados);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();

			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
	
	private void carregarConvenio(DadosSelecaoConvenio dados) {
		idConv = dados.id();
		
		vBoxDadosConvenio.getChildren().clear();
		
		// Temporário
		// Substituir por card builder
		vBoxDadosConvenio.getChildren().addAll(
				new Label("ID: " + dados.id()),
				new Label("CNPJ: " + dados.cnpj()),
				new Label("Nome: " + dados.nome()),
				new Label("Cobrança individual: R$" + dados.cobrancaIndividual())
				);
	}
	
	private void onAlterarTipo() {
		boolean isMensalista;
		boolean isConveniado;
		
		TipoCliente tipo = choiceTipo.getValue();
		if (tipo == TipoCliente.MENSALISTA) {
			isMensalista = true;
			isConveniado = false;
		} else if (tipo == TipoCliente.CONVENIADO) {
			isMensalista = false;
			isConveniado = true;
		} else {
			isMensalista = false;
			isConveniado = false;
		}

		campoDadosMensalista.setVisible(isMensalista);
		campoDadosMensalista.setManaged(isMensalista);
		
		campoDadosConveniado.setVisible(isConveniado);
		campoDadosConveniado.setManaged(isConveniado);
		
		if (!isConveniado) {
			idConv = null;
		}
		
		Stage stage = (Stage) campoDadosConveniado.getScene().getWindow();
		Platform.runLater(stage::sizeToScene);
	}
}
