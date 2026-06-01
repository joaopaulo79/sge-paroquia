package com.paroquiaTeam.sgeParoquia.core;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NavegacaoManager {
    private static NavegacaoManager instancia;
    private AnchorPane containerPrincipal; // O container (ex: centro do seu BorderPane) onde as telas mudam

    private NavegacaoManager() {}

    public static NavegacaoManager getInstancia() {
        if (instancia == null) {
            instancia = new NavegacaoManager();
        }
        return instancia;
    }

    public void setContainerPrincipal(AnchorPane container) {
        this.containerPrincipal = container;
    }

    public <T> T navegarPara(Tela tela) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(tela.getPathFxml()));
            Parent root = loader.load();
            
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            
            containerPrincipal.getChildren().setAll(root);
            
            // Alteração de título que espera que o Scene exista
            Platform.runLater(() -> {
                if (containerPrincipal.getScene() != null && containerPrincipal.getScene().getWindow() != null) {
                    Stage stage = (Stage) containerPrincipal.getScene().getWindow();
                    stage.setTitle(tela.getTitulo());
                }
            });
            
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao carregar a tela: " + tela, e);
        }
    }
}
