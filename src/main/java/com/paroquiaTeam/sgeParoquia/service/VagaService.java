package com.paroquiaTeam.sgeParoquia.service;

import java.util.List;
import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.VagaDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.entity.Vaga;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;

import jakarta.persistence.EntityNotFoundException;

public class VagaService {
    public record AjusteVaga(TipoVaga tipo, TipoReservaVaga reserva, long quantidade) {}
    public record AjusteConvenio(TipoVaga tipo, long idConv, long quantidade) {}
    
    private VagaDAO dao = new VagaDAO();

    public boolean veiculoEstaNoPatio(String placa) {
    	return dao.veiculoEstaEmAlgumaVaga(placa);
    }

    public long contar(TipoVaga tipo, TipoReservaVaga reserva) {
    	return dao.count(tipo, reserva);
    }
    
    public boolean temEspaco(TipoVaga tipo, TipoReservaVaga reserva) {
    	return dao.isThereVagaLivre(tipo, reserva);
    }
    
    public boolean temEspacoConvenio(TipoVaga tipo, Long id) {
    	return dao.isThereVagaConvenioLivre(tipo, id);
    }

    public Optional<Vaga> buscarPorPlaca(String placa) {
    	return dao.getByPlaca(placa);
    }
    
    public Vaga obterPorPlaca(String placa) {
    	return buscarPorPlaca(placa)
    			.orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada para a placa "+placa));
    }
    
    public List<Vaga> buscarVagas() {
    	return dao.getAll();
    }

    public void registrarEntradaComum(TipoVaga tipo, String placa) {
    	Vaga vaga = dao.getFirstLivre(tipo, TipoReservaVaga.COMUM);
    	
    	if (vaga == null) {
    		throw new IllegalStateException("Não há vagas livres!");
    	}
    	
    	vaga.setPlaca(placa);
    	vaga.setStatus(StatusVaga.OCUPADA);
    	
    	dao.update(vaga);
    }
    
    public void registrarEntradaConvenio(TipoVaga tipo, Long idConv, String placa) {
    	Vaga vaga = dao.getFirstLivreByIdConvenio(idConv, tipo);
    	
    	if (vaga == null) {
    		throw new IllegalStateException("Não há vagas livres!");
    	}
    	
    	vaga.setPlaca(placa);
    	vaga.setStatus(StatusVaga.OCUPADA);
    	
    	dao.update(vaga);
    }
    
    public void registrarSaida(String placa) {
    	Vaga vaga = obterPorPlaca(placa);
    	
    	vaga.setPlaca(null);
    	vaga.setStatus(StatusVaga.LIVRE);
    	
    	dao.update(vaga);
    }
    
    public long adicionarVagas(AjusteVaga ajuste) {
    	return dao.batchSave(ajuste.tipo, ajuste.reserva, ajuste.quantidade);
    }
    
    public long adicionarVagasConvenio(AjusteConvenio ajuste) {
    	Convenio c = new ConvenioService().obterPorId(ajuste.idConv);
    	
    	return dao.batchSaveConvenio(ajuste.tipo, c, ajuste.quantidade);
    }

    public long liberarVagas(AjusteVaga ajuste, StatusVaga statusAntigo) {
    	validarLiberacao(ajuste, statusAntigo);
    	List<Long> ids = dao.getIdsByStatus(ajuste.tipo, ajuste.reserva, statusAntigo, ajuste.quantidade);

    	return dao.batchUpdateStatus(ids, StatusVaga.LIVRE);
    }
    
    public long liberarVagasConvenio(AjusteConvenio ajuste) {
    	List<Long> ids = dao.getAllByIdConvenioWithTipo(ajuste.idConv, ajuste.tipo)
    			.stream().map(Vaga::getId).toList();
    	long mudancas = 0;
    	
    	if (ids.size() != 0) {
    		mudancas += dao.batchUpdateStatus(ids, StatusVaga.LIVRE);
    	}
    	
    	// Tinha menos vagas do que a nova quantidade, adicionar novas
    	if (ids.size() < ajuste.quantidade) {
    		AjusteConvenio ajusteNovo = new AjusteConvenio(
    				ajuste.tipo, 
    				ajuste.idConv, 
    				ajuste.quantidade-ids.size());
    		
    		mudancas += adicionarVagasConvenio(ajusteNovo);
    	} // Tinha mais vagas do que a nova quantidade, reduzir
    	else if (ids.size() > ajuste.quantidade) {
    		mudancas += reduzirVagasConvenio(ajuste.idConv, ids.size()-ajuste.quantidade, ajuste.tipo);
    	}
    	
    	return mudancas;
    }

    public long liberarTodasAsVagas(StatusVaga statusAntigo) {
    	return dao.batchLiberar(statusAntigo);
    }

    public long reduzirVagas(AjusteVaga ajuste, StatusVaga novoStatus) {
    	validarReducao(ajuste);
    	List<Long> ids = dao.getIdsByStatus(ajuste.tipo, ajuste.reserva, StatusVaga.LIVRE, ajuste.quantidade);

    	return dao.batchUpdateStatus(ids, novoStatus);
    }
    
    public long reduzirTodasAsVagasConvenio(Long idConvenio) {
    	List<Long> ids = dao.getAllByIdConvenio(idConvenio).stream().map(Vaga::getId).toList();
    	
    	long mudancas = 0;
    	if (ids.size() != 0) {
    		mudancas += dao.batchUpdateStatus(ids, StatusVaga.BLOQUEADA);
    	}
    	return mudancas;
    }
    
    public long reduzirVagasConvenio(Long idConvenio, long quantidade, TipoVaga tipo) {
    	List<Long> ids = dao.getAllByIdConvenioWithTipo(idConvenio, tipo)
    			.stream().map(Vaga::getId).toList();
    	
    	ids = ids.subList(0, Math.toIntExact(quantidade));
    	
    	long mudancas = 0;
    	if (ids.size() != 0) {
    		mudancas += dao.batchUpdateStatus(ids, StatusVaga.BLOQUEADA);
    	}
    	return mudancas;
    }
    
    public void excluirVagasDoConvenio(Long idConvenio) {
    	List<Long> ids = dao.getAllByIdConvenio(idConvenio)
    			.stream().map(Vaga::getId).toList();
    	
    	if (ids.size() != 0) {
    		dao.batchDelete(ids);
    	}
    }

    private void validarLiberacao(AjusteVaga ajuste, StatusVaga statusAntigo) {
    	long liberaveis = dao.countComStatus(ajuste.tipo, ajuste.reserva, statusAntigo);

    	if (liberaveis < ajuste.quantidade) {
            throw new IllegalStateException(
                "Não há vagas com status "+statusAntigo+" suficientes para serem liberadas.\n"+
                "Vagas: "+liberaveis+", valor inserido: "+ajuste.quantidade
            );
        }
    }

	private void validarReducao(AjusteVaga ajuste) {
        long livres = dao.countComStatus(ajuste.tipo, ajuste.reserva, StatusVaga.LIVRE);
        if (livres < ajuste.quantidade) {
            throw new IllegalStateException(
            		"Não há vagas livres suficientes para serem reservadas ou bloqueadas.\n"+
                    "Vagas: "+livres+", valor inserido: "+ajuste.quantidade
            );
        }

	}

}
