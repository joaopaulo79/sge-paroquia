package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.service.ClienteService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
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
	
	@FXML TextField textFieldCpf;
	@FXML TextField textFieldNome;
	@FXML TextField textFieldTelefone;
	@FXML ChoiceBox<TipoCliente> choiceTipo;
	@FXML DatePicker pickerDataVencimento;
	@FXML Spinner<Double> spinnerMensalidade;
	@FXML Spinner<Integer> spinnerConvenio;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;

	@FXML
	private void initialize() {
		boolean isMensalista;
		boolean isConveniado;
		
		TipoCliente tipo = cliente.getTipo();
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
		
		textFieldNome.setText(cliente.getNome());
		textFieldCpf.setText(cliente.getCpf());
		textFieldTelefone.setText(cliente.getTelefone());
		choiceTipo.setValue(cliente.getTipo());
		pickerDataVencimento.setValue(cliente.getDataVencimentoMensalidade());
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});

		choiceTipo.getItems().setAll(TipoCliente.values());
		choiceTipo.setOnAction(e -> ativarCamposEspecificos());
		
		spinnerMensalidade.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, cliente.getValorMensalidade(), 0.01)
			);
		
		int idConv = 0;
		if (cliente.getConvenio() != null) {
			idConv = Math.toIntExact(cliente.getConvenio().getId());
		}
		spinnerConvenio.setValueFactory(
				new IntegerSpinnerValueFactory(0, 1000, idConv)
			);
		
		btnSalvar.setOnAction(e -> {
			try {
				DadosCliente dadosNovos = new DadosCliente(
						textFieldNome.getText(),
						textFieldCpf.getText(),
						textFieldTelefone.getText(),
						choiceTipo.getValue(),
						cliente.getStatus(),
						pickerDataVencimento.getValue(),
						spinnerMensalidade.getValue(),
						(long) spinnerConvenio.getValue());
				
				service.atualizar(dadosNovos, cliente);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Editado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();

			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
	
	private void ativarCamposEspecificos() {
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
		
		Stage stage = (Stage) campoDadosConveniado.getScene().getWindow();
		Platform.runLater(stage::sizeToScene);
	}
}
