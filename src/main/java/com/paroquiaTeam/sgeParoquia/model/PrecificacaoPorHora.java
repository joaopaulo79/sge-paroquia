package com.paroquiaTeam.sgeParoquia.model;

import com.paroquiaTeam.sgeParoquia.service.Calculavel;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRECIFICACAO_POR_HORA")
public class PrecificacaoPorHora implements Calculavel {
	@Id
	private long id = 1;
	
	@Column(nullable = false)
	private int tolerancia;
	
	@Column(name = "valor_entrada", nullable = false)
	private double valorEntrada;
	
	@Column(name = "valor_hora", nullable = false)
	private double valorHora;
	
	@Column(name = "valor_diaria", nullable = false)
	private double valorDiaria;
	
	@Column(name = "valor_entrada_moto", nullable = false)
	private double valorEntradaMoto;
	
	@Column(name = "valor_hora_moto", nullable = false)
	private double valorHoraMoto;
	
	@Column(name = "valor_diaria_moto", nullable = false)
	private double valorDiariaMoto;
	
	
	
	public PrecificacaoPorHora() {}

	public PrecificacaoPorHora(int tolerancia, double valorEntrada, 
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

	public double calcular(long tempoEmMinutos, boolean ehMoto) {
		if (tempoEmMinutos <= tolerancia) return 0.0;
		
		double valor = (ehMoto) ? valorEntradaMoto : valorEntrada;
		long horas = tempoEmMinutos/60;
		valor += horas * ((ehMoto) ? valorHoraMoto : valorHora);
		
		return valor;
	}
	
	@Override
	public String toString() {
		return "PrecificacaoPorHora [id=" + id + ", tolerancia=" + tolerancia + ", valorEntrada=" + valorEntrada
				+ ", valorHora=" + valorHora + ", valorDiaria=" + valorDiaria + ", valorEntradaMoto=" + valorEntradaMoto
				+ ", valorHoraMoto=" + valorHoraMoto + ", valorDiariaMoto=" + valorDiariaMoto + "]";
	}
}
