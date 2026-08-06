package com.paroquiaTeam.sgeParoquia.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="ESTADIA")
public class Estadia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estadia")
	private Long id;
	
	@Column(name = "data_hora_entrada", nullable = false)
	private LocalDateTime dataHoraEntrada;

	@Column(name = "data_hora_saida", nullable = true)
	private LocalDateTime dataHoraSaida;
	
	@Column(name = "valor_estadia", nullable = true)
	private double valor;
	
	@Column(name = "placa_veiculo", nullable = false)
	private String placaVeiculo;

	@OneToMany(mappedBy = "estadia")
	private List<MovimentoCaixa> movimentos;
	
	
	
	public Estadia() {}
	
	public Estadia(Long id, LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida, 
				   double valor, String placaVeiculo) {
		this.id = id;
		this.dataHoraEntrada = dataHoraEntrada;
		this.dataHoraSaida = dataHoraSaida;
		this.valor = valor;
		this.placaVeiculo = placaVeiculo;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public LocalDateTime getDataHoraEntrada() {
		return dataHoraEntrada;
	}
	public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
		this.dataHoraEntrada = dataHoraEntrada;
	}

	
	
	public LocalDateTime getDataHoraSaida() {
		return dataHoraSaida;
	}
	public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}

	
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}

	
	
	public String getPlacaVeiculo() {
		return placaVeiculo;
	}
	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}
	
	
	
	public List<MovimentoCaixa> getMovimentos() {
		return movimentos;
	}
	public void setMovimentos(List<MovimentoCaixa> movimentos) {
		this.movimentos = movimentos;
	}

	@Override
	public String toString() {
		return "Estadia [id=" + id + ", dataHoraEntrada=" + dataHoraEntrada + ", dataHoraSaida=" + dataHoraSaida
				+ ", valor=" + valor + ", placaVeiculo=" + placaVeiculo + ", movimentos=" + movimentos + "]";
	}
}
