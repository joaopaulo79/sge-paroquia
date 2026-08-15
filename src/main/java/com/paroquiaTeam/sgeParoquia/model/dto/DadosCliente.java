package com.paroquiaTeam.sgeParoquia.model.dto;

import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

public record DadosCliente(
		String nome,
		String cpf,
		String telefone,
		TipoCliente tipo,
		boolean status,
		Long idConvenio
		) {
	
	public Cliente paraCliente() {
		return new Cliente(
				this.nome,
				this.cpf,
				this.telefone,
				this.tipo,
				this.status
				);
	}
	
	public Cliente paraCliente(Convenio convenio) {
		return new Cliente(
				this.nome,
				this.cpf,
				this.telefone,
				this.tipo,
				this.status,
				convenio
				);
	}
}
