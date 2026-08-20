package com.paroquiaTeam.sgeParoquia.model.dto;

import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;

public record DadosVeiculo(
		String placa,
		String marca,
		String modelo,
		int ano,
		String cor,
		String observacoes,
		TipoVeiculo tipo,
		long idCliente
		) {
	public Veiculo paraVeiculo(Cliente cliente) {
		return new Veiculo(this.placa,
			this.marca,
			this.modelo,
			this.cor,
			this.observacoes,
			this.tipo,
			cliente);
	}
}
