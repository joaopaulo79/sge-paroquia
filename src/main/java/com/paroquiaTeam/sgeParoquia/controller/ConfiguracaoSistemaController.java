package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.model.enums.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;
import com.paroquiaTeam.sgeParoquia.view.factory.PainelPrecificacaoFracionadaFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.PainelPrecificacaoPorHoraFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConfiguracaoSistemaController {

    @FXML
    private ChoiceBox<TipoPrecificacao> escolhaTipoPrecificacao;

    @FXML
    private VBox containerCamposPrecificacao;

    private final PrecificacaoController controllerPrecificacao =
            new PrecificacaoController();

    @FXML
    public void initialize() {

        escolhaTipoPrecificacao.getItems().addAll(TipoPrecificacao.values());

        escolhaTipoPrecificacao.setValue(TipoPrecificacao.FRACIONADA);

        escolhaTipoPrecificacao.setOnAction(e -> trocarPainelPrecificacao());

        trocarPainelPrecificacao();
    }

    private void trocarPainelPrecificacao() {

        TipoPrecificacao tipo = escolhaTipoPrecificacao.getValue();

        PainelPrecificacao painel = switch (tipo) {

            case POR_HORA ->
                    new PainelPrecificacaoPorHoraFactory(controllerPrecificacao)
                            .criar();

            case FRACIONADA ->
                    new PainelPrecificacaoFracionadaFactory(controllerPrecificacao)
                            .criar();
        };

        VBox.setVgrow(painel, Priority.ALWAYS);

        painel.setMaxWidth(Double.MAX_VALUE);

        containerCamposPrecificacao.getChildren().setAll(painel);
    }
}