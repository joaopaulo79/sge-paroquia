package com.paroquiaTeam.sgeParoquia.model.dto;

import java.time.LocalDate;

import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusConvenio;

public record DadosConvenio(
		String cnpj,
		String nome,
		double mensalidade,
		int vagasCarro,
		int vagasMoto,
		StatusConvenio status,
		double cobrancaIndividual,
		LocalDate dataVencimento
) {
	public Convenio paraConvenio() {
	    return new Convenio(
	        this.cnpj,
	        this.nome,
	        this.mensalidade,
	        this.vagasCarro,
	        this.vagasMoto,
	        this.status,
	        this.cobrancaIndividual,
	        this.dataVencimento
	    );
	}
}
