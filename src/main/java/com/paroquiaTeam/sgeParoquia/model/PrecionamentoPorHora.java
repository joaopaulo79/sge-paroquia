package com.paroquiaTeam.sgeParoquia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRECIONAMENTO_POR_HORA")
public class PrecionamentoPorHora {
	@Id
	private long id = 1;
	
	@Column
	private int tolerancia;
	
	@Column(name = "valor_entrada")
	private double valorEntrada;
	
	@Column(name = "valor_hora")
	private double valorHora;
	
	@Column(name = "valor_diaria")
	private double valorDiaria;
	
	@Column(name = "valor_entrada_moto")
	private double valorEntradaMoto;
	
	@Column(name = "valor_hora_moto")
	private double valorHoraMoto;
	
	@Column(name = "valor_diaria_moto")
	private double valorDiariaMoto;
	
	
	protected PrecionamentoPorHora() {}
}
