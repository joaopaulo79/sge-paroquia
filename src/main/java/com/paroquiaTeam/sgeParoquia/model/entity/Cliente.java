package com.paroquiaTeam.sgeParoquia.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.paroquiaTeam.sgeParoquia.model.enums.StatusCliente;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLIENTE")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Long id;

	@Column(name = "nome_cliente", nullable = false)
	private String nome;

	@Column(name = "cpf_cliente", nullable = false, unique = true)
	private String cpf;

	@Column(name = "telefone_cliente")
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cliente", nullable = false)
	private TipoCliente tipo;

	@Column(name = "status_cliente", nullable = false)
	private StatusCliente status;
	
	@Column(name = "data_vencimento_mensalidade", nullable = true)
	private LocalDate dataVencimentoMensalidade;
	
	@Column(name = "valor_mensalidade", nullable = true)
	private Double valorMensalidade;

	@OneToMany(mappedBy = "cliente")
	private List<Veiculo> veiculos;

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_convenio")
	private Convenio convenio;



	public Cliente() {}

	public Cliente(String nome, String cpf, String telefone, TipoCliente tipo,
			StatusCliente status, LocalDate dataVencimentoMensalidade, Double valorMensalidade) {
		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.tipo = tipo;
		this.status = status;
		this.dataVencimentoMensalidade = dataVencimentoMensalidade;
		this.valorMensalidade = valorMensalidade;
	}

	public Cliente(String nome, String cpf, String telefone, TipoCliente tipo,
			StatusCliente status, LocalDate dataVencimentoMensalidade, Double valorMensalidade, Convenio convenio) {
		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.tipo = tipo;
		this.status = status;
		this.dataVencimentoMensalidade = dataVencimentoMensalidade;
		this.valorMensalidade = valorMensalidade;
		this.convenio = convenio;
	}



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



	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}



	public TipoCliente getTipo() {
		return tipo;
	}
	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}



	public StatusCliente getStatus() {
		return status;
	}
	public void setStatus(StatusCliente status) {
		this.status = status;
	}
	public boolean isAtivo() {
		return status == StatusCliente.ATIVO;
	}
	public boolean isDesativado() {
		return status == StatusCliente.DESATIVADO;
	}

	

	public LocalDate getDataVencimentoMensalidade() {
		return dataVencimentoMensalidade;
	}
	public void setDataVencimentoMensalidade(LocalDate dataVencimentoMensalidade) {
		this.dataVencimentoMensalidade = dataVencimentoMensalidade;
	}



	public Double getValorMensalidade() {
		return valorMensalidade;
	}
	public void setValorMensalidade(Double valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}



	public List<Veiculo> getVeiculos() {
		return veiculos;
	}
	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}



	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", telefone=" + telefone + ", tipo=" + tipo
				+ ", status=" + status + ", veiculos=" + veiculos + ", convenio=" + convenio + "]";
	}
}
