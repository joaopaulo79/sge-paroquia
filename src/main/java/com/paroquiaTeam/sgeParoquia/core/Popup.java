package com.paroquiaTeam.sgeParoquia.core;

import com.paroquiaTeam.sgeParoquia.controller.popup.AlterarVagasController;
import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroConvenioController;
import com.paroquiaTeam.sgeParoquia.controller.popup.EditarConvenioController;

public enum Popup {
	ALTERARVAGAS("/popups/alterarVagas/alterarVagas.fxml","Alterar Vagas", AlterarVagasController.class),
	CADASTRARCONVENIO("/popups/cadastrarConvenio/cadastrarConvenio.fxml", "Cadastrar Convenio", CadastroConvenioController.class),
	EDITARCONVENIO("/popups/cadastrarConvenio/cadastrarConvenio.fxml", "Editar Convenio", EditarConvenioController.class);

	private final String pathFxml;
    private final String titulo;
    private final Class<?> classeController;

    Popup(String fxmlPath, String titulo, Class<?> classeController) {
        this.pathFxml = fxmlPath;
        this.titulo = titulo;
        this.classeController = classeController;
    }

    public String getPathFxml() { return pathFxml; }
    public String getTitulo() { return titulo; }
    public Class<?> getClasseController() { return classeController; }
}
