package com.paroquiaTeam.sgeParoquia.controller.records;

import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;

public record AcoesConvenio(
		Consumer<Long> onEditar,
	    Consumer<Long> onExcluir,
	    Consumer<Long> onAtivar,
	    Consumer<Long> onDesativar,
	    Consumer<Convenio> onRegistrarPagamento
	    ) {

}
