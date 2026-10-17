package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.dao.CaixaDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCaixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.utils.TempoUtils;

import jakarta.persistence.EntityNotFoundException;

public class CaixaService {
	private CaixaDAO dao = new CaixaDAO();
	
	public Optional<Caixa> buscarPorId(long id) {
		return dao.getById(id);
	}
	
	public Caixa obterPorId(long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado para o id "+id));
	}
	
	public Optional<Caixa> buscarUltimo() {
		return dao.getLast();
	}
	
	public Caixa obterUltimo() {
		return buscarUltimo()
				.orElseThrow(() -> new EntityNotFoundException("Nenhum caixa encontrado"));
	} 
	
	public void abrir(DadosCaixa dados) {
		validarAbertura(dados);
		
		Usuario user = new UsuarioService().obterPorId(dados.idOperador());
		Caixa caixa = dados.paraCaixa(user);
		
		dao.save(caixa);
		
		SessaoSistema.getInstancia().setCaixa(caixa);
	}
	
	public void fechar(Caixa caixa, LocalDateTime dataHoraFechamento) {
		validarFechamento(new DadosCaixa(
				caixa.getDataHoraAbertura(), 
				dataHoraFechamento,
				caixa.getValorInicial(),
				caixa.getOperador().getId()));
		
		caixa.setDataHoraFechamento(dataHoraFechamento);
		dao.update(caixa);
	}
	
	public void excluir(Caixa caixa) {
		if (!caixa.getMovimentos().isEmpty()) {
			throw new IllegalStateException("Caixa tem movimentos registrados. Não foi possível excluir");
		}
		
		if (SessaoSistema.getInstancia().getUserLogado().getTipo() != TipoUsuario.ADMINISTRADOR) {
			throw new IllegalStateException("Usuário");
		}
	}
	
	private void validarAbertura(DadosCaixa dados) {
		if (dados.dataHoraFechamento() != null) {
			throw new IllegalArgumentException("Horário de fechamento deve ser nulo quando abrir caixa");
		}
		
		validarCaixa(dados);
		
		Optional<Caixa> talvezCaixa = dao.getLast();
		if (talvezCaixa.isPresent()) {
			Caixa caixaAtual = talvezCaixa.get();
			if (caixaAtual.getDataHoraFechamento() == null) {
				throw new IllegalStateException("Último caixa não foi fechado. Não é possível abrir novo caixa");
			}
		}
	}
	
	private void validarFechamento(DadosCaixa dados) {
		if (dados.dataHoraFechamento() == null) {
			throw new IllegalArgumentException("Horário de abertura é obrigatório");
		}
		
		if (TempoUtils.calcularDiferencaMinutos(dados.dataHoraAbertura(), dados.dataHoraFechamento()) < 0) {
			throw new IllegalArgumentException("Horário de fechamento não pode ser antes do horário de abertura");
		}
		
		validarCaixa(dados);
	}
	
	private void validarCaixa(DadosCaixa dados) {
		if (dados.dataHoraAbertura() == null) {
			throw new IllegalArgumentException("Horário de abertura é obrigatório");
		}
		
		if (dados.valorInicial() < 0) {
			throw new IllegalArgumentException("Valor inicial não pode ser negativo");
		}

		if (!new UsuarioService().existePorId(dados.idOperador())) {
			throw new IllegalArgumentException("Não foi possível encontrar operador com id informado");
		}
	}
}
