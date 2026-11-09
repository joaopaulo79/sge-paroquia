package com.paroquiaTeam.sgeParoquia.model.enums;

public enum TipoPagamento {
	DINHEIROFISICO("Dinheiro"),
	DEBITO("Débito"),
	CREDITO("Crédito"),
	PIX("PIX"),
	PIXMAQUINA("PIX pela maquininha");
	
	private final String nome;

	private TipoPagamento(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
