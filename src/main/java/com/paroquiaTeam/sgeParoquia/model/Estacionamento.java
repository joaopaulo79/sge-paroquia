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
	@Column(name = "precionamento_ativo")
	private TipoPrecionamento precionamento;
	
	public Estacionamento() {}

	public Estacionamento(int numeroDeVagas, TipoPrecionamento precionamento) {
		this.numeroDeVagas = numeroDeVagas;
		this.precionamento = precionamento;
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

	
	
	public TipoPrecionamento getPrecionamento() {
		return precionamento;
	}
	public void setPrecionamento(TipoPrecionamento precionamento) {
		this.precionamento = precionamento;
	}
}
