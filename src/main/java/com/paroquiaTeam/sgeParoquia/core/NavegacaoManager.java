package com.paroquiaTeam.sgeParoquia.core;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NavegacaoManager {
    private static NavegacaoManager instancia;
    private AnchorPane containerPrincipal;

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
    
    public <T> T abrirModal(Popup popup) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(popup.getPathFxml()));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(popup.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            if (containerPrincipal != null && containerPrincipal.getScene() != null) {
                Stage owner = (Stage) containerPrincipal.getScene().getWindow();
                dialogStage.initOwner(owner);
            }
            
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            root.setUserData(dialogStage);

            dialogStage.showAndWait(); 

            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao abrir modal: " + popup, e);
        }
    }
}
