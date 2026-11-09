package com.paroquiaTeam.sgeParoquia.model.enums;

public enum TipoMovimento {
	SANGRIA("Sangria"),
	REFORCO("Reforço"),
	DESPESA("Despesa"),
	ENTRADA("Entrada");
	
	private final String nome;

	private TipoMovimento(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
