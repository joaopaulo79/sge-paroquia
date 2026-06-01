package com.paroquiaTeam.sgeParoquia.core;

public enum Tela {
	LOGIN("/screens/login/login.fxml", "Login"),
    DASHBOARD("/screens/dashboard/dashboard.fxml", "Painel Principal"),
    CONFIGURACAO("/screens/configuracaoSistema/configuracaoSistema.fxml", "Configurações do Sistema"),
    VAGAS("/screens/listaVagas/listaVagas.fxml", "Vagas");

    private final String pathFxml;
    private final String titulo;

    Tela(String fxmlPath, String titulo) {
        this.pathFxml = fxmlPath;
        this.titulo = titulo;
    }

    public String getPathFxml() { return pathFxml; }
    public String getTitulo() { return titulo; }
}
