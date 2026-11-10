package com.paroquiaTeam.sgeParoquia.controller;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.core.Tela;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BaseController {
	@FXML private AnchorPane contentArea;
	@FXML private VBox sideBar;

	// nav buttons
	@FXML private ToggleButton btnNavDashboard;
	@FXML private ToggleButton btnNavConvenios;
	@FXML private ToggleButton btnNavClientes;
	@FXML private ToggleButton btnNavVeiculos;
	@FXML private ToggleButton btnNavConfig;
	@FXML private ToggleButton btnNavVagas;
	@FXML private ToggleButton btnNavUsuarios;
	@FXML private ToggleButton btnNavCaixas;
	
	@FXML private Label labelTitulo;
	
	@FXML private HBox hBoxUsuario;
	@FXML private Label labelUsuario;
	@FXML private Label labelLetraUsuario;
	
	private ContextMenu menuLogout;
	
	private static BaseController instance;

	private Tela telaAtual;

	@FXML
	private void initialize() {
		NavegacaoManager.getInstancia().setContainerPrincipal(this.contentArea);
		NavegacaoManager.getInstancia().setLabelTitulo(labelTitulo);

		LoginController loginController = NavegacaoManager.getInstancia().navegarPara(Tela.LOGIN);

		loginController.setOnSucessoLogin(this::onSucessoLogin);

		ToggleGroup grupoNav = new ToggleGroup();
		btnNavDashboard.setToggleGroup(grupoNav);
		btnNavConvenios.setToggleGroup(grupoNav);
		btnNavClientes.setToggleGroup(grupoNav);
		btnNavVeiculos.setToggleGroup(grupoNav);
		btnNavConfig.setToggleGroup(grupoNav);
		btnNavVagas.setToggleGroup(grupoNav);
		btnNavCaixas.setToggleGroup(grupoNav);
		btnNavUsuarios.setToggleGroup(grupoNav);

		btnNavDashboard.setOnAction(event -> navegar(event, Tela.DASHBOARD));
		btnNavConvenios.setOnAction(event -> navegar(event, Tela.CONVENIOS));
		btnNavClientes.setOnAction(event -> navegar(event, Tela.CLIENTES));
		btnNavVeiculos.setOnAction(event -> navegar(event, Tela.VEICULOS));
		btnNavConfig.setOnAction(event -> navegar(event, Tela.CONFIGURACAO));
		btnNavVagas.setOnAction(event -> navegar(event, Tela.VAGAS));
		btnNavCaixas.setOnAction(event -> navegar(event, Tela.CAIXAS));
		btnNavUsuarios.setOnAction(event -> navegar(event, Tela.USUARIOS));

		configurarMenuDropdown();
		
		instance = this;
	}

	public static BaseController getInstance() {
		return instance;
	}

	private void navegar(ActionEvent e, Tela tela) {
		ToggleButton btn = (ToggleButton) e.getSource();

		if (this.telaAtual == tela) {
			e.consume();
			btn.setSelected(true);
			return;
		}
		NavegacaoManager.getInstancia().navegarPara(tela);
	}

	public void setSideBar(boolean status) {
		sideBar.setManaged(status);
		sideBar.setVisible(status);
	}
	
	private void onSucessoLogin() {
		Usuario u = SessaoSistema.getInstancia().getUserLogado();
		
		labelUsuario.setText(u.getLogin());
		labelLetraUsuario.setText(u.getLogin().toUpperCase().substring(0, 1));
		hBoxUsuario.setVisible(true);
		
		setSideBar(true);
		
		boolean isAdmin = u.getTipo() == TipoUsuario.ADMINISTRADOR;
		
		btnNavUsuarios.setManaged(isAdmin);
		btnNavUsuarios.setVisible(isAdmin);
		btnNavUsuarios.setDisable(!isAdmin);
		
		NavegacaoManager.getInstancia().navegarPara(Tela.DASHBOARD);
	}
	
	private void configurarMenuDropdown() {
        menuLogout = new ContextMenu();
        
        MenuItem itemSair = new MenuItem("Sair (Realizar Logout)");
        itemSair.setStyle("-fx-cursor: hand;");
        itemSair.setOnAction(event -> executarLogout());
        
        menuLogout.getItems().add(itemSair);

        // Ao clicar no HBox do usuário, exibe o dropdown logo abaixo dele
        hBoxUsuario.setOnMouseClicked(event -> {
            if (menuLogout.isShowing()) {
                menuLogout.hide();
            } else {
                // Exibe o menu ancorado ao container do usuário
                menuLogout.show(hBoxUsuario, Side.BOTTOM, 0, 5);
            }
        });
    }

    private void executarLogout() {
        SessaoSistema.getInstancia().encerrarSessao();
        
        hBoxUsuario.setVisible(false);
		setSideBar(false);
        
		LoginController loginController = NavegacaoManager.getInstancia().navegarPara(Tela.LOGIN);
		loginController.setOnSucessoLogin(this::onSucessoLogin);
    }
}
