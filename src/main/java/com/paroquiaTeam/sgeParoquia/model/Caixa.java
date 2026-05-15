package com.paroquiaTeam.sgeParoquia.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="CAIXA")
public class Caixa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_caixa")
	private Long id;
	
	@Column(name = "data_hora_abertura", nullable = false)
	private LocalDateTime dataHoraAbertura;

	@Column(name = "data_hora_fechamento", nullable = true)
	private LocalDateTime dataHoraFechamento;
	
	@Column(name = "valor_inicial", nullable = false)
	private double valorInicial;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_operador")
	private Usuario operador;

	@OneToMany(mappedBy = "caixa")
	private List<MovimentoCaixa> movimentos;
	
	
	
	public Caixa() {}
	
	public Caixa(Long id, LocalDateTime dataHoraAbertura, LocalDateTime dataHoraFechamento, 
			double valorInicial, Usuario operador) {
		this.id = id;
		this.dataHoraAbertura = dataHoraAbertura;
		this.dataHoraFechamento = dataHoraFechamento;
		this.valorInicial = valorInicial;
		this.operador = operador;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public LocalDateTime getDataHoraAbertura() {
		return dataHoraAbertura;
	}
	public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}
	
	
	
	public LocalDateTime getDataHoraFechamento() {
		return dataHoraFechamento;
	}
	public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
		this.dataHoraFechamento = dataHoraFechamento;
	}
	
	
	
	public double getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(double valorInicial) {
		this.valorInicial = valorInicial;
	}
	
	
	
	public Usuario getOperador() {
		return operador;
	}
	public void setOperador(Usuario operador) {
		this.operador = operador;
	}
	
	
	
	public List<MovimentoCaixa> getMovimentos() {
		return movimentos;
	}
	public void setMovimentos(List<MovimentoCaixa> movimentos) {
		this.movimentos = movimentos;
	}

	@Override
	public String toString() {
		return "Caixa [id=" + id + ", dataHoraAbertura=" + dataHoraAbertura + ", dataHoraFechamento="
				+ dataHoraFechamento + ", valorInicial=" + valorInicial + ", operador=" + operador + ", movimentos="
				+ movimentos + "]";
	}
}
