package com.paroquiaTeam.sgeParoquia.view;

import com.paroquiaTeam.sgeParoquia.model.TipoPrecificacao;

public enum TipoPrecificacaoViewModel {
	FRACIONADA(TipoPrecificacao.FRACIONADA, "Fracionada", new String[]{"Até 30 minutos", "Até 1 hora", "Diária"}),
	POR_HORA(TipoPrecificacao.POR_HORA, "Por Hora", new String[]{"Entrada", "Adicional Por Hora", "Diária"});

	
	public final TipoPrecificacao tipo;
	public final String label;
	public final String[] labelsCampos;
	
	TipoPrecificacaoViewModel(TipoPrecificacao tipo, String label, String[] labelsCampos) {
		this.tipo = tipo;
		this.label = label;
		this.labelsCampos = labelsCampos;
	}
	
	@Override public String toString() { return label; };
}
