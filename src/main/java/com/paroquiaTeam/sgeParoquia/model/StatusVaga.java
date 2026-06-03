package com.paroquiaTeam.sgeParoquia.model;

public enum StatusVaga {
	LIVRE("Livre"),
	OCUPADA("Ocupada"),
	RESERVADAEVENTO("Reservada para Evento"),
	INDISPONIVEL("Indísponivel");
	
	private String nome;
	
	private StatusVaga(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
