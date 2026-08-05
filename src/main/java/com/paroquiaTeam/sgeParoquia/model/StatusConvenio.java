package com.paroquiaTeam.sgeParoquia.model;

public enum StatusConvenio {
	ATIVO("Ativo"),
	ATRASO("Em Atraso"),
	DESATIVADO("Desativado");
	
	private String nome;
	
	private StatusConvenio(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
