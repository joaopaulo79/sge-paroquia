package com.paroquiaTeam.sgeParoquia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ESTACIONAMENTO")
public class Estacionamento {
	@Id
	private long id = 1;
	
	@Column(name = "numero_vagas")
	private int numeroDeVagas;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "precificacao_ativa")
	private TipoPrecificacao precificacao;
	
	public Estacionamento() {}

	public Estacionamento(int numeroDeVagas, TipoPrecificacao precificacao) {
		this.numeroDeVagas = numeroDeVagas;
		this.precificacao = precificacao;
	}



	public long getId() {
		return id;
	}
	
	
	
	public int getNumeroDeVagas() {
		return numeroDeVagas;
	}
	public void setNumeroDeVagas(int numeroDeVagas) {
		this.numeroDeVagas = numeroDeVagas;
	}

	
	
	public TipoPrecificacao getPrecificacao() {
		return precificacao;
	}
	public void setPrecificacao(TipoPrecificacao precificacao) {
		this.precificacao = precificacao;
	}
}
