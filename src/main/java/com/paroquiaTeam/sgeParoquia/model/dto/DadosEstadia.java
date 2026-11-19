package com.paroquiaTeam.sgeParoquia.model.dto;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;

public record DadosEstadia(
		LocalDateTime dataHoraEntrada,
		LocalDateTime dataHoraSaida,
		Double valor,
		String placaVeiculo,
		TipoVeiculo tipoVeiculo
) {
	public Estadia paraEstadia() {
		return new Estadia(
				this.dataHoraEntrada,
				this.dataHoraSaida,
				this.valor,
				this.placaVeiculo,
				this.tipoVeiculo);
	}
}