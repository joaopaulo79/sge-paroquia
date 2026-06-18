package com.paroquiaTeam.sgeParoquia.controller;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.core.NavegacaoManager;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService.DadosPrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.service.PrecificacaoService.DadosPrecificacaoPorHora;

public class PrecificacaoController {
	PrecificacaoService service = new PrecificacaoService();
	
	public Optional<PrecificacaoFracionada> buscarFracionada () {
		return service.buscarPrecificacaoFracionada();
	}
	
	public Optional<PrecificacaoPorHora> buscarPorHora () {
		return service.buscarPrecificacaoPorHora();
	}
	
	public void salvarPrecificacaoFracionada(int tolerancia,
			double meiaHora, double hora, double diaria,
			double meiaHoraMoto, double horaMoto, double diariaMoto) {
		try {			
			DadosPrecificacaoFracionada dados = new DadosPrecificacaoFracionada(
					tolerancia, meiaHora, hora, diaria, 
					meiaHoraMoto, horaMoto, diariaMoto);
			service.salvarFracionada(dados);			
			NavegacaoManager.getInstancia().abrirAlertaSucesso("Preços salvos com sucesso.");
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e.getLocalizedMessage());
		}
	}
	
	public void salvarPrecificacaoPorHora(int tolerancia,
			double entrada, double hora, double diaria,
			double entradaMoto, double horaMoto, double diariaMoto) {
		
		try {			
			DadosPrecificacaoPorHora dados = new DadosPrecificacaoPorHora(
					tolerancia, entrada, hora, diaria, 
					entradaMoto, horaMoto, diariaMoto);
			service.salvarPorHora(dados);			
			NavegacaoManager.getInstancia().abrirAlertaSucesso("Preços salvos com sucesso.");
		} catch (Exception e) {
			NavegacaoManager.getInstancia().abrirAlertaErro(e.getLocalizedMessage());
		}
	}
}
