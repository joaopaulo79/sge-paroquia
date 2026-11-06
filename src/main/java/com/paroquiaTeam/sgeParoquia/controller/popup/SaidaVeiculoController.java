package com.paroquiaTeam.sgeParoquia.controller.popup;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.controller.EstadiaController.DadosSaida;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosEstadia;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.Vaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;
import com.paroquiaTeam.sgeParoquia.service.EstadiaService;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;
import com.paroquiaTeam.sgeParoquia.service.VagaService;
import com.paroquiaTeam.sgeParoquia.utils.StringUtil;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;
import com.paroquiaTeam.sgeParoquia.view.factory.ClienteCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.ConvenioCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.EstadiaCardFactory;
import com.paroquiaTeam.sgeParoquia.view.factory.VeiculoCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaidaVeiculoController {
	DadosSaida dados;
	boolean precisaRealizarPagamento = true;
	Double valor = null;
	LocalDateTime dataHoraSaida = LocalDateTime.now();;
	
	// Vaga que o veículo ocupa, e estadia relacionada
	Vaga vaga;
	Estadia estadia;
	
	VagaService vagaService = new VagaService();
	EstadiaService estadiaService = new EstadiaService();
	
	public SaidaVeiculoController(DadosSaida dados) {
		this.dados = dados;
		
		vaga = vagaService.obterPorPlaca(dados.placa());
		estadia = estadiaService.obterPorPlaca(dados.placa());
	}
	
	@FXML VBox containerCards;
	@FXML CheckBox checkAdiantarDiaria;
	
	@FXML Button btnConfirmar;
	@FXML Button btnCancelar;
	
	@FXML
	private void initialize() {
		checkAdiantarDiaria.setVisible(false);
		checkAdiantarDiaria.setManaged(false);
		checkAdiantarDiaria.setDisable(true);
		checkAdiantarDiaria.setSelected(false);
		
		if (!vagaService.veiculoEstaNoPatio(dados.placa())) {
			throw new IllegalStateException("Veículo não está no pátio, não foi possível registrar saída");
		}
		
		// Validando dados
		if (dados == null) {
			throw new IllegalArgumentException("Dados inválidos");
		}
		
		if (estadia.getDataHoraSaida() != null) {
			throw new IllegalStateException("Última estadia deste veículo, teve saída");
		}
		
		calcularValor();
		
		
		btnConfirmar.setOnAction(e -> {
			try {
				boolean registroTeveSucesso = registrarSaida();

				if (registroTeveSucesso) {
					NavegacaoManager.getInstancia().abrirAlertaSucesso("Saída registrada");
					
					Stage stage = (Stage) btnConfirmar.getScene().getWindow();
			        stage.close();
				}
			} catch (Exception e2) {
				NavegacaoManager.getInstancia().abrirAlertaErro(e2.getLocalizedMessage());
				e2.printStackTrace();
			}
		});
		
		btnCancelar.setOnAction(e -> {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
	        stage.close();
		});
		
		carregarCards();
	}
	
	private void carregarCards() {
		containerCards.getChildren().clear();

		Estadia e = new Estadia(estadia.getDataHoraEntrada(), dataHoraSaida, valor, dados.placa());
		containerCards.getChildren().add(new EstadiaCardFactory().criarCardSaida(e));
		
		if (dados.entidades().veiculo() != null) {
			containerCards.getChildren().add(
					new VeiculoCardFactory().criarCardInformacoes(dados.entidades().veiculo()));
			
			if (dados.entidades().cliente() != null) {
				containerCards.getChildren().add(
						new ClienteCardFactory().criarCardInformacoes(dados.entidades().cliente()));
			}
			
			if (dados.entidades().convenio() != null) {
				containerCards.getChildren().add(
						new ConvenioCardFactory().criarCardInformacoes(dados.entidades().convenio()));
			}
		}
	}
	
	private void calcularValor() {
		if (estadia.getValor() != null) {
			// houve pagamento, não tem que pagar
			precisaRealizarPagamento = false;
			valor = estadia.getValor();
		} 
		else {
			// Não houve pagamento, decidir o que cobrar
			Cliente c = dados.entidades().cliente();
			
			TipoCliente tipoCliente;
			if (c == null) tipoCliente = TipoCliente.AVULSO;
			else tipoCliente = c.getTipo();
			
			// Cliente existe, é conveniado e entrou em vaga do convenio
			if (tipoCliente == TipoCliente.CONVENIADO && vaga.getReserva() == TipoReservaVaga.CONVENIO) {
				valor = c.getConvenio().getCobrancaIndividual();
				carregarCards();
				return;
			}
			
			boolean moto = (vaga.getTipo() == TipoVaga.MOTO);
			long tempo = TempoUtils.calcularDiferencaMinutos(estadia.getDataHoraEntrada(), dataHoraSaida);
			valor = new PrecificacaoService().calcular(
					tempo,
					moto,
					tipoCliente);
			
			carregarCards();
		}
	}
	
	private boolean registrarSaida() {
		DadosEstadia dadosEstadia = new DadosEstadia(
				estadia.getDataHoraEntrada(),
				dataHoraSaida,
				valor,
				dados.placa());
		
		estadiaService.validarEstadia(dadosEstadia);
		
		DadosMovimentoCaixa dadosMovimento = null;
		if (valor != null) {
			String mensagem = "Pagamento referente à saída de veículo com placa '"
					+ dados.placa() + "', ocorrido no dia "
					+ StringUtil.extrairEFormatarData(dataHoraSaida)
					+ ", às " + StringUtil.extrairEFormatarHora(dataHoraSaida);
			
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
			    return false; // Interrompe o método e não prossegue com o salvamento
			}
			
			dadosMovimento = new DadosMovimentoCaixa(
					valor,
					eFPC.getFormaPagamento(),
					TipoMovimento.ENTRADA,
					mensagem,
					idCaixa,
					null);
		}
		
		estadiaService.registrarSaida(dadosEstadia, estadia, dadosMovimento);
		vagaService.registrarSaida(dados.placa());
		return true;
	}
}
