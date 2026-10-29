package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.controller.records.DadosSelecaoConvenio;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
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


public class EditarClienteController {
	ClienteService service = new ClienteService();

	Long idCliente;
	Cliente cliente;
	public EditarClienteController(Long idCliente) {
		this.idCliente = idCliente;
		cliente = service.obterPorId(idCliente);
	}
	
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
		textFieldNome.setText(cliente.getNome());
		textFieldCpf.setText(cliente.getCpf());
		textFieldTelefone.setText(cliente.getTelefone());
		choiceTipo.setValue(cliente.getTipo());
		pickerDataVencimento.setValue(cliente.getDataVencimentoMensalidade());
		
		onAlterarTipo();
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});

		choiceTipo.getItems().setAll(TipoCliente.values());
		choiceTipo.setOnAction(e -> {
			onAlterarTipo();
			
			Stage stage = (Stage) campoDadosConveniado.getScene().getWindow();
			Platform.runLater(stage::sizeToScene);
		});
		
		spinnerMensalidade.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, cliente.getValorMensalidade(), 0.01)
			);
		spinnerMensalidade.setEditable(true);
		
		btnSelecionarConvenio.setOnAction(e -> {
			SelecaoConvenioController sCC = NavegacaoManager.getInstancia().abrirModal(Popup.SELECAOCONVENIO);
			DadosSelecaoConvenio dadosNovos = sCC.getDadosConvenio();
			
			carregarConvenio(dadosNovos);
			
			Stage stage = (Stage) vBoxDadosConvenio.getScene().getWindow();
			Platform.runLater(stage::sizeToScene);
		});
		
		btnSalvar.setOnAction(e -> {
			try {
				if (idConv == null && choiceTipo.getValue() == TipoCliente.CONVENIADO) {
					throw new IllegalStateException("Selecione um convênio");
				}
				
				DadosCliente dadosNovos = new DadosCliente(
						textFieldNome.getText(),
						textFieldCpf.getText(),
						textFieldTelefone.getText(),
						choiceTipo.getValue(),
						cliente.getStatus(),
						pickerDataVencimento.getValue(),
						spinnerMensalidade.getValue(),
						idConv);
				
				service.atualizar(dadosNovos, cliente);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Editado com sucesso!");
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
		
		if (isConveniado && cliente.getConvenio() != null) {
			Convenio c = cliente.getConvenio();
			DadosSelecaoConvenio dados = new DadosSelecaoConvenio(
					c.getId(),
					c.getCnpj(),
					c.getNome(),
					c.getCobrancaIndividual());
			
			carregarConvenio(dados);
		}
		
		if (!isConveniado) {
			idConv = null;
		}
	}
}
