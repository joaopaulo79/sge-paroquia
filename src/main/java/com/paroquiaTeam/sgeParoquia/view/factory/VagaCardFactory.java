package com.paroquiaTeam.sgeParoquia.view.factory;

import com.paroquiaTeam.sgeParoquia.model.Vaga;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VagaCardFactory {

    public CardGenerico criarCard(Vaga vaga) {

        CardHeader header = new CardHeader(null);

        HBox conteudo = new HBox();

        conteudo.setAlignment(Pos.CENTER);

        Label label = new Label();

        conteudo.getChildren().add(label);

        VBox.setVgrow(conteudo, Priority.ALWAYS);

        if (vaga.isOcupada()) {

            header.getStyleClass().add("card-header-atencao");

            label.setText(vaga.getPlaca());

            label.setStyle("""
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #202020;
            """);

        } else {

            header.getStyleClass().add("card-header-primario");

            label.setText("Livre");

            label.setStyle("""
                    -fx-font-size: 13px;
                    -fx-text-fill: #999999;
            """);
        }

        CardGenerico card = new CardGenerico.Builder(header)
                .comConteudo(conteudo)
                .build();

        double largura = 170;
        double altura = 95;

        card.setMinWidth(largura);
        card.setMinHeight(altura);

        card.setPrefWidth(largura);
        card.setPrefHeight(altura);

        card.setMaxWidth(largura);
        card.setMaxHeight(altura);

        card.getStyleClass().add("card");

        return card;
    }
}