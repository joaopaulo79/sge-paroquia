package com.paroquiaTeam.sgeParoquia.model.dto;

public record DadosPrecificacaoPorHora(
		int tolerancia,
		double entrada, 
		double hora, 
		double diaria,
		double entradaMoto, 
		double horaMoto, 
		double diariaMoto
) {}