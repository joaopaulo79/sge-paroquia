package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosEstadia;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
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
	
	// se Valor não for null, a estadia foi adiantada
	public void registrarEntrada(LocalDateTime dataHoraEntrada, String placa, Double valor) {
		// Redundante mas vale a pena
		if (!ultimaEstadiaTeveSaida(placa)) {
			throw new IllegalStateException("Veículo informado está no pátio. Não foi possível registrar entrada");
		}
		
		DadosEstadia dados = new DadosEstadia(dataHoraEntrada, null, valor, placa);
		salvar(dados);
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
	
	private void validarEstadia(DadosEstadia dados) {
		// Testa entrada sendo depois da saída
		if (TempoUtils.calcularDiferencaMinutos(dados.dataHoraEntrada(), dados.dataHoraSaida()) < 0) {
			throw new IllegalArgumentException("Data de saída é antes da data de entrada");
		};
		
		if (dados.valor() < 0) {
			throw new IllegalArgumentException("Valor não pode ser negativo");
		}
		
		if (dados.placaVeiculo().isBlank() || dados.placaVeiculo().equals(null)) {
			throw new IllegalArgumentException("Placa é obrigatória");
		}
	}
}
