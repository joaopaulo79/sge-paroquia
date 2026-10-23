package com.paroquiaTeam.sgeParoquia.controller.records;

import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;

public record AcoesCliente(
		Consumer<Long> onEditar,
	    Consumer<Long> onExcluir,
	    Consumer<Long> onAtivar,
	    Consumer<Long> onDesativar,
	    Consumer<Cliente> onRegistrarPagamento
	    ) {

}
