package com.paroquiaTeam.sgeParoquia.controller.popup;

import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelecionarTipoVeiculoController {
	private TipoVaga tipoSelecionado; 
	
	@FXML VBox container;

    @FXML
    private void selecionarCarro() {
        this.tipoSelecionado = TipoVaga.CARRO;
        fecharJanela();
    }

    @FXML
    private void selecionarMoto() {
        this.tipoSelecionado = TipoVaga.MOTO;
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.close();
    }

    public TipoVaga getTipoSelecionado() {
        return tipoSelecionado;
    }
}
