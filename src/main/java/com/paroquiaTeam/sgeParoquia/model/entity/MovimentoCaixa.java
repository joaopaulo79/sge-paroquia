package com.paroquiaTeam.sgeParoquia.model.entity;

import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MOVIMENTO_CAIXA")
public class MovimentoCaixa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movimento")
	private Long id;
	
	@Column(name = "valor_movimento", nullable = false)
	private double valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = false)
	private TipoPagamento formaPagamento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_movimento", nullable = false)
	private TipoMovimento tipoMovimento;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_caixa")
	private Caixa caixa;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_estadia")
	private Estadia estadia;

	
	
	public MovimentoCaixa() {}
	
	public MovimentoCaixa(Long id, double valor, TipoPagamento formaPagamento, 
			TipoMovimento tipoMovimento, Caixa caixa) {
		this.id = id;
		this.valor = valor;
		this.formaPagamento = formaPagamento;
		this.tipoMovimento = tipoMovimento;
		this.caixa = caixa;
	}
	
	public MovimentoCaixa(Long id, double valor, TipoPagamento formaPagamento, 
			TipoMovimento tipoMovimento, Caixa caixa, Estadia estadia) {
		this.id = id;
		this.valor = valor;
		this.formaPagamento = formaPagamento;
		this.tipoMovimento = tipoMovimento;
		this.caixa = caixa;
		this.estadia = estadia;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}

	
	
	public TipoPagamento getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(TipoPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	
	
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	
	
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	
	
	public Estadia getEstadia() {
		return estadia;
	}
	public void setEstadia(Estadia estadia) {
		this.estadia = estadia;
	}

	@Override
	public String toString() {
		return "MovimentoCaixa [id=" + id + ", valor=" + valor + ", formaPagamento=" + formaPagamento
				+ ", tipoMovimento=" + tipoMovimento + ", caixa=" + caixa + ", estadia=" + estadia + "]";
	}
}
