package com.paroquiaTeam.sgeParoquia.controller;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.dao.CaixaDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.service.CaixaService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class CaixaController {
	private final StackPane containerCaixa;
	private final CaixaService service = new CaixaService();
	
	private Consumer<Boolean> onStatusCaixaAlterado;
	
	public CaixaController(StackPane containerCaixa) {
        this.containerCaixa = containerCaixa;
    }
	
	public void setOnStatusCaixaAlterado(Consumer<Boolean> listener) {
        this.onStatusCaixaAlterado = listener;
    }
	

	@FXML TextField campoFundoInicial;
	@FXML Spinner<Double> spinnerFundoInicial;
	@FXML Button btnAbrirCaixa;
	@FXML Label labelOperador;
	@FXML Label labelHora;

	public void carregarPainel() {
		try {
			boolean caixaAberto = SessaoSistema.getInstancia().temCaixaAberto();
			
			containerCaixa.getChildren().clear();
	        
	        String fxmlPath = caixaAberto ? 
	            "/screens/dashboard/acoes-caixa-aberto.fxml" : 
	            "/screens/dashboard/acoes-caixa-fechado.fxml";
	
	        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
	        loader.setController(this);
	
	        Node painel = loader.load();
	        containerCaixa.getChildren().add(painel);
	
	        if (caixaAberto) {
	        	configurarTelaCaixaAberto();
	        } else {
            	configurarTelaCaixaFechado();
	        }
		
	        if (onStatusCaixaAlterado != null) onStatusCaixaAlterado.accept(caixaAberto);
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro("Erro ao inicializar painel de caixa:\n" + e.getMessage());
		}
	}

	private void configurarTelaCaixaAberto() {
        Caixa caixaAtual = SessaoSistema.getInstancia().getCaixa();
        if (caixaAtual != null && labelOperador != null && labelHora != null) {
            labelOperador.setText(caixaAtual.getOperador().getNome());
            String hora = String.format("%02d:%02d", 
                caixaAtual.getDataHoraAbertura().getHour(), 
                caixaAtual.getDataHoraAbertura().getMinute());
            labelHora.setText(hora);
        }
    }
	
	private void configurarTelaCaixaFechado() {
		if (spinnerFundoInicial != null) {
			spinnerFundoInicial.setValueFactory(
					new DoubleSpinnerValueFactory(0, 100000, 0, 0.01));
		}
	}

	public void abrirCaixa() {
		long idOperador = SessaoSistema.getInstancia().getUserLogado().getId();
		double fundoInicial = spinnerFundoInicial.getValue();
		DadosCaixa dados = new DadosCaixa(LocalDateTime.now(), null, fundoInicial, idOperador);
		service.abrir(dados);
		
		carregarPainel();
	}

	public void fecharCaixa() {
		Caixa caixaAtual = SessaoSistema.getInstancia().getCaixa();

		service.fechar(caixaAtual, LocalDateTime.now());

		carregarPainel();
	}
}
