package com.paroquiaTeam.sgeParoquia.service;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoFracionadaDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoPorHoraDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPrecificacao;

public class PrecificacaoService {
	public record DadosPrecificacaoFracionada(
			int tolerancia,
			double meiaHora, 
			double hora, 
			double diaria,
			double meiaHoraMoto, 
			double horaMoto, 
			double diariaMoto) {}
	
	public record DadosPrecificacaoPorHora(
			int tolerancia,
			double entrada, 
			double hora, 
			double diaria,
			double entradaMoto, 
			double horaMoto, 
			double diariaMoto) {}
	
	private final PrecificacaoFracionadaDAO daoFrac = new PrecificacaoFracionadaDAO();
	private final PrecificacaoPorHoraDAO daoHora = new PrecificacaoPorHoraDAO();
	private final EstacionamentoDAO daoEst = new EstacionamentoDAO();
	
	public Optional<PrecificacaoFracionada> buscarPrecificacaoFracionada() {
		return daoFrac.get();
	}
	
	public Optional<PrecificacaoPorHora> buscarPrecificacaoPorHora() {
		return daoHora.get();
	}
	
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
	
	public void salvarFracionada(DadosPrecificacaoFracionada dados) {
		validarPrecificacao(
				dados.tolerancia, 
				dados.meiaHora, 
				dados.hora,
				dados.diaria, 
				dados.meiaHoraMoto,
				dados.horaMoto,
				dados.diariaMoto);
		
		PrecificacaoFracionada prec = new PrecificacaoFracionada(
				dados.tolerancia, 
				dados.meiaHora, 
				dados.hora,
				dados.diaria, 
				dados.meiaHoraMoto,
				dados.horaMoto,
				dados.diariaMoto);
		
		daoFrac.saveOrUpdate(prec);
		
		daoEst.updatePrecificacao(TipoPrecificacao.FRACIONADA);
	}
	
	public void salvarPorHora(DadosPrecificacaoPorHora dados) {
		validarPrecificacao(
				dados.tolerancia, 
				dados.entrada, 
				dados.hora,
				dados.diaria, 
				dados.entradaMoto,
				dados.horaMoto,
				dados.diariaMoto);
		
		PrecificacaoPorHora prec = new PrecificacaoPorHora(
				dados.tolerancia, 
				dados.entrada, 
				dados.hora,
				dados.diaria, 
				dados.entradaMoto,
				dados.horaMoto,
				dados.diariaMoto);
		
		daoHora.saveOrUpdate(prec);
		
		daoEst.updatePrecificacao(TipoPrecificacao.POR_HORA);
	}
	
	private void validarPrecificacao(int tolerancia, double... valores) {
		if (tolerancia < 0) {
			throw new IllegalArgumentException("Tolerância não pode ser negativa.");
		}
		for (double valor : valores) {
			if (valor < 0) {
				throw new IllegalArgumentException("Valores monetários não podem ser negativos.");
			}
		}
	}
}
