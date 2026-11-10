package com.paroquiaTeam.sgeParoquia.controller;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.controller.popup.SelecionarTipoVeiculoController;
import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.Popup;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.service.VagaService;
import com.paroquiaTeam.sgeParoquia.service.VeiculoService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class EstadiaController {
	public record Entidades(Veiculo veiculo, Cliente cliente, Convenio convenio) {}
	public record DadosEntrada(Entidades entidades, TipoVaga tipo, String placa) {};
	public record DadosSaida(Entidades entidades, String placa) {};

    
    private VagaService vagaService = new VagaService();

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
}
