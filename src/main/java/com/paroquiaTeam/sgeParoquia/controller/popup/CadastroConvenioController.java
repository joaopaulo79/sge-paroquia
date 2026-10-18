package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosConvenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusConvenio;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroConvenioController {
	ConvenioService service = new ConvenioService();
	
	@FXML TextField textFieldCnpj;
	@FXML TextField textFieldNome;
	@FXML Spinner<Double> spinnerMensalidade;
	@FXML Spinner<Double> spinnerCobrancaIndividual;
	@FXML Spinner<Integer> spinnerVagasCarro;
	@FXML Spinner<Integer> spinnerVagasMoto;
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	@FXML DatePicker pickerDataVencimento;
	
	@FXML 
	private void initialize() {
		spinnerMensalidade.setEditable(true);
		spinnerCobrancaIndividual.setEditable(true);
		spinnerVagasCarro.setEditable(true);
		spinnerVagasMoto.setEditable(true);
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});
		
		spinnerMensalidade.setValueFactory(
			new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01)
		);
		
		spinnerCobrancaIndividual.setValueFactory(
			new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01)
		);

		// Temporário
		int vagasConvMax = 100;
		spinnerVagasCarro.setValueFactory(
			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, vagasConvMax, 0)
		);

		
		spinnerVagasMoto.setValueFactory(
			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, vagasConvMax, 0)
		);
		
		btnSalvar.setOnAction(e -> {
			try {
				DadosConvenio dados = new DadosConvenio(
						textFieldCnpj.getText(),
						textFieldNome.getText(),
						spinnerMensalidade.getValue(),
						spinnerVagasCarro.getValue(),
						spinnerVagasMoto.getValue(),
						StatusConvenio.DESATIVADO,
						spinnerCobrancaIndividual.getValue(),
						pickerDataVencimento.getValue()
						);
				
				service.salvar(dados);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
				
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
}
