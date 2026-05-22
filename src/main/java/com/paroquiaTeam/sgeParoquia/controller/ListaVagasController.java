package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.dao.VagaDAO;
import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ListaVagasController {
	@FXML TextField vagasCarroComum;
	@FXML TextField vagasMotoComum;
	@FXML Button btnSalvar;
	
	@FXML
	private void initialize() {
		btnSalvar.setOnAction(e -> {
			//números de vaga, tipoReserva
			long carroComum = Long.parseLong(vagasCarroComum.getText());
			long motoComum = Long.parseLong(vagasMotoComum.getText());
			
			//Atualizando Vagas
			VagaDAO dao = new VagaDAO();
			dao.ajustarVagas(List.of(
					new VagaDAO.AjusteVaga(TipoVaga.CARRO, TipoReservaVaga.COMUM, carroComum),
					new VagaDAO.AjusteVaga(TipoVaga.MOTO, TipoReservaVaga.COMUM, motoComum)
					));
		});
	}
}
