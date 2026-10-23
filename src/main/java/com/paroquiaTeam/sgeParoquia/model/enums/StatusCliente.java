package com.paroquiaTeam.sgeParoquia.model.enums;

public enum StatusCliente {
	ATIVO("Ativo"),
	ATRASO("Mensalidade Em Atraso"),
	DESATIVADO("Desativado");

	private final String nome;

	private StatusCliente(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
