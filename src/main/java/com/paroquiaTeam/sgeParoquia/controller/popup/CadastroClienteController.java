package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusCliente;
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


public class CadastroClienteController {
	ClienteService service = new ClienteService();

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
		choiceTipo.setOnAction(e -> ativarCamposEspecificos());
		
		spinnerMensalidade.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01)
			);
		
		spinnerConvenio.setValueFactory(
				new IntegerSpinnerValueFactory(0, 1000)
			);
		
		btnSalvar.setOnAction(e -> {
			try {
				DadosCliente dados = new DadosCliente(
						textFieldNome.getText(),
						textFieldCpf.getText(),
						textFieldTelefone.getText(),
						choiceTipo.getValue(),
						StatusCliente.DESATIVADO,
						pickerDataVencimento.getValue(),
						spinnerMensalidade.getValue(),
						(long) spinnerConvenio.getValue());
				
				service.salvar(dados);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
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
