package com.paroquiaTeam.sgeParoquia.controller;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.dao.VagaDAO;
import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.factory.VagaCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class ListaVagasController {
	@FXML TextField vagasCarroComum;
	@FXML TextField vagasMotoComum;
	@FXML Button btnSalvar;
	@FXML FlowPane paneVagas;
	@FXML AnchorPane root;
	
	@FXML
	private void initialize() {
		btnSalvar.setOnAction(e -> {
			VagaDAO dao = new VagaDAO();
			//números de vaga, tipoReserva
			long carroComum = Long.parseLong(vagasCarroComum.getText());
			long motoComum = Long.parseLong(vagasMotoComum.getText());
			
			//Atualizando Vagas
			dao.ajustarVagas(List.of(
					new VagaDAO.AjusteVaga(TipoVaga.CARRO, TipoReservaVaga.COMUM, carroComum),
					new VagaDAO.AjusteVaga(TipoVaga.MOTO, TipoReservaVaga.COMUM, motoComum)
			));
			atualizarCards();
		});
		
		atualizarCards();
	}
	
	public void atualizarCards() {
		paneVagas.getChildren().clear();
		
		VagaDAO dao = new VagaDAO();
		VagaCardFactory cardFactory = new VagaCardFactory();
		
		dao.getAll().forEach(v -> {			
			CardGenerico card = cardFactory.criarCard(v);
			paneVagas.getChildren().add(card);
		});
	}
}
