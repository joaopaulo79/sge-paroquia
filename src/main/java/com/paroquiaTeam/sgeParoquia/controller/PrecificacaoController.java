package com.paroquiaTeam.sgeParoquia.controller;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoFracionadaDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoPorHoraDAO;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.PrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;

public class PrecificacaoController {
	
	public Optional<PrecificacaoFracionada> buscarFracionada () {
		return new PrecificacaoFracionadaDAO().get();
	}
	
	public Optional<PrecificacaoPorHora> buscarPorHora () {
		return new PrecificacaoPorHoraDAO().get();
	}
	
	public void salvarPrecificacaoFracionada(int tolerancia,
			double meiaHora, double hora, double diaria,
			double meiaHoraMoto, double horaMoto, double diariaMoto) {
		
		PrecificacaoFracionada precificacao = new PrecificacaoFracionada(tolerancia,
				meiaHora, hora, diaria, meiaHoraMoto, horaMoto, diariaMoto);
		PrecificacaoFracionadaDAO dao = new PrecificacaoFracionadaDAO();
		if (dao.exists()) {
			dao.update(precificacao);
		} else {
			dao.save(precificacao);
		}
		new EstacionamentoDAO().updatePrecificacao(TipoPrecificacao.FRACIONADA);
	}
	
	public void salvarPrecificacaoPorHora(int tolerancia,
			double entrada, double hora, double diaria,
			double entradaMoto, double horaMoto, double diariaMoto) {
		
		PrecificacaoPorHora precificacao = new PrecificacaoPorHora(tolerancia,
				entrada, hora, diaria, entradaMoto, horaMoto, diariaMoto);
		PrecificacaoPorHoraDAO dao = new PrecificacaoPorHoraDAO();
		if (dao.exists()) {
			dao.update(precificacao);
		} else {
			dao.save(precificacao);
		}
		new EstacionamentoDAO().updatePrecificacao(TipoPrecificacao.POR_HORA);
	}
}
