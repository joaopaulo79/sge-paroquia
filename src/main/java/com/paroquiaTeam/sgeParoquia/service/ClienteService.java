package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.ClienteDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosCliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusCliente;
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
	
	public Optional<Cliente> buscarComVeiculosPorId(long id) {
		return dao.getByIdWithVeiculos(id);
	}

	public Cliente obterComVeiculosPorId(long id) {
		return buscarComVeiculosPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o id "+id));
	}

	public Optional<Cliente> buscarPorCpf(String cpf) {
		return dao.getByCpf(cpf);
	}

	public Cliente obterPorCpf(String cpf) {
		return buscarPorCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o cpf "+cpf));
	}
	
	public List<Cliente> obterTodos() {
		return dao.getAll();
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
		cliente.setDataVencimentoMensalidade(dadosNovos.dataVencimentoMensalidade());
		cliente.setValorMensalidade(dadosNovos.valorMensalidade());


		if (dadosNovos.tipo() == TipoCliente.CONVENIADO) {
			// Testando se cliente já era conveniado, e o id do convenio mudou
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

	public void ativar(Long id) {
		Cliente cliente = obterComVeiculosPorId(id);
		if (cliente.getTipo() == TipoCliente.MENSALISTA && 
				cliente.getDataVencimentoMensalidade().isBefore(LocalDate.now())) {
			throw new IllegalStateException("Mensalidade do cliente atrasada. Não foi possível ativar");
		}
		
		verificarSeVeiculosDoClienteEstaoNoPatio(cliente);
		
		cliente.setStatus(StatusCliente.ATIVO);
		dao.update(cliente);
	}
	
	public void marcarAtraso(Long id) {
		Cliente cliente = obterComVeiculosPorId(id);
		if (cliente.getTipo() != TipoCliente.MENSALISTA) {
			throw new IllegalStateException("Cliente não é mensalista. Não foi possível marcar atraso");
		}
		
		if (cliente.getDataVencimentoMensalidade().isBefore(LocalDate.now())) {
			throw new IllegalStateException("Data de vencimento é posterior à data atual. Não foi possível marcar atraso");
		}
		
		verificarSeVeiculosDoClienteEstaoNoPatio(cliente);
		
		cliente.setStatus(StatusCliente.ATRASO);
		dao.update(cliente);
	}
	
	public void desativar(Long id) {
		Cliente cliente = obterComVeiculosPorId(id);
		verificarSeVeiculosDoClienteEstaoNoPatio(cliente);

		
		cliente.setStatus(StatusCliente.DESATIVADO);
		dao.update(cliente);
	}
	
	public void excluir(Long id) {
		Cliente cliente = obterComVeiculosPorId(id);
		if (!cliente.getVeiculos().isEmpty()) {
	        throw new IllegalArgumentException("Cliente possuí veículos atrelados, não foi possível excluir.");
		}

		dao.delete(id);
	}

	private void verificarSeVeiculosDoClienteEstaoNoPatio(Cliente c) {
		VagaService vs = new VagaService();
		for (Veiculo v : c.getVeiculos()) {
			if (vs.veiculoEstaNoPatio(v.getPlaca())) {
				throw new IllegalStateException("Um dos veículos do cliente está no pátio. Operação cancelada");
			}
		}
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
		if (dados.nome() == null || dados.nome().isBlank()  ) {
	        throw new IllegalArgumentException("O nome do cliente é obrigatório.");
		}

		if (dados.cpf() == null || dados.cpf().isBlank()) {
	        throw new IllegalArgumentException("O cpf do cliente é obrigatório.");
		}

		if (dados.tipo() == null) {
	        throw new IllegalArgumentException("O tipo do cliente é obrigatório.");
		}

		if (dados.status() == null) {
	        throw new IllegalArgumentException("O status do cliente é obrigatório.");
		}
		
		if (dados.tipo() == TipoCliente.MENSALISTA) {
			if (dados.telefone() == null || dados.telefone().isBlank()) {
		        throw new IllegalArgumentException("Telefone é obrigatório para mensalistas e dizimistas.");
			}
			
			if (dados.dataVencimentoMensalidade() == null) {
		        throw new IllegalArgumentException("Data de vencimento de mensalidade é obrigatória para mensalistas.");
			}
			
			if (dados.valorMensalidade() == null) {
		        throw new IllegalArgumentException("Valor de mensalidade é obrigatório para mensalistas.");
			}
		}
		
		if (dados.tipo() == TipoCliente.DIZIMISTA) {
			if (dados.telefone() == null || dados.telefone().isBlank()) {
		        throw new IllegalArgumentException("Telefone é obrigatório para mensalistas e dizimistas.");
			}
		}

		if (dados.tipo() == TipoCliente.CONVENIADO && dados.idConvenio() == null) {
	        throw new IllegalArgumentException("Cliente convêniado deve ser conectado a um convenio, obrigatóriamente.");
		}
	}
}
