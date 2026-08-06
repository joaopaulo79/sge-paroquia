package com.paroquiaTeam.sgeParoquia.model.dto;

import java.time.LocalDateTime;

public record DadosEstadia(
		LocalDateTime dataHoraEntrada,
		LocalDateTime dataHoraSaida,
		double valor,
		String placaVeiculo
) {}