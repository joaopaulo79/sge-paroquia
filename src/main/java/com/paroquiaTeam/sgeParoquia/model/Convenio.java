package com.paroquiaTeam.sgeParoquia.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONVENIO")
public class Convenio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_convenio")
	private Long id;
	
	@Column(name = "cnpj_convenio", nullable = false, unique = true)
	private String cnpj;
	
	@Column(name = "nome_convenio", nullable = false)
	private String nome;
	
	@Column(name = "mensalidade_convenio", nullable = false)
	private double mensalidade;
	
	@Column(name = "vagas_carro_convenio", nullable = false)
	private int vagasCarro;
	
	@Column(name = "vagas_moto_convenio", nullable = false)
	private int vagasMoto;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_convenio", nullable = false)
	private StatusConvenio status;
	
	@Column(name = "cobranca_individual_convenio", nullable = false)
	private double cobrancaIndividual;
	
	@Column(name = "data_vencimento_convenio", nullable = false)
	private LocalDate dataVencimento;
	
	@OneToMany(mappedBy = "convenio")
	private List<Cliente> clientes;

	
	
	public Convenio() {}

	public Convenio(String cnpj, String nome, double mensalidade, int vagasCarro, int vagasMoto,
					StatusConvenio status, double cobrancaIndividual, LocalDate dataVencimento) {
		this.cnpj = cnpj;
		this.nome = nome;
		this.mensalidade = mensalidade;
		this.vagasCarro = vagasCarro;
		this.vagasMoto = vagasMoto;
		this.status = status;
		this.cobrancaIndividual = cobrancaIndividual;
		this.dataVencimento = dataVencimento;
	}

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	
	
	public int getVagasCarro() {
		return vagasCarro;
	}
	public void setVagasCarro(int vagasCarro) {
		this.vagasCarro = vagasCarro;
	}


	
	public int getVagasMoto() {
		return vagasMoto;
	}
	public void setVagasMoto(int vagasMoto) {
		this.vagasMoto = vagasMoto;
	}

	
	
	public StatusConvenio getStatus() {
		return status;
	}
	public void setStatus(StatusConvenio status) {
		this.status = status;
	}

	
	
	public double getCobrancaIndividual() {
		return cobrancaIndividual;
	}
	public void setCobrancaIndividual(double cobrancaIndividual) {
		this.cobrancaIndividual = cobrancaIndividual;
	}
	
	

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	
	
	public List<Cliente> getClientes() {
		return clientes;
	}
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}
