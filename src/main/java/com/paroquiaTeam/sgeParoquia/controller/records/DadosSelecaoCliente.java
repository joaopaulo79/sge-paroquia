package com.paroquiaTeam.sgeParoquia.controller.records;

import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

public record DadosSelecaoCliente(
		Long id,
		String cpf,
		String nome,
		TipoCliente tipo
		) {

}
