package com.paroquiaTeam.sgeParoquia.core;

import com.paroquiaTeam.sgeParoquia.controller.popup.AlterarVagasController;
import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroClienteController;
import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroConvenioController;
import com.paroquiaTeam.sgeParoquia.controller.popup.CadastroVeiculoController;
import com.paroquiaTeam.sgeParoquia.controller.popup.EditarClienteController;
import com.paroquiaTeam.sgeParoquia.controller.popup.EditarConvenioController;
import com.paroquiaTeam.sgeParoquia.controller.popup.EditarVeiculoController;
import com.paroquiaTeam.sgeParoquia.controller.popup.RegistrarMovimentoCaixaController;
import com.paroquiaTeam.sgeParoquia.controller.popup.SelecaoClienteController;

public enum Popup {
	ALTERARVAGAS("/popups/alterarVagas/alterarVagas.fxml","Alterar Vagas", AlterarVagasController.class),
	
	CADASTRARCONVENIO("/popups/cadastrarConvenio/cadastrarConvenio.fxml", "Cadastrar Convênio", CadastroConvenioController.class),
	EDITARCONVENIO("/popups/cadastrarConvenio/cadastrarConvenio.fxml", "Editar Convenio", EditarConvenioController.class),
	
	CADASTRARCLIENTE("/popups/cadastrarCliente/cadastrarCliente.fxml", "Cadastrar Cliente", CadastroClienteController.class),
	EDITARCLIENTE("/popups/cadastrarCliente/cadastrarCliente.fxml", "Editar Cliente", EditarClienteController.class),
	SELECAOCLIENTE("/shared/listaBase/listaBase.fxml", "Selecionar Cliente", SelecaoClienteController.class),
	
	CADASTRARVEICULO("/popups/cadastrarVeiculo/cadastrarVeiculo.fxml", "Cadastrar Veículo", CadastroVeiculoController.class),
	EDITARVEICULO("/popups/cadastrarVeiculo/cadastrarVeiculo.fxml", "Editar Veículo", EditarVeiculoController.class),

	REGISTRARMOVIMENTO("/popups/registrarMovimento/registrarMovimento.fxml", "Registrar Movimento de Caixa", RegistrarMovimentoCaixaController.class);

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
