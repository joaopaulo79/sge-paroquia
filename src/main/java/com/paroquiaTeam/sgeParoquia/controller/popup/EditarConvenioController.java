package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosConvenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.service.ConvenioService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarConvenioController {

	Long idConvenio;
	Convenio convenio;

	public EditarConvenioController(Long idConvenio) {
		this.idConvenio = idConvenio;
		convenio = service.obterPorId(idConvenio);
	}

	ConvenioService service = new ConvenioService();

	@FXML TextField textFieldCnpj;
	@FXML TextField textFieldNome;
	@FXML Spinner<Double> spinnerMensalidade;
	@FXML Spinner<Double> spinnerCobrancaIndividual;
	@FXML Spinner<Integer> spinnerVagasCarro;
	@FXML Spinner<Integer> spinnerVagasMoto;
	@FXML DatePicker pickerDataVencimento;
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;

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

		textFieldCnpj.setText(convenio.getCnpj());
		textFieldNome.setText(convenio.getNome());
		pickerDataVencimento.setValue(convenio.getDataVencimento());

		spinnerMensalidade.setValueFactory(
			new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, convenio.getMensalidade(), 0.01)
		);

		spinnerCobrancaIndividual.setValueFactory(
			new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, convenio.getCobrancaIndividual(), 0.01)
		);

		// Temporário
		int vagasConvMax = 100;
		spinnerVagasCarro.setValueFactory(
			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, vagasConvMax, convenio.getVagasCarro())
		);


		spinnerVagasMoto.setValueFactory(
			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, vagasConvMax, convenio.getVagasMoto())
		);

		btnSalvar.setOnAction(e -> {
			try {
				DadosConvenio dadosNovos = new DadosConvenio(
						textFieldCnpj.getText(),
						textFieldNome.getText(),
						spinnerMensalidade.getValue(),
						spinnerVagasCarro.getValue(),
						spinnerVagasMoto.getValue(),
						convenio.getStatus(),
						spinnerCobrancaIndividual.getValue(),
						pickerDataVencimento.getValue()
						);

				service.atualizar(dadosNovos, convenio);

				NavegacaoManager.getInstancia().abrirAlertaSucesso("Editado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();

			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
}
