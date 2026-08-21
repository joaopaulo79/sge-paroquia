package com.paroquiaTeam.sgeParoquia.service;

import com.paroquiaTeam.sgeParoquia.dao.VeiculoDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosVeiculo;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;

public class VeiculoService {
	VeiculoDAO dao = new VeiculoDAO();
	
	public void salvar(DadosVeiculo dados) {
		validarVeiculoNovo(dados);
		
		ClienteService cService = new ClienteService();
		
		Veiculo veiculo = dados.paraVeiculo(cService.obterPorId(dados.idCliente()));
		dao.save(veiculo);
	}
	
	public void atualizar(DadosVeiculo dadosNovos, Veiculo veiculo) {
		validarAtualizacaoVeiculo(dadosNovos, veiculo);
		
		veiculo.setPlaca(dadosNovos.placa());
		veiculo.setMarca(dadosNovos.marca());
		veiculo.setModelo(dadosNovos.modelo());
		veiculo.setAno(dadosNovos.ano());
		veiculo.setCor(dadosNovos.cor());
		veiculo.setObservacoes(dadosNovos.observacoes());
		veiculo.setTipo(dadosNovos.tipo());
		
		// Mudou cliente
		if (dadosNovos.idCliente() != veiculo.getCliente().getId()) {
			ClienteService cService = new ClienteService();
			veiculo.setCliente(cService.obterPorId(dadosNovos.idCliente()));
		}
		
		dao.update(veiculo);
	}
	
	public void excluir(Veiculo veiculo) {
		VagaService vService = new VagaService();
		if (vService.veiculoEstaNoPatio(veiculo.getPlaca())) {
			throw new IllegalStateException("Não foi possível excluir veículo pois está no pátio");
		}
		
		dao.delete(veiculo.getId());
	}
	
	private void validarVeiculoNovo(DadosVeiculo dados) {
		validarDadosVeiculo(dados);
		
		if (dao.existsByPlaca(dados.placa())) {
	        throw new IllegalArgumentException("Veículo com esta placa já existe.");
		}
	}
	
	private void validarAtualizacaoVeiculo(DadosVeiculo dadosNovos, Veiculo veiculo) {
		validarDadosVeiculo(dadosNovos);
		
		boolean mudouPlaca = !dadosNovos.placa().equalsIgnoreCase(veiculo.getPlaca());
		if (mudouPlaca && dao.existsByPlaca(dadosNovos.placa())) {
	        throw new IllegalArgumentException("Veículo com esta placa já existe.");
		}
	}
	
	private void validarDadosVeiculo(DadosVeiculo dados) {
		if (dados.placa().isBlank() || dados.placa() == null) {
	        throw new IllegalArgumentException("Placa do veículo é obrigatória.");
		}
		if (dados.marca().isBlank() || dados.marca() == null) {
	        throw new IllegalArgumentException("Marca do veículo é obrigatória.");
		}
		if (dados.modelo().isBlank() || dados.modelo() == null) {
	        throw new IllegalArgumentException("Modelo do veículo é obrigatório.");
		}
		if (dados.cor().isBlank() || dados.cor() == null) {
	        throw new IllegalArgumentException("Cor do veículo é obrigatória.");
		}
		if (dados.tipo() == null) {
	        throw new IllegalArgumentException("Tipo do veículo é obrigatório.");
		}
		// nenhum cliente, em teoria, deve ter id 0
		// se tiver, provavelmente ou não foi inserido, ou teve algum outro erro
		if (dados.idCliente() <= 0) {
	        throw new IllegalArgumentException("Id do cliente inválido");
		}
	}
}