package com.paroquiaTeam.sgeParoquia.model.enums;

public enum StatusConvenio {
	ATIVO("Ativo"),
	ATRASO("Mensalidade Em Atraso"),
	DESATIVADO("Desativado");

	private final String nome;

	private StatusConvenio(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
