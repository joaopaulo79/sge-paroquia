package com.paroquiaTeam.sgeParoquia.model.dto;

import java.time.LocalDateTime;

import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;

public record DadosCaixa(
		LocalDateTime dataHoraAbertura,
		LocalDateTime dataHoraFechamento,
		double valorInicial,
		long idOperador) {
	
	public Caixa paraCaixa(Usuario operador) {
		return new Caixa(
				this.dataHoraAbertura, 
				this.dataHoraFechamento, 
				this.valorInicial, 
				operador);
	}
}
