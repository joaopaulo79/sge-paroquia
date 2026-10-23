package com.paroquiaTeam.sgeParoquia.model.dto;

import java.time.LocalDate;

import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

public record DadosCliente(
		String nome,
		String cpf,
		String telefone,
		TipoCliente tipo,
		StatusCliente status,
		LocalDate dataVencimentoMensalidade,
		Double valorMensalidade,
		Long idConvenio
		) {

	public Cliente paraCliente() {
		return new Cliente(
				this.nome,
				this.cpf,
				this.telefone,
				this.tipo,
				this.status,
				this.dataVencimentoMensalidade,
				this.valorMensalidade
				);
	}

	public Cliente paraCliente(Convenio convenio) {
		return new Cliente(
				this.nome,
				this.cpf,
				this.telefone,
				this.tipo,
				this.status,
				this.dataVencimentoMensalidade,
				this.valorMensalidade,
				convenio
				);
	}
}
