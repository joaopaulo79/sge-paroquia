package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;
import com.paroquiaTeam.sgeParoquia.service.VagaService;
import com.paroquiaTeam.sgeParoquia.service.VagaService.AjusteVaga;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class AlterarVagasController {
	private enum AcaoVaga {
		RESERVAR("Reservar vagas"),
		BLOQUEAR("Bloquear vagas"),
		LIBERAR("Liberar vagas"),
		ADICIONAR("Adicionar vagas");
		
		private String label;
		
		private AcaoVaga(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() { return label; }
	}
	
	VagaService service = new VagaService();
	NavegacaoManager navegacao = NavegacaoManager.getInstancia();
	
	@FXML ChoiceBox<AcaoVaga> choiceAcao;
	@FXML CheckBox checkLiberarTudo;
	@FXML Button btnSalvar;
	
	@FXML ChoiceBox<TipoVaga> choiceTipo;
	@FXML ChoiceBox<TipoReservaVaga> choiceReserva;
	@FXML Spinner<Integer> spinQuantidade;
	
	@FXML Label labelStatus;
	@FXML ChoiceBox<StatusVaga> choiceStatus;
	
	@FXML
	private void initialize() {
		choiceAcao.getItems().addAll(AcaoVaga.values());
		choiceAcao.setValue(AcaoVaga.ADICIONAR);
		
		choiceTipo.getItems().addAll(TipoVaga.values());
		choiceTipo.setValue(TipoVaga.CARRO);
		
		choiceReserva.getItems().addAll(TipoReservaVaga.values());
		choiceReserva.setValue(TipoReservaVaga.COMUM);
		
		choiceAcao.setOnAction(e -> lidarMudancaAcao());
		
		spinQuantidade.setValueFactory(
			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1)
		);
		
		checkLiberarTudo.setOnAction(e -> {
			if (checkLiberarTudo.isSelected()) {
				choiceTipo.setDisable(true);
				choiceReserva.setDisable(true);
				spinQuantidade.setDisable(true);
			} else {
				choiceTipo.setDisable(false);
				choiceReserva.setDisable(false);
				spinQuantidade.setDisable(false);
			}
		});
		
		btnSalvar.setOnAction(e -> {
			try {
				AcaoVaga acao = choiceAcao.getValue();
	
				int vagasParaAlterar = spinQuantidade.getValue();
				TipoVaga tipo = choiceTipo.getValue();
				TipoReservaVaga reserva = choiceReserva.getValue();
				
				StatusVaga status = null;
				if (!choiceStatus.isDisabled() && choiceStatus.getValue() != null) {
					status = choiceStatus.getValue();
				}
				
				AjusteVaga ajuste = new AjusteVaga(tipo, reserva, vagasParaAlterar);
				
					
				long vagasAfetadas = switch (acao) {
					case RESERVAR -> service.reduzirVagas(ajuste, StatusVaga.RESERVADAEVENTO);
					case BLOQUEAR -> service.reduzirVagas(ajuste, StatusVaga.BLOQUEADA);
					case LIBERAR -> (checkLiberarTudo.isSelected()) ?
								service.liberarTodasAsVagas(status) :							
								service.liberarVagas(ajuste, status);
						
					
					case ADICIONAR -> service.adicionarVagas(ajuste);
				};
				
				navegacao.abrirAlertaSucesso(vagasAfetadas + " vagas afetadas");
				
				Stage stageAtual = (Stage) btnSalvar.getScene().getWindow();
				stageAtual.close();
			} catch (Exception e2) {
				navegacao.abrirAlertaErro("Erro ao realizar ação: " + e2.getLocalizedMessage());
			}
		});
		
		lidarMudancaAcao();
	}
	
	private void lidarMudancaAcao() {
		AcaoVaga acao = choiceAcao.getValue();
		
		if (acao == null) {
			return;
		}
		
		if (acao == AcaoVaga.LIBERAR) {
			labelStatus.setText("Status antigo");
			labelStatus.setDisable(false);
			
			choiceStatus.getItems().setAll(StatusVaga.RESERVADAEVENTO, StatusVaga.BLOQUEADA);
			choiceStatus.setValue(StatusVaga.RESERVADAEVENTO);
			choiceStatus.setDisable(false);
			
			checkLiberarTudo.setVisible(true);
			checkLiberarTudo.setManaged(true);
		} else {
			labelStatus.setText("...");
			labelStatus.setDisable(true);
			
			choiceStatus.getItems().clear();
			choiceStatus.setDisable(true);
			
			checkLiberarTudo.setVisible(false);
			checkLiberarTudo.setManaged(false);
		}
	}
}
