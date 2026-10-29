package com.paroquiaTeam.sgeParoquia.model.enums;

public enum TipoUsuario {
	OPERADOR("Operador"),
	ADMINISTRADOR("Administrador");
	
	private final String nome;

	private TipoUsuario(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
