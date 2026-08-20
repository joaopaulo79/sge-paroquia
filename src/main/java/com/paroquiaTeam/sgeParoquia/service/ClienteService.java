package com.paroquiaTeam.sgeParoquia.service;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.ClienteDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

import jakarta.persistence.EntityNotFoundException;

public class ClienteService {
	private ClienteDAO dao = new ClienteDAO();
	
	public Optional<Cliente> buscarPorId(long id) {
		return dao.getById(id);
	}
	
	public Cliente obterPorId(long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o id "+id));
	}
	
	public Optional<Cliente> buscarPorCpf(String cpf) {
		return dao.getByCpf(cpf);
	}
	
	public Cliente obterPorCpf(String cpf) {
		return buscarPorCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o id "+cpf));
	}
	
	public void salvar(DadosCliente dados) {
		validarClienteNovo(dados);
		
		Cliente cl;
		
		if (dados.tipo() == TipoCliente.CONVENIADO) {
			Convenio con = new ConvenioService().obterPorId(dados.idConvenio());
			cl = dados.paraCliente(con);
		} else {
			cl = dados.paraCliente();
		}
		
		dao.save(cl);
	}
	
	public void atualizar(DadosCliente dadosNovos, Cliente cliente) {
		validarAtualizacaoCliente(dadosNovos, cliente);
		
		cliente.setNome(dadosNovos.nome());
		cliente.setCpf(dadosNovos.cpf());
		cliente.setTelefone(dadosNovos.telefone());
		cliente.setTipo(dadosNovos.tipo());
		cliente.setStatus(dadosNovos.status());
		
		
		if (dadosNovos.tipo() == TipoCliente.CONVENIADO) {
			if (cliente.getTipo() == TipoCliente.CONVENIADO) {				
				if (dadosNovos.idConvenio() != cliente.getConvenio().getId()) {
					Convenio convenioNovo = new ConvenioService().obterPorId(dadosNovos.idConvenio());
					cliente.setConvenio(convenioNovo);
				}
			} else {
				Convenio convenioNovo = new ConvenioService().obterPorId(dadosNovos.idConvenio());
				cliente.setConvenio(convenioNovo);
			}
		}
	}
	
	public void excluir(Cliente cliente) {
		if (!cliente.getVeiculos().isEmpty()) {
	        throw new IllegalArgumentException("Cliente possuí veículos atrelados, não foi possível excluir.");
		}
		
		dao.delete(cliente.getId());
	}
	
	private void validarAtualizacaoCliente(DadosCliente dados, Cliente cliente) {
		validarDadosCliente(dados);
		
		boolean mudouCpf = !dados.cpf().equalsIgnoreCase(cliente.getCpf());
		
		if (mudouCpf && dao.existsByCpf(dados.cpf())) {
	        throw new IllegalArgumentException("Cliente com este cpf já existe.");
		}
	}
	
	private void validarClienteNovo(DadosCliente dados) {
		validarDadosCliente(dados);
		
		if (dao.existsByCpf(dados.cpf())) {
	        throw new IllegalArgumentException("Cliente com este cpf já existe.");
		}
	}
	
	private void validarDadosCliente(DadosCliente dados) {
		// validação de dados nulos
		if (dados.nome().isBlank() || dados.nome() == null) {
	        throw new IllegalArgumentException("Os dados do cliente não podem ser nulos.");
		}
		
		if (dados.cpf().isBlank() || dados.cpf() == null) {
	        throw new IllegalArgumentException("Os dados do cliente não podem ser nulos.");
		}
		
		if (dados.telefone() == null) {
	        throw new IllegalArgumentException("Os dados do cliente não podem ser nulos.");
		}
		
		if (dados.tipo() == null) {
	        throw new IllegalArgumentException("Os dados do cliente não podem ser nulos.");
		}
		
		if (dados.tipo() == TipoCliente.MENSALISTA || dados.tipo() == TipoCliente.DIZIMISTA) {
			if (dados.telefone() == null || dados.telefone().isBlank()) {
		        throw new IllegalArgumentException("Telefone é obrigatório para mensalistas.");
			}
		}
		
		if (dados.tipo() == TipoCliente.CONVENIADO && dados.idConvenio() == null) {
	        throw new IllegalArgumentException("Cliente convêniado deve ser conectado a um convenio, obrigatóriamente.");
		}
	}
}
