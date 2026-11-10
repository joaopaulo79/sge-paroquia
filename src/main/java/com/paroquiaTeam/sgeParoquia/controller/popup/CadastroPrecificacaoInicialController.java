package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosPrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class CadastroPrecificacaoInicialController {
	PrecificacaoService service = new PrecificacaoService();
	private boolean cadastrou = false;
	
	@FXML Spinner<Integer> spinnerTolerancia;
	@FXML Spinner<Double> spinnerMeiaHora;
	@FXML Spinner<Double> spinnerHora;
	@FXML Spinner<Double> spinnerDiaria;
	@FXML Spinner<Double> spinnerMeiaHoraMoto;
	@FXML Spinner<Double> spinnerHoraMoto;
	@FXML Spinner<Double> spinnerDiariaMoto;
	
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		spinnerTolerancia.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0));
		
		spinnerMeiaHora.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		spinnerHora.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		spinnerDiaria.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		spinnerMeiaHoraMoto.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		spinnerHoraMoto.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		spinnerDiariaMoto.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 0.01));
		
		btnSalvar.setOnAction(e -> {
			try {
				DadosPrecificacaoFracionada dados = new DadosPrecificacaoFracionada(
						spinnerTolerancia.getValue(),
						spinnerMeiaHora.getValue(),
						spinnerHora.getValue(),
						spinnerDiaria.getValue(),
						spinnerMeiaHoraMoto.getValue(),
						spinnerHoraMoto.getValue(),
						spinnerDiariaMoto.getValue());
				
				service.salvarFracionada(dados);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				cadastrou = true;
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
				e2.printStackTrace();
			}
		});
	}
	
	public boolean getCadastrou() {
		return cadastrou;
	}
}
