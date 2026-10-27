package com.paroquiaTeam.sgeParoquia.core;

import com.paroquiaTeam.sgeParoquia.controller.ConfiguracaoSistemaController;
import com.paroquiaTeam.sgeParoquia.controller.DashboardController;
import com.paroquiaTeam.sgeParoquia.controller.ListaClienteController;
import com.paroquiaTeam.sgeParoquia.controller.ListaConvenioController;
import com.paroquiaTeam.sgeParoquia.controller.ListaVagasController;
import com.paroquiaTeam.sgeParoquia.controller.ListaVeiculoController;
import com.paroquiaTeam.sgeParoquia.controller.LoginController;

public enum Tela {
	LOGIN("/screens/login/login.fxml", "Login", LoginController.class),
    DASHBOARD("/screens/dashboard/dashboard.fxml", "Painel Principal", DashboardController.class),
    CONFIGURACAO("/screens/configuracaoSistema/configuracaoSistema.fxml", "Configurações do Sistema", ConfiguracaoSistemaController.class),
    VAGAS("/screens/listaVagas/listaVagas.fxml", "Vagas", ListaVagasController.class),

    // Telas que usam listaBase
	CONVENIOS("/shared/listaBase/listaBase.fxml", "Convênios", ListaConvenioController.class),
	CLIENTES("/shared/listaBase/listaBase.fxml", "Clientes", ListaClienteController.class),
	VEICULOS("/shared/listaBase/listaBase.fxml", "Veículos", ListaVeiculoController.class);


    private final String pathFxml;
    private final String titulo;
    private final Class<?> classeController;

    Tela(String fxmlPath, String titulo, Class<?> classeController) {
        this.pathFxml = fxmlPath;
        this.titulo = titulo;
        this.classeController = classeController;
    }

    public String getPathFxml() { return pathFxml; }
    public String getTitulo() { return titulo; }
    public Class<?> getClasseController() { return classeController; }
}
