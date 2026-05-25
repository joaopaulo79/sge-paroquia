package com.paroquiaTeam.sgeParoquia.service;

import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoFracionadaDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoPorHoraDAO;
import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;

public class PrecificacaoService {
	public double calcular(long tempoMinutos, boolean ehMoto) {
		try {
			TipoPrecificacao prec = new EstacionamentoDAO().get().get().getPrecificacao();
			
			Calculavel estrategia;
			
			switch (prec) {
				case FRACIONADA -> estrategia = new PrecificacaoFracionadaDAO().get().get();
				case POR_HORA -> estrategia = new PrecificacaoPorHoraDAO().get().get();
				default -> throw new IllegalStateException("Erro: precificação ativa inválida ou não implementada");
			}
			
			return estrategia.calcular(tempoMinutos, ehMoto);
		} catch (Exception e) {
			throw e;
		}
	}
}
