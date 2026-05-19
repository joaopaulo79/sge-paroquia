package com.paroquiaTeam.sgeParoquia.view.components;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CardHeader extends HBox {

    private final Label labelId;
    private final HBox containerExtras;

    
    public CardHeader(long id) {
        this.labelId = new Label("#"+id);
        this.containerExtras = new HBox();

        HBox.setHgrow(containerExtras, Priority.ALWAYS);
        containerExtras.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(containerExtras, labelId);
        getStyleClass().add("card-header");
    }

    public void adicionarElemento(Node elemento) {
        containerExtras.getChildren().add(elemento);
    }

    public void adicionarElementos(List<Node> elementos) {
        containerExtras.getChildren().addAll(elementos);
    }
}
