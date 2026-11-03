package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosEstadia;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;

import jakarta.persistence.EntityNotFoundException;

public class EstadiaService {
	private EstadiaDAO dao = new EstadiaDAO();

	public boolean ultimaEstadiaTeveSaida(String placa) {
		Optional<Estadia> talvezEstadia = dao.getLastByPlaca(placa);
		if (talvezEstadia.isEmpty()) {
			return true; // Não tem estadia, logo trata como se tivesse saída
		}

		Estadia estadia = talvezEstadia.get();
		if (estadia.getDataHoraSaida() == null) {
			return false; // estadia sem dataHora de saida, logo ainda está no pátio
		}
		return true; // se chegou aqui, estadia tem dataHora de saida
	}

	public boolean existePorId(long id) {
		return dao.exists(id);
	}

	public Optional<Estadia> buscarPorPlaca(String placa) {
		return dao.getLastByPlaca(placa);
	}

	public Optional<Estadia> buscarPorId(Long id) {
		return dao.getById(id);
	}

	public Estadia obterPorId(Long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Estadia não encontrada para o id "+id));
	}

	public void salvar(DadosEstadia dados) {
		validarEstadia(dados);

		Estadia estadia = dados.paraEstadia();
		dao.save(estadia);
	}

//	public void atualizar(Estadia estadia, DadosEstadia dadosNovos) {
//		validarEstadia(dadosNovos);
//
//		estadia.setDataHoraEntrada(dadosNovos.dataHoraEntrada());
//		estadia.setDataHoraSaida(dadosNovos.dataHoraSaida());
//		estadia.setValor(dadosNovos.valor());
//		estadia.setPlacaVeiculo(dadosNovos.placaVeiculo());
//
//		dao.update(estadia);
//	}

	// se Valor não for null, a estadia foi adiantada, e movimento é obrigatório
	// caso contrário, não deve ser registrado nada
	public void registrarEntrada(DadosEstadia dadosEstadia, DadosMovimentoCaixa dadosMovimento) {
		validarEstadia(dadosEstadia);
		
		boolean entradaAdiantada = dadosEstadia.valor() != null;
		boolean temDadosMovimento = dadosMovimento != null;
		
		if (entradaAdiantada && !temDadosMovimento) {
	        throw new IllegalStateException("É obrigatório ter um movimento de caixa para entrada com valor adiantado.");
		}
		
		if (dadosMovimento != null) {
			new MovimentoCaixaService().validarMovimento(dadosMovimento);
		}
		
	    if (!SessaoSistema.getInstancia().temCaixaAberto()) {
	        throw new IllegalStateException("É obrigatório ter um caixa aberto para registrar a entrada.");
	    }

	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();

	        Estadia estadia = dadosEstadia.paraEstadia();	        
	       
	        session.persist(estadia); // Aqui a estadia ganha ID, mas ainda NÃO foi confirmada definitivamente
			
	        if (temDadosMovimento) {
		        // Cria e Salva o Movimento de Caixa usando o objeto 'estadia'
		        Caixa caixaAtual = SessaoSistema.getInstancia().getCaixa();
		        MovimentoCaixa movimento = dadosMovimento.paraMovimentoCaixa(caixaAtual, estadia);
		        session.persist(movimento);
	    	}
	        
	        // Se TUDO deu certo, efetiva no banco de dados de uma vez só
	        transaction.commit();
	    } catch (Exception e) {
	        // Se houver qualquer falha no passo 2 ou 3, o banco desfaz a estadia instantaneamente
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        throw new RuntimeException("Erro ao registrar entrada: " + e.getMessage(), e);
	    }
	};
	
	

	public void registrarSaida(LocalDateTime dataHoraSaida, Double valor, Estadia estadia) {
		if (ultimaEstadiaTeveSaida(estadia.getPlacaVeiculo())) {
			throw new IllegalStateException("Veículo informado não está no pátio. Não foi possível registrar saída");
		}

		// Se valor é nulo: Não foi adiantado
		if (estadia.getValor() == null) {
			// Estadia não foi adiantada, então o valor é OBRIGATÓRIO
			if (valor == null) {
				throw new IllegalArgumentException("O valor da saída não pode ser nulo.");
			}

			estadia.setValor(valor);
		}

		estadia.setDataHoraSaida(dataHoraSaida);

		dao.update(estadia);
	}

	public void validarEstadia(DadosEstadia dados) {
		// Testa entrada sendo depois da saída
		if (dados.dataHoraSaida() != null) {			
			if (TempoUtils.calcularDiferencaMinutos(dados.dataHoraEntrada(), dados.dataHoraSaida()) < 0) {
				throw new IllegalArgumentException("Hora de saída é antes da hora de entrada");
			};
		}

		if (dados.valor() != null && dados.valor() < 0) {
			throw new IllegalArgumentException("Valor não pode ser negativo");
		}

		if (dados.placaVeiculo() == null || dados.placaVeiculo().isBlank()) {
			throw new IllegalArgumentException("Placa é obrigatória");
		}
	}
}
