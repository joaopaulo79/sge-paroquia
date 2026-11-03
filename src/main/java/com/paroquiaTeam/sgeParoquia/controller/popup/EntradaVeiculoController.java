package com.paroquiaTeam.sgeParoquia.controller.popup;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.controller.EstadiaController.DadosEntrada;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosEstadia;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;
import com.paroquiaTeam.sgeParoquia.service.EstadiaService;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;
import com.paroquiaTeam.sgeParoquia.service.VagaService;
import com.paroquiaTeam.sgeParoquia.utils.StringUtil;
import com.paroquiaTeam.sgeParoquia.view.factory.ClienteCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.ConvenioCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.EstadiaCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.VeiculoCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EntradaVeiculoController {
	DadosEntrada dados;
	private Double valor = null;
	LocalDateTime dataHoraEntrada = LocalDateTime.now();
	
	VagaService vagaService = new VagaService();
	EstadiaService estadiaService = new EstadiaService();
	
	public EntradaVeiculoController(DadosEntrada dados) {
		this.dados = dados;
	}
	
	@FXML VBox containerCards;
	@FXML CheckBox checkAdiantarDiaria;
	
	@FXML Button btnConfirmar;
	@FXML Button btnCancelar;

	
	@FXML
	private void initialize() {
		// Validando dados
		if (dados == null) {
			throw new IllegalArgumentException("Dados inválidos");
		}
	
		// Verificando tipo de cliente, para escolher tipo de reserva
		TipoReservaVaga reserva;
		Cliente c = dados.entidades().cliente();
		if (c != null && c.getTipo() == TipoCliente.CONVENIADO && c.getConvenio() != null) {
			if (vagaService.temEspacoConvenio(dados.tipo(), c.getConvenio().getId())) {
				reserva = TipoReservaVaga.CONVENIO;
			} 
			else {
				String mensagem = "Vagas de convênio esgotadas\n"
						+ "Se o cliente desejar entrar, deverá entrar como avulso, e será cobrado como tal"
						+ "Registrar entrada do cliente como avulso?";
				if (NavegacaoManager.getInstancia().abrirAlertaConfirmacao(mensagem)) {
					reserva = TipoReservaVaga.COMUM;
				} else {
					throw new RuntimeException("Vagas de convênio esgotadas e operação cancelada");
				}
			}
		} else {
			if (!vagaService.temEspaco(dados.tipo(), TipoReservaVaga.COMUM)) {
				throw new IllegalStateException("Não há vagas livres compativeis no estacionamento");
			}
			reserva = TipoReservaVaga.COMUM;
		}
		
		carregarCards();
		
		checkAdiantarDiaria.setOnAction(e -> {
			if (checkAdiantarDiaria.isSelected() ) {
				boolean moto = (dados.tipo() == TipoVaga.MOTO);
				valor = new PrecificacaoService().obterValorDiaria(moto);
			} else {
				valor = null;
			}
			
			carregarCards();
		});
		
		btnConfirmar.setOnAction(e -> {
			try {
				registrarEntrada(reserva);
				
				NavegacaoManager.getInstancia().abrirAlertaSucesso("Entrada registrada");
				
				Stage stage = (Stage) btnConfirmar.getScene().getWindow();
		        stage.close();
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getLocalizedMessage());
				e2.printStackTrace();
			}
		});
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
	        stage.close();
		});
	}
	
	private void carregarCards() {
		containerCards.getChildren().clear();

		Estadia e = new Estadia(dataHoraEntrada, null, valor, dados.placa());
		containerCards.getChildren().add(new EstadiaCardFactory().criarCardEntrada(e));
		
		if (dados.entidades().veiculo() != null) {
			containerCards.getChildren().add(
					new VeiculoCardFactory().criarCardInformacoes(dados.entidades().veiculo()));
			containerCards.getChildren().add(
					new ClienteCardFactory().criarCardInformacoes(dados.entidades().cliente()));
			
			if (dados.entidades().convenio() != null) {
				containerCards.getChildren().add(
						new ConvenioCardFactory().criarCardInformacoes(dados.entidades().convenio()));
			}
		}
	}

	private void registrarEntrada(TipoReservaVaga tipo) {
		DadosEstadia dadosEstadia = new DadosEstadia(
				dataHoraEntrada,
				null,
				valor,
				dados.placa());
		
		estadiaService.validarEstadia(dadosEstadia);
		
		if (tipo == TipoReservaVaga.CONVENIO) {
			if (!vagaService.temEspacoConvenio(dados.tipo(), dados.entidades().convenio().getId())) {
				throw new IllegalStateException("Não há vagas livres compativeis no estacionamento");
			}
		} else {
			if (!vagaService.temEspaco(dados.tipo(), tipo)) {
				throw new IllegalStateException("Não há vagas livres compativeis no estacionamento");
			}
		}
		
		DadosMovimentoCaixa dadosMovimento = null;
		if (valor != null) {
			String mensagem = "Pagamento referente ao adiantamento de entrada de veículo com placa '"
					+ dados.placa() + "', ocorrido no dia "
					+ StringUtil.extrairEFormatarData(dataHoraEntrada)
					+ ", as " + StringUtil.extrairEFormatarHora(dataHoraEntrada);
			
			Long idCaixa = SessaoSistema.getInstancia().getCaixa().getId();
			DadosMovimentoCaixa dTemp = new DadosMovimentoCaixa(
					valor,
					null,
					TipoMovimento.ENTRADA,
					mensagem,
					idCaixa,
					null);
			
			EscolherFormaPagamentoController eFPC = NavegacaoManager
					.getInstancia().abrirModal(Popup.ESCOLHERFORMAPAGAMENTO, dTemp);
			
			if (eFPC == null || eFPC.getFormaPagamento() == null) {
			    return; // Interrompe o método e não prossegue com o salvamento
			}
			
			dadosMovimento = new DadosMovimentoCaixa(
					valor,
					eFPC.getFormaPagamento(),
					TipoMovimento.ENTRADA,
					mensagem,
					idCaixa,
					null);
		}
		
		estadiaService.registrarEntrada(dadosEstadia, dadosMovimento);			
		
		if (tipo == TipoReservaVaga.CONVENIO) {
			vagaService.registrarEntradaConvenio(dados.tipo(), dados.entidades().convenio().getId(), dados.placa());
		} else {
			vagaService.registrarEntradaComum(dados.tipo(), dados.placa());
		}
	}
}
