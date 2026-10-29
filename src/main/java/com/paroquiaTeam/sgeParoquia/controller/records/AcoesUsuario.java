package com.paroquiaTeam.sgeParoquia.controller.records;

import java.util.function.Consumer;

public record AcoesUsuario(
		Consumer<Long> onEditar,
	    Consumer<Long> onExcluir,
	    Consumer<Long> onAtivar,
	    Consumer<Long> onDesativar,
	    Consumer<Long> onAlterarSenha
	    ) {

}
