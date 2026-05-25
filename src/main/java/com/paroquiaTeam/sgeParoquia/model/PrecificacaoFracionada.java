package com.paroquiaTeam.sgeParoquia.model;

import com.paroquiaTeam.sgeParoquia.service.Calculavel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRECIFICACAO_FRACIONADA")
public class PrecificacaoFracionada implements Calculavel {
	@Id
	private long id = 1;
	
	@Column(nullable = false)
	private int tolerancia;
	
	@Column(name = "valor_meia_hora", nullable = false)
	private double valorMeiaHora;
	
	@Column(name = "valor_hora", nullable = false)
	private double valorHora;
	
	@Column(name = "valor_diaria", nullable = false)
	private double valorDiaria;
	
	@Column(name = "valor_meia_hora_moto", nullable = false)
	private double valorMeiaHoraMoto;
	
	@Column(name = "valor_hora_moto", nullable = false)
	private double valorHoraMoto;
	
	@Column(name = "valor_diaria_moto", nullable = false)
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
	
	public double calcular(long tempoEmMinutos, boolean ehMoto) {
		if (tempoEmMinutos <= tolerancia) {
			return 0.0;
		} else if (tempoEmMinutos <= 30) {
			return (ehMoto) ? valorMeiaHoraMoto : valorMeiaHora;
		} else if (tempoEmMinutos <= 60) {
			return (ehMoto) ? valorHoraMoto : valorHora;
		} else {
			return (ehMoto) ? valorDiariaMoto : valorDiaria;
		}
	}

	@Override
	public String toString() {
		return "PrecificacaoFracionada [id=" + id + ", tolerancia=" + tolerancia + ", valorMeiaHora=" + valorMeiaHora
				+ ", valorHora=" + valorHora + ", valorDiaria=" + valorDiaria + ", valorMeiaHoraMoto="
				+ valorMeiaHoraMoto + ", valorHoraMoto=" + valorHoraMoto + ", valorDiariaMoto=" + valorDiariaMoto + "]";
	}
}
