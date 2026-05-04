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
	
	
	
	public PrecionamentoPorHora() {}

	public PrecionamentoPorHora(int tolerancia, double valorEntrada, 
			double valorHora, double valorDiaria, double valorEntradaMoto, 
			double valorHoraMoto, double valorDiariaMoto) {
		this.tolerancia = tolerancia;
		this.valorEntrada = valorEntrada;
		this.valorHora = valorHora;
		this.valorDiaria = valorDiaria;
		this.valorEntradaMoto = valorEntradaMoto;
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

	
	
	public double getValorEntrada() {
		return valorEntrada;
	}
	public void setValorEntrada(double valorEntrada) {
		this.valorEntrada = valorEntrada;
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

	
	
	public double getValorEntradaMoto() {
		return valorEntradaMoto;
	}
	public void setValorEntradaMoto(double valorEntradaMoto) {
		this.valorEntradaMoto = valorEntradaMoto;
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
