package com.paroquiaTeam.sgeParoquia.model.dto;

public record DadosPrecificacaoFracionada(
		int tolerancia,
		double meiaHora, 
		double hora, 
		double diaria,
		double meiaHoraMoto, 
		double horaMoto, 
		double diariaMoto
) {}