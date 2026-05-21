package com.paroquiaTeam.sgeParoquia.model;

public enum TipoPrecificacao {
	FRACIONADA("Fracionada"),
	POR_HORA("Por Hora");
	
	private final String descricao;

    TipoPrecificacao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
