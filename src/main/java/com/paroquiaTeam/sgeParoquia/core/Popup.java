package com.paroquiaTeam.sgeParoquia.core;

public enum Popup {
	ALTERARVAGAS("","Alterar Vagas");
	
	private final String pathFxml;
    private final String titulo;

    Popup(String fxmlPath, String titulo) {
        this.pathFxml = fxmlPath;
        this.titulo = titulo;
    }

    public String getPathFxml() { return pathFxml; }
    public String getTitulo() { return titulo; }
}
