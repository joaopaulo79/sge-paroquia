package com.paroquiaTeam.sgeParoquia.service;

import java.time.LocalDate;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.ConvenioDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosConvenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusConvenio;

public class ConvenioService {
	
	private ConvenioDAO dao = new ConvenioDAO();
	
	public Optional<Convenio> buscarPorCnpj(String cnpj) {
		return dao.getByCnpj(cnpj);
	}
	
	public Optional<Convenio> buscarPorNome(String nome) {
		return dao.getByNome(nome);
	}
	
	public void salvar (DadosConvenio dados) {
		validarNovoConvenio(dados);
		
		Convenio convenio = dados.paraConvenio();
		
		dao.save(convenio);
	}
	
	public void atualizar (DadosConvenio dadosNovos, Convenio convenio) {
		validarAtualizacaoConvenio(dadosNovos, convenio);
		
		convenio.setCnpj(dadosNovos.cnpj());
		convenio.setNome(dadosNovos.nome());
		convenio.setMensalidade(dadosNovos.mensalidade());
		convenio.setVagasCarro(dadosNovos.vagasCarro());
		convenio.setVagasMoto(dadosNovos.vagasMoto());
		convenio.setCobrancaIndividual(dadosNovos.cobrancaIndividual());
		convenio.setDataVencimento(dadosNovos.dataVencimento());
		
		
		
		dao.update(convenio);
	}
	
	public void ativar(Convenio convenio) {
		if (convenio.getDataVencimento().isBefore(LocalDate.now())) {
			throw new IllegalStateException("Convenio com atraso. Não foi possível ativar");
		}

		convenio.setStatus(StatusConvenio.ATIVO);
		dao.update(convenio);
	}
	
	public void marcarAtraso(Convenio convenio) {
		if (!convenio.getDataVencimento().isBefore(LocalDate.now())) {
			throw new IllegalStateException("Data de vencimento é posterior à data atual. Não foi possível marcar atraso");
		}
		
		convenio.setStatus(StatusConvenio.ATRASO);
		dao.update(convenio);

	}
	
	public void desativar(Convenio convenio) {
		convenio.setStatus(StatusConvenio.DESATIVADO);
		dao.update(convenio);

	}
	
	private void validarNovoConvenio(DadosConvenio dados) {
		validarCamposBasicos(dados);

		if (dao.existsByCnpj(dados.cnpj())) {
			throw new IllegalArgumentException("Convênio com este cnpj já existe");
		}
	}
	
	private void validarAtualizacaoConvenio(DadosConvenio dados, Convenio convenioAtual) {
	    validarCamposBasicos(dados);

	    boolean cnpjFoiAlterado = !dados.cnpj().equalsIgnoreCase(convenioAtual.getCnpj());
	    
	    if (cnpjFoiAlterado && dao.existsByCnpj(dados.cnpj())) {
	        throw new IllegalArgumentException("O novo CNPJ informado já pertence a outro convênio.");
	    }
	}
	
	private void validarCamposBasicos(DadosConvenio dados) {
		// Checando se o parametro em si passado não é nulo
		if (dados == null) {
	        throw new IllegalArgumentException("Os dados do convênio não podem ser nulos.");
	    }

	    if (dados.cnpj() == null || dados.cnpj().isBlank()) {
	        throw new IllegalArgumentException("O CNPJ é obrigatório.");
	    }

	    if (dados.nome() == null || dados.nome().isBlank()) {
	        throw new IllegalArgumentException("O nome do convênio é obrigatório.");
	    }

	    if (dados.status() == null) {
	        throw new IllegalArgumentException("O status é obrigatório.");
	    }

	    if (dados.dataVencimento() == null) {
	        throw new IllegalArgumentException("A data de vencimento é obrigatória.");
	    }
		
		if (dados.mensalidade() < 0 || dados.vagasCarro() < 0 ||
				dados.vagasMoto() < 0 || dados.cobrancaIndividual() < 0) {
			throw new IllegalArgumentException("Dados numéricos não podem ser negativos");
		}
	}
}
