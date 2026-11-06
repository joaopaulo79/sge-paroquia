package com.paroquiaTeam.sgeParoquia.service;

import java.util.Optional;

import com.mysql.cj.exceptions.WrongArgumentException;
import com.paroquiaTeam.sgeParoquia.dao.EstacionamentoDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoFracionadaDAO;
import com.paroquiaTeam.sgeParoquia.dao.PrecificacaoPorHoraDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosPrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosPrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoPorHora;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPrecificacao;

public class PrecificacaoService {
	private final PrecificacaoFracionadaDAO daoFrac = new PrecificacaoFracionadaDAO();
	private final PrecificacaoPorHoraDAO daoHora = new PrecificacaoPorHoraDAO();
	private final EstacionamentoDAO daoEst = new EstacionamentoDAO();

	public Optional<PrecificacaoFracionada> buscarPrecificacaoFracionada() {
		return daoFrac.get();
	}

	public Optional<PrecificacaoPorHora> buscarPrecificacaoPorHora() {
		return daoHora.get();
	}
	
	public double calcular(long tempoMinutos, boolean ehMoto, TipoCliente tipoCliente) {
		if (tipoCliente == TipoCliente.CONVENIADO) {
			throw new WrongArgumentException("Método calcular() não deve ser usado para clientes conveniados.");
		}
		
		if (tipoCliente == TipoCliente.MENSALISTA) {
			return 0.0;
		}
		
		try {			
			TipoPrecificacao prec = new EstacionamentoDAO().get().get().getPrecificacao();

			Calculavel estrategia;

			switch (prec) {
				case FRACIONADA -> estrategia = new PrecificacaoFracionadaDAO().get().get();
				case POR_HORA -> estrategia = new PrecificacaoPorHoraDAO().get().get();
				default -> throw new IllegalStateException("Erro: precificação ativa inválida ou não implementada");
			}
			
			boolean dizimista = tipoCliente == TipoCliente.DIZIMISTA;

			return estrategia.calcular(tempoMinutos, ehMoto, dizimista);
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public double obterValorDiaria(boolean ehMoto, TipoCliente tipoCliente) {
		if (tipoCliente == TipoCliente.CONVENIADO) {
			throw new WrongArgumentException("Método obterValorDiaria() não deve ser usado para clientes conveniados.");
		}
		
		if (tipoCliente == TipoCliente.MENSALISTA) {
			return 0.0;
		}
		
		TipoPrecificacao prec = new EstacionamentoDAO().get().get().getPrecificacao();

		switch (prec) {
			case FRACIONADA -> {
				if (ehMoto) return daoFrac.get().get().getValorDiariaMoto();
				else return daoFrac.get().get().getValorDiaria();
			}
			case POR_HORA -> {
				if (ehMoto) return daoHora.get().get().getValorDiariaMoto();
				else return daoHora.get().get().getValorDiaria();
			}
			default -> throw new IllegalStateException("Erro: precificação ativa inválida ou não implementada");
		}
	}

	public void salvarFracionada(DadosPrecificacaoFracionada dados) {
		validarPrecificacao(
				dados.tolerancia(),
				dados.meiaHora(),
				dados.hora(),
				dados.diaria(),
				dados.meiaHoraMoto(),
				dados.horaMoto(),
				dados.diariaMoto());

		PrecificacaoFracionada prec = new PrecificacaoFracionada(
				dados.tolerancia(),
				dados.meiaHora(),
				dados.hora(),
				dados.diaria(),
				dados.meiaHoraMoto(),
				dados.horaMoto(),
				dados.diariaMoto());

		daoFrac.saveOrUpdate(prec);

		daoEst.updatePrecificacao(TipoPrecificacao.FRACIONADA);
	}

	public void salvarPorHora(DadosPrecificacaoPorHora dados) {
		validarPrecificacao(
				dados.tolerancia(),
				dados.entrada(),
				dados.hora(),
				dados.diaria(),
				dados.entradaMoto(),
				dados.horaMoto(),
				dados.diariaMoto());

		PrecificacaoPorHora prec = new PrecificacaoPorHora(
				dados.tolerancia(),
				dados.entrada(),
				dados.hora(),
				dados.diaria(),
				dados.entradaMoto(),
				dados.horaMoto(),
				dados.diariaMoto());

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
