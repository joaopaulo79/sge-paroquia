package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.controller.popup.SelecionarTipoVeiculoController;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;
import com.paroquiaTeam.sgeParoquia.service.VagaService;
import com.paroquiaTeam.sgeParoquia.service.VeiculoService;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;
import com.paroquiaTeam.sgeParoquia.view.factory.EstadiaCardFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EstadiaController {
	public record Entidades(Veiculo veiculo, Cliente cliente, Convenio convenio) {}
	public record DadosEntrada(Entidades entidades, TipoVaga tipo, String placa) {};
	public record DadosSaida(Entidades entidades, String placa) {};
	
    // Enum para deixar o código mais limpo
    private enum TipoOperacao { ENTRADA, SAIDA }
    
    private VagaService vagaService = new VagaService();
    
    private TipoOperacao operacao;
    private Estadia estadia;
    private EstadiaDAO dao = new EstadiaDAO();

    @FXML VBox containerCards;
    @FXML Button btnConfirmar;
    @FXML Button btnCancelar;
    
    public void handleEstadia(String placa) {    	
    	Entidades entidades = carregarEntidades(placa);
    	Veiculo veiculo = entidades.veiculo;
    	
    	if (vagaService.veiculoEstaNoPatio(placa)) {
    		DadosSaida dados = new DadosSaida(entidades, placa);
    		
    		NavegacaoManager.getInstancia().abrirModal(Popup.SAIDAVEICULO, dados);
    	} 
    	else {
    		TipoVaga tipoVaga;
    		if (veiculo == null) {
    			SelecionarTipoVeiculoController sTVC = NavegacaoManager
    					.getInstancia().abrirModal(Popup.SELECIONARTIPOVEICULO);
    			tipoVaga = sTVC.getTipoSelecionado();
    		} else {
    			if (veiculo.getTipo() == TipoVeiculo.CARRO) {
    				tipoVaga = TipoVaga.CARRO;
    			} else {
    				tipoVaga = TipoVaga.MOTO;
    			}
    		}
    		
    		DadosEntrada dados = new DadosEntrada(entidades, tipoVaga, placa);
    		
    		NavegacaoManager.getInstancia().abrirModal(Popup.ENTRADAVEICULO, dados);
    	}
    	
    }
    
    private Entidades carregarEntidades(String placa) {
    	Veiculo veiculo = null;
    	Cliente cliente = null;
    	Convenio convenio = null;
    	
    	Optional<Veiculo> talvezVeiculo = new VeiculoService().buscarPorPlacaComClienteEConvenio(placa);
    	if (talvezVeiculo.isPresent()) {
    		veiculo = talvezVeiculo.get();
    		
    		// Só carrega cliente e convenio se cliente for ativo
    		if (veiculo.getCliente().isAtivo()) {
    			cliente = veiculo.getCliente();
    			convenio = cliente.getConvenio();
    		} else {
    			String mensagem = "Cliente está com mensalidade atrasada ou está desativado.\n"
    					+ "Cliente será tratado como avulso, e benefícios não serão aplicados";
    			NavegacaoManager.getInstancia().abrirAlerta("Alerta", mensagem, AlertType.WARNING);
    		}
    	}
    	
    	// Todos podem acabar nulo
    	return new Entidades(veiculo, cliente, convenio);
    }
  
    
    public boolean configurarEValidar(TipoOperacao operacao, String placa) {
        this.operacao = operacao;

        if (operacao == TipoOperacao.SAIDA && !validarEstadiaAtiva(placa)) {
        	return false;
        }

        if (operacao == TipoOperacao.ENTRADA) {
            this.estadia = new Estadia();
            this.estadia.setPlacaVeiculo(placa);
            this.estadia.setDataHoraEntrada(LocalDateTime.now());
        }

    	criarCards();
    	btnCancelar.setOnAction(e -> ((Stage) btnCancelar.getScene().getWindow()).close());
    	btnConfirmar.setOnAction(e -> {
    		if (this.operacao == TipoOperacao.ENTRADA) {
    			this.dao.save(estadia);
    		} else if (this.operacao == TipoOperacao.SAIDA) {
    			this.dao.update(estadia);
    		}
    		((Stage) btnConfirmar.getScene().getWindow()).close();
    	});
    	return true;
    }

    private void criarCards() {
    	containerCards.getChildren().add(
	    	switch (operacao) {
	    		case ENTRADA -> new EstadiaCardFactory().criarCardEntrada(estadia);
	    		case SAIDA -> new EstadiaCardFactory().criarCardSaida(estadia, false);
	    	}
    	);
    }

    private boolean validarEstadiaAtiva(String placa) {
        Optional<Estadia> busca = dao.getLastByPlaca(placa);
        if (busca.isPresent() && busca.get().getDataHoraSaida() == null) {
            this.estadia = busca.get();
            this.estadia.setDataHoraSaida(LocalDateTime.now());
            PrecificacaoService precificacaoService = new PrecificacaoService();
            this.estadia.setValor(precificacaoService.calcular(
            		TempoUtils.calcularDiferencaMinutos(
            				estadia.getDataHoraEntrada(),
            				estadia.getDataHoraSaida()),
            		false,
            		TipoCliente.AVULSO));

            return true;
        } else {
        	return false;
        }
    }
}
