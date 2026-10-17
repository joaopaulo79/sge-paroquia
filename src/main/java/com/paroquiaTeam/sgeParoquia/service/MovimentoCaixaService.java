package com.paroquiaTeam.sgeParoquia.service;

import java.util.List;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.MovimentoCaixaDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosMovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;

import jakarta.persistence.EntityNotFoundException;

public class MovimentoCaixaService {
	private MovimentoCaixaDAO dao = new MovimentoCaixaDAO();
	
	public Optional<MovimentoCaixa> buscarPorId(long id) {
		return dao.getById(id);
	}
	
	public MovimentoCaixa obterPorId(long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Movimento de caixa não encontrado para o id "+id));
	}
	
	public List<MovimentoCaixa> buscarPorCaixa(Long idCaixa) {
		return dao.getByCaixa(idCaixa);
	}
	
	public List<MovimentoCaixa> buscarPorEstadia(Long idEstadia) {
		return dao.getByEstadia(idEstadia);
	}
	
	public void salvar(DadosMovimentoCaixa dados) {
		validarMovimento(dados);
		
		Caixa caixa = new CaixaService().obterPorId(dados.idCaixa());
		MovimentoCaixa movimento;
		if (dados.idEstadia() == null) {
			movimento = dados.paraMovimentoCaixa(caixa);
		} else {
			Estadia estadia = new EstadiaService().obterPorId(dados.idEstadia());
			movimento = dados.paraMovimentoCaixa(caixa, estadia);
		}
		
		dao.save(movimento);
	}
	
	public void excluir(MovimentoCaixa movimento) {
		dao.delete(movimento.getId());
	}
	
	private void validarMovimento(DadosMovimentoCaixa dados) {
		if (dados.formaPagamento() == null) {
			throw new IllegalArgumentException("Forma de pagamento é obrigatória");
		}
		
		if (dados.tipoMovimento() == null) {
			throw new IllegalArgumentException("Tipo de movimento é obrigatório");
		}

		if (!new CaixaService().existePorId(dados.idCaixa())) {
			throw new IllegalArgumentException("Não foi possível encontrar caixa com id informado");
		}
		
		if (dados.idEstadia() != null) {
			if (!new EstadiaService().existePorId(dados.idEstadia())) {
				throw new IllegalArgumentException("Não foi possível encontrar estadia com id informado");
			} 
		}
	}
}
