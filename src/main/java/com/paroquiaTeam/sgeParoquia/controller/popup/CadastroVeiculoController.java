package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosVeiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.service.VeiculoService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;

public class CadastroVeiculoController {
	VeiculoService service = new VeiculoService();
	
	@FXML TextField textFieldPlaca;
	@FXML TextField textFieldMarca;
	@FXML TextField textFieldModelo;
	@FXML Spinner<Integer> spinnerAno;	
	@FXML TextField textFieldCor;
	@FXML TextArea textAreaObservacoes;
	
	@FXML ToggleGroup grupoTipoVeiculo;
	@FXML RadioButton radioCarro;
	@FXML RadioButton radioMoto;
	
	@FXML Spinner<Integer> spinnerIdCliente;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});
		
		spinnerAno.setValueFactory(
				new IntegerSpinnerValueFactory(1900, 9999, 2000)
			);
		spinnerAno.setEditable(true);
		
		spinnerIdCliente.setValueFactory(
				new IntegerSpinnerValueFactory(0, 1000)
			);
		spinnerIdCliente.setEditable(true);
		
		radioCarro.setSelected(true);
		radioCarro.setUserData(TipoVeiculo.CARRO);
		
		radioMoto.setUserData(TipoVeiculo.MOTO);
		
		btnSalvar.setOnAction(e -> {
			try {
				TipoVeiculo tipo = obterTipoVeiculoSelecionado();
				
				DadosVeiculo dados = new DadosVeiculo(
						textFieldPlaca.getText(),
						textFieldMarca.getText(),
						textFieldModelo.getText(),
						spinnerAno.getValue(),
						textFieldCor.getText(),
						textAreaObservacoes.getText(),
						tipo,
						spinnerIdCliente.getValue());
				
				service.salvar(dados);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Cadastrado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
	
	public TipoVeiculo obterTipoVeiculoSelecionado() {
	    Toggle selectedToggle = grupoTipoVeiculo.getSelectedToggle();
	    
	    if (selectedToggle != null && selectedToggle.getUserData() instanceof TipoVeiculo) {
	        return (TipoVeiculo) selectedToggle.getUserData();
	    }
	    
	    throw new IllegalStateException("Nenhum tipo de veículo foi selecionado.");
	}
}
