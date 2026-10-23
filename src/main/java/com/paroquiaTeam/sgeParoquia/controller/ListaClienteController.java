package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.paroquiaTeam.sgeParoquia.controller.records.AcoesCliente;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.service.ClienteService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaClienteController {
	ClienteService clienteService = new ClienteService();
//	ConvenioService convenioService = new ConvenioService();

	@FXML Button btnCadastrar;
	@FXML HBox hBoxLabels;
	@FXML VBox vBoxCards;

	@FXML
	private void initialize() {
		btnCadastrar.setOnAction(e -> {
			NavegacaoManager.getInstancia().abrirModal(Popup.CADASTRARCLIENTE);
			carregarClientes();
		});

		hBoxLabels.getChildren().addAll(
				new Label("Nome"),
				new Label("Tipo"),
				new Label("Ações"));

		carregarClientes();
	}

	private void carregarClientes() {
		vBoxCards.getChildren().clear();

		List<Cliente> clientes = clienteService.obterTodos();

		AcoesCliente acoes = new AcoesCliente(
				this::editar,
				this::excluir,
				this::ativar,
				this::desativar,
				this::registrarPagamento
				);

		for (Cliente c : clientes) {
			vBoxCards.getChildren().add(criarCard(c, acoes));
		}
	}

	private void editar(Long id) {
		//NavegacaoManager.getInstancia().abrirModal(Popup.EDITARCONVENIO, id);
		carregarClientes();
	}

	private void ativar(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação",
				"Tem certeza que deseja ativar o cliente?",
				AlertType.CONFIRMATION)) {
			try {
				clienteService.ativar(id);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		}

		carregarClientes();
	}

	private void desativar(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação",
				"Tem certeza que deseja desativar o cliente?",
				AlertType.CONFIRMATION)) {
			try {
				clienteService.desativar(id);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		}

		carregarClientes();
	}

	private void excluir(Long id) {
		if (NavegacaoManager.getInstancia().abrirAlerta(
				"Confirmação",
				"Tem certeza que deseja excluir o cliente?",
				AlertType.CONFIRMATION)) {
			try {
				clienteService.excluir(id);
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getMessage());
			}
		}

		carregarClientes();
	}

	private void registrarPagamento(Cliente c) {
		try {
			if (!SessaoSistema.getInstancia().temCaixaAberto()) {
				throw new IllegalStateException("É necessário ter um caixa aberto para registrar um movimento");
			}

			String mes = c.getDataVencimentoMensalidade().format(
					DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"))
					);
			String ano = c.getDataVencimentoMensalidade().format(
					DateTimeFormatter.ofPattern("yyyy", Locale.forLanguageTag("pt-BR"))
					);

			String descricao = "Pagamento de mensalidade do cliente de nome " + c.getNome()
					+ ", de valor R$"+ c.getValorMensalidade()
					+ " para o mês de " + mes
					+ " do ano " + ano;

			DadosMovimentoCaixa dadosMovimento = new DadosMovimentoCaixa(
					c.getValorMensalidade(),
					null,
					TipoMovimento.ENTRADA,
					descricao,
					SessaoSistema.getInstancia().getCaixa().getId(),
					null
					);

			NavegacaoManager.getInstancia().abrirModal(Popup.REGISTRARMOVIMENTO, dadosMovimento);

			LocalDate dataVencimentoNova = c.getDataVencimentoMensalidade().plusMonths(1);
			Long idConv = null;
			if (c.getTipo() == TipoCliente.CONVENIADO) {
				idConv = c.getConvenio().getId();
			}
			
			DadosCliente dadosNovos = new DadosCliente(
					c.getNome(),
					c.getCpf(),
					c.getTelefone(),
					c.getTipo(),
					c.getStatus(),
					dataVencimentoNova,
					c.getValorMensalidade(),
					idConv
					);

			clienteService.atualizar(dadosNovos, c);

			carregarClientes();
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e.getMessage());
		}
	}
	
	// MOCK
	private VBox criarCard(Cliente c, AcoesCliente acoes) {
		VBox card = new VBox();

		HBox linhaPrincipal = new HBox();
		HBox linhaSecundaria = new HBox();

		Button btnEditar = new Button("Editar");
		Button btnAlterarStatus;
		Button btnExcluir = new Button("Excluir");

		btnEditar.setOnAction(e -> acoes.onEditar().accept(c.getId()));

		if (c.isDesativado()) {
			btnAlterarStatus = new Button("Ativar");
			btnAlterarStatus.setOnAction(e -> acoes.onAtivar().accept(c.getId()) );
		} else {
			btnAlterarStatus = new Button("Desativar");
			btnAlterarStatus.setOnAction(e -> acoes.onDesativar().accept(c.getId()) );

		}

		btnExcluir.setOnAction(e -> acoes.onExcluir().accept(c.getId()) );
		
		
		if (c.getTipo() == TipoCliente.MENSALISTA) {
			Button btnPagamento = new Button("Registrar Pagamento");
			
			btnPagamento.setOnAction(e -> acoes.onRegistrarPagamento().accept(c));
			
			linhaSecundaria.getChildren().add(btnPagamento);
		}
		

		linhaPrincipal.getChildren().addAll(
				new Label(c.getNome()),
				new Label(" "+(c.getTipo())),
				btnEditar,
				btnAlterarStatus
				);
		
		linhaSecundaria.getChildren().addAll(
				btnExcluir
				);

		card.getChildren().addAll(linhaPrincipal, linhaSecundaria);
		return card;
	}
}
