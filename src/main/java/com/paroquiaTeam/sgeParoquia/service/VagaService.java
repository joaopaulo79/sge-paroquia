package com.paroquiaTeam.sgeParoquia.service;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.dao.VagaDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Vaga;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVaga;

public class VagaService {
    public record AjusteVaga(TipoVaga tipo, TipoReservaVaga reserva, long quantidade) {}
    
    private VagaDAO dao = new VagaDAO();
    
    public List<Vaga> buscarVagas() {
    	return dao.getAll();
    }
    
    public long adicionarVagas(AjusteVaga ajuste) {
    	return dao.batchSave(ajuste.tipo, ajuste.reserva, ajuste.quantidade);
    }
    
    public long liberarVagas(AjusteVaga ajuste, StatusVaga statusAntigo) {
    	validarLiberacao(ajuste, statusAntigo);
    	List<Long> ids = dao.getIdsByStatus(ajuste.tipo, ajuste.reserva, statusAntigo, ajuste.quantidade);
    	
    	return dao.batchUpdateStatus(ids, StatusVaga.LIVRE);
    }
    
    public long liberarTodasAsVagas(StatusVaga statusAntigo) {
    	return dao.batchLiberar(statusAntigo);
    }
    
    public long reduzirVagas(AjusteVaga ajuste, StatusVaga novoStatus) {
    	validarReducao(ajuste);
    	List<Long> ids = dao.getIdsByStatus(ajuste.tipo, ajuste.reserva, StatusVaga.LIVRE, ajuste.quantidade);
    	
    	return dao.batchUpdateStatus(ids, novoStatus);
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
