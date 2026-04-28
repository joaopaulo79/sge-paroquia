package com.paroquiaTeam.sgeParoquia.model;

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
@Table(name = "CONVENIO")
public class Convenio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_convenio")
	private Long id;
	
	@Column(name = "nome_convenio")
	private String nome;
	
	@Column(name = "mensalidade_convenio")
	private double mensalidade;
	
	@Column(name = "vagas_convenio")
	private int vagasContratadas;
	
	@Column(name = "status_convenio")
	private boolean status;
	
	@Column(name = "cobranca_individual_convenio")
	private double cobrancaIndividual;
	
	@OneToMany(mappedBy = "convenio")
	private List<Cliente> clientes;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
	
	public double getMensalidade() {
		return mensalidade;
	}

	public void setMensalidade(double mensalidade) {
		this.mensalidade = mensalidade;
	}

	
	
	public int getVagasContratadas() {
		return vagasContratadas;
	}

	public void setVagasContratadas(int vagasContratadas) {
		this.vagasContratadas = vagasContratadas;
	}

	
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	
	public double getCobrancaIndividual() {
		return cobrancaIndividual;
	}

	public void setCobrancaIndividual(double cobrancaIndividual) {
		this.cobrancaIndividual = cobrancaIndividual;
	}
	
	

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}
