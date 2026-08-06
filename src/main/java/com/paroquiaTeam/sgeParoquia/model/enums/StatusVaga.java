package com.paroquiaTeam.sgeParoquia.model.enums;

public enum StatusVaga {
	LIVRE("Livre"),
	OCUPADA("Ocupada"),
	RESERVADAEVENTO("Reservada para Evento"),
	BLOQUEADA("Bloqueada");
	
	private String nome;
	
	private StatusVaga(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
