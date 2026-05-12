package com.paroquiaTeam.sgeParoquia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRECIFICACAO_FRACIONADA")
public class PrecificacaoFracionada {
	@Id
	private long id = 1;
	
	@Column
	private int tolerancia;
	
	@Column(name = "valor_meia_hora")
	private double valorMeiaHora;
	
	@Column(name = "valor_hora")
	private double valorHora;
	
	@Column(name = "valor_diaria")
	private double valorDiaria;
	
	@Column(name = "valor_meia_hora_moto")
	private double valorMeiaHoraMoto;
	
	@Column(name = "valor_hora_moto")
	private double valorHoraMoto;
	
	@Column(name = "valor_diaria_moto")
	private double valorDiariaMoto;
	
	public PrecificacaoFracionada() {}

	public PrecificacaoFracionada(int tolerancia, double valorMeiaHora, double valorHora, double valorDiaria,
			double valorMeiaHoraMoto, double valorHoraMoto, double valorDiariaMoto) {
		this.tolerancia = tolerancia;
		this.valorMeiaHora = valorMeiaHora;
		this.valorHora = valorHora;
		this.valorDiaria = valorDiaria;
		this.valorMeiaHoraMoto = valorMeiaHoraMoto;
		this.valorHoraMoto = valorHoraMoto;
		this.valorDiariaMoto = valorDiariaMoto;
	}


	
	public long getId() {
		return id;
	}
	
	
	
	public int getTolerancia() {
		return tolerancia;
	}
	public void setTolerancia(int tolerancia) {
		this.tolerancia = tolerancia;
	}

	
	
	public double getValorMeiaHora() {
		return valorMeiaHora;
	}
	public void setValorMeiaHora(double valorMeiaHora) {
		this.valorMeiaHora = valorMeiaHora;
	}

	
	
	public double getValorHora() {
		return valorHora;
	}
	public void setValorHora(double valorHora) {
		this.valorHora = valorHora;
	}

	
	
	public double getValorDiaria() {
		return valorDiaria;
	}
	public void setValorDiaria(double valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	
	
	public double getValorMeiaHoraMoto() {
		return valorMeiaHoraMoto;
	}
	public void setValorMeiaHoraMoto(double valorMeiaHoraMoto) {
		this.valorMeiaHoraMoto = valorMeiaHoraMoto;
	}

	
	
	public double getValorHoraMoto() {
		return valorHoraMoto;
	}
	public void setValorHoraMoto(double valorHoraMoto) {
		this.valorHoraMoto = valorHoraMoto;
	}

	
	
	public double getValorDiariaMoto() {
		return valorDiariaMoto;
	}
	public void setValorDiariaMoto(double valorDiariaMoto) {
		this.valorDiariaMoto = valorDiariaMoto;
	}	
}
