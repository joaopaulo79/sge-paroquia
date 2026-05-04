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
@Table(name = "VAGA")
public class Vaga {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_vaga")
	private Long id;
	
	@Column(name = "status_ocupacao")
	private boolean ocupada;
	
	@Column(name = "placa_ocupador", nullable = true)
	private String placa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_vaga")
	private TipoVaga tipo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_reserva")
	private TipoReservaVaga reserva;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "id_convenio")
	private Convenio convenio;

	
	
	public Vaga() {}

	public Vaga(Long id, boolean ocupada, String placa, TipoVaga tipo, 
			TipoReservaVaga reserva) {
		this.id = id;
		this.ocupada = ocupada;
		this.placa = placa;
		this.tipo = tipo;
		this.reserva = reserva;
	}
	
	public Vaga(Long id, boolean ocupada, String placa, TipoVaga tipo, 
				TipoReservaVaga reserva, Convenio convenio) {
		this.id = id;
		this.ocupada = ocupada;
		this.placa = placa;
		this.tipo = tipo;
		this.reserva = reserva;
		this.convenio = convenio;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	public boolean isOcupada() {
		return ocupada;
	}

	public void setOcupada(boolean ocupada) {
		this.ocupada = ocupada;
	}

	
	
	public TipoVaga getTipo() {
		return tipo;
	}

	public void setTipo(TipoVaga tipo) {
		this.tipo = tipo;
	}

	
	
	public TipoReservaVaga getReserva() {
		return reserva;
	}

	public void setReserva(TipoReservaVaga reserva) {
		this.reserva = reserva;
	}

	
	
	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
}
