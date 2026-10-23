package com.paroquiaTeam.sgeParoquia.model.enums;

public enum TipoCliente {
	AVULSO("Avulso"),
	DIZIMISTA("Dizimista"),
	MENSALISTA("Mensalista"),
	CONVENIADO("Conveniado");
	
	private final String nome;

	private TipoCliente(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
