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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_vaga")
	private TipoVaga tipo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_reserva")
	private TipoReservaVaga reserva;
	
	@ManyToOne
	@JoinColumn(name = "id_convenio")
	private Convenio convenio;
}
