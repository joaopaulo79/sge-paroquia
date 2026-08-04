package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.EstadiaDAO;
import com.paroquiaTeam.sgeParoquia.model.Estadia;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;

public class EstadiaService {
	public record DadosEstadia(
			Long id,
			LocalDateTime dataHoraEntrada,
			LocalDateTime dataHoraSaida,
			double valor,
			String placaVeiculo) {}
	
	private EstadiaDAO dao = new EstadiaDAO();

	public boolean estadiaTeveSaida(String placa) {
		Optional<Estadia> talvezEstadia = dao.getLastByPlaca(placa);
		if (talvezEstadia.isEmpty()) {
			return true; // Não tem estadia, logo trata como se tivesse saída
		}
		
		Estadia estadia = talvezEstadia.get();
		if (estadia.getDataHoraSaida() == null) {
			return false; // estadia sem dataHora de saida
		}
		return true; // se chegou aqui, estadie tem dataHora de saida
	}
	
	public Estadia buscarPorPlaca(String placa) {
		return dao.getLastByPlaca(placa).get();
	}
	
	public void salvar(Estadia estadia) {
		dao.save(estadia);
	}
	
	private void validarEstadia(Estadia estadia) {
		// Testa entrada sendo depois da saída
		if (TempoUtils.calcularDiferencaMinutos(estadia.getDataHoraEntrada(), estadia.getDataHoraSaida()) < 0) {
			throw new IllegalArgumentException("Data de saída é antes da data de entrada");
		};
		
		if (estadia.getValor() < 0) {
			throw new IllegalArgumentException("Valor não pode ser negativo");
		}
	}
}
