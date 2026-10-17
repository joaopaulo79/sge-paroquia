package com.paroquiaTeam.sgeParoquia.model.dto;

import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPagamento;

public record DadosMovimentoCaixa(
		double valor, 
		TipoPagamento formaPagamento, 
		TipoMovimento tipoMovimento, 
		String descricao,
		long idCaixa,
		Long idEstadia) {
	
	public MovimentoCaixa paraMovimentoCaixa(Caixa caixa) {
		return new MovimentoCaixa(
				this.valor, 
				this.formaPagamento, 
				this.tipoMovimento, 
				this.descricao,
				caixa);
	}
	
	public MovimentoCaixa paraMovimentoCaixa(Caixa caixa, Estadia estadia) {
		return new MovimentoCaixa(
				this.valor, 
				this.formaPagamento, 
				this.tipoMovimento, 
				this.descricao,
				caixa,
				estadia);
	}
}
