package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.controller.records.DadosSelecaoCliente;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosVeiculo;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.service.VeiculoService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditarVeiculoController {
	VeiculoService service = new VeiculoService();
	
	Long idVeiculo;
	Veiculo veiculo;
	
	public EditarVeiculoController(Long idVeiculo) {
		this.idVeiculo = idVeiculo;
		veiculo = service.obterPorId(idVeiculo);
	}
	
	@FXML TextField textFieldPlaca;
	@FXML TextField textFieldMarca;
	@FXML TextField textFieldModelo;
	@FXML Spinner<Integer> spinnerAno;	
	@FXML TextField textFieldCor;
	@FXML TextArea textAreaObservacoes;
	
	@FXML ToggleGroup grupoTipoVeiculo;
	@FXML RadioButton radioCarro;
	@FXML RadioButton radioMoto;
	
	@FXML VBox vBoxDadosCliente;
	@FXML Button btnSelecionarCliente;
	
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	
	Long idCliente;
	
	@FXML
	private void initialize() {
		textFieldPlaca.setText(veiculo.getPlaca());
		textFieldMarca.setText(veiculo.getMarca());
		textFieldModelo.setText(veiculo.getModelo());
		textFieldCor.setText(veiculo.getCor());
		textAreaObservacoes.setText(veiculo.getObservacoes());
		
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		});
		
		spinnerAno.setValueFactory(
				new IntegerSpinnerValueFactory(1900, 9999, veiculo.getAno())
			);
		spinnerAno.setEditable(true);
		
		if (veiculo.getTipo() == TipoVeiculo.CARRO) {
			radioCarro.setSelected(true);
		} else {
			radioMoto.setSelected(true);
		}
		
		radioCarro.setUserData(TipoVeiculo.CARRO);
		
		radioMoto.setUserData(TipoVeiculo.MOTO);
		
		Cliente c = veiculo.getCliente();
		DadosSelecaoCliente dadosCliente = new DadosSelecaoCliente(
				c.getId(),
				c.getCpf(),
				c.getNome(),
				c.getTipo());
		carregarCliente(dadosCliente);
		
		btnSelecionarCliente.setOnAction(e -> {
			SelecaoClienteController sCC = NavegacaoManager.getInstancia().abrirModal(Popup.SELECAOCLIENTE);
			DadosSelecaoCliente dadosNovoCliente = sCC.getDadosCliente();
			
			carregarCliente(dadosNovoCliente);
			
			Stage stage = (Stage) vBoxDadosCliente.getScene().getWindow();
			Platform.runLater(stage::sizeToScene);
		});
		
		btnSalvar.setOnAction(e -> {
			try {
				TipoVeiculo tipo = obterTipoVeiculoSelecionado();
				
				if (idCliente == null) {
					throw new IllegalStateException("Selecione um cliente");
				}
				
				DadosVeiculo dadosNovos = new DadosVeiculo(
						textFieldPlaca.getText(),
						textFieldMarca.getText(),
						textFieldModelo.getText(),
						spinnerAno.getValue(),
						textFieldCor.getText(),
						textAreaObservacoes.getText(),
						tipo,
						idCliente);
				
				service.atualizar(dadosNovos, veiculo);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Editado com sucesso!");
				Stage stage = (Stage) btnSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		});
	}
	
	private void carregarCliente(DadosSelecaoCliente dados) {
		idCliente = dados.id();
		
		vBoxDadosCliente.getChildren().clear();
		
		// Temporário
		// Substituir por card builder
		vBoxDadosCliente.getChildren().addAll(
				new Label("ID: " + dados.id()),
				new Label("CPF: " + dados.cpf()),
				new Label("Nome: " + dados.nome()),
				new Label("Tipo: " + dados.tipo().toString())
				);
	}
	
	private TipoVeiculo obterTipoVeiculoSelecionado() {
	    Toggle selectedToggle = grupoTipoVeiculo.getSelectedToggle();
	    
	    if (selectedToggle != null && selectedToggle.getUserData() instanceof TipoVeiculo) {
	        return (TipoVeiculo) selectedToggle.getUserData();
	    }
	    
	    throw new IllegalStateException("Nenhum tipo de veículo foi selecionado.");
	}
}
