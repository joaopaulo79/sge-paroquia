package com.paroquiaTeam.sgeParoquia.model.enums;

public enum TipoPrecificacao {
	FRACIONADA("Fracionada"),
	POR_HORA("Por Hora");
	
	private final String nome;

    TipoPrecificacao(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
