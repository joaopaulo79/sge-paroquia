package com.paroquiaTeam.sgeParoquia.model;

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
@Table(name="VEICULO")
public class Veiculo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_veiculo")
	private Long id;
	
	@Column(name="placa_veiculo")
	private String placa;
	
	@Column(name="marca_veiculo")
	private String marca;
	
	@Column(name="modelo_veiculo")
	private String modelo;
	
	@Column(name="cor_veiculo")
	private String cor;
	
	@Column(name="observacoes_veiculo")
	private String observacoes;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_veiculo")
	private TipoVeiculo tipo;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	public TipoVeiculo getTipo() {
		return tipo;
	}
	public void setTipo(TipoVeiculo tipo) {
		this.tipo = tipo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
}
