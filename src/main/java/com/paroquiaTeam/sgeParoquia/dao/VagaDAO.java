package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.StatusVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;
import com.paroquiaTeam.sgeParoquia.model.Vaga;

public class VagaDAO {
    public record AjusteVaga(TipoVaga tipo, TipoReservaVaga reserva, long novaQuantidade) {}
	
	public boolean exists(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Vaga> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.id = ?1";
			return sessao.createSelectionQuery(query, Vaga.class)
						.setParameter(1, id)
						.uniqueResultOptional();
					
		}
	}
	
	public List<Vaga> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v";
			return sessao.createSelectionQuery(query, Vaga.class)
						.getResultList();
					
		}
	}
	
	public List<Long> getIdsByStatus(TipoVaga tipo, TipoReservaVaga reserva, StatusVaga status, long max) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT v.id FROM Vaga v WHERE v.status = ?1 AND v.tipo = ?2 AND v.reserva = ?3";
			return sessao.createSelectionQuery(query, Long.class)
						.setParameter(1, status)
						.setParameter(2, tipo)
						.setParameter(3, reserva)
						.setMaxResults((int) max)
						.getResultList();
		}
	}
	
	public List<Vaga> getByStatus(TipoVaga tipo, TipoReservaVaga reserva, StatusVaga status, long max) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.status = ?1 AND v.tipo = ?2 AND v.reserva = ?3";
			return sessao.createSelectionQuery(query, Vaga.class)
						.setParameter(1, status)
						.setParameter(2, tipo)
						.setParameter(3, reserva)
						.setMaxResults((int) max)
						.getResultList();
		}
	}
	
	public Vaga getFirstLivre(TipoVaga tipo, TipoReservaVaga reserva) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2 AND v.status = LIVRE";
			return sessao.createSelectionQuery(query, Vaga.class)
						.setParameter(1, tipo)
						.setParameter(2, reserva)
						.getSingleResultOrNull();			
		}
	}
	
	public void save(Vaga vaga) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(vaga);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public long batchSave(TipoVaga tipo, TipoReservaVaga reserva, long quantidade) {
		if (quantidade <= 0) {
			return 0;
		}
		
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction t = sessao.beginTransaction();
	        try {
	            for (long i = 0; i < quantidade; i++) {
	                Vaga vaga = new Vaga();
	                vaga.setTipo(tipo);
	                vaga.setReserva(reserva);
	                vaga.setStatus(StatusVaga.LIVRE);
	                sessao.persist(vaga);
	            }
	            t.commit();
	            
	            return quantidade;
	        } catch (Exception e) {
	            t.rollback();
	            throw e;
	        }
	    }
	}
	
	public void update(Vaga vaga) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(vaga);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public int batchLiberar(StatusVaga statusAntigo) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				String hql = "UPDATE Vaga v SET v.status = :statusLivre WHERE v.status = :statusAntigo";
	            
	            int linhas = sessao.createMutationQuery(hql)
	                    .setParameter("statusLivre", StatusVaga.LIVRE)
	                    .setParameter("statusAntigo", statusAntigo)
	                    .executeUpdate();
				t.commit();
				
				return linhas;
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public int batchUpdateStatus(List<Long> ids, StatusVaga novoStatus) {
		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("Nenhum id fornecido");
	    }

	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction t = sessao.beginTransaction();
	        try {
	            String hql = "UPDATE Vaga v SET v.status = :novoStatus WHERE v.id IN :ids";
	            
	            int linhas = sessao.createMutationQuery(hql)
	                    .setParameter("novoStatus", novoStatus)
	                    .setParameter("ids", ids)
	                    .executeUpdate();
	            t.commit();
	            return linhas;
	        } catch (Exception e) {
	            t.rollback();
	            throw e;
	        }
	    }
	}
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Vaga vaga = sessao.get(Vaga.class, id);
				if (vaga == null) {					
					throw new IllegalArgumentException("Vaga com id especificado não encontrada");
				}
				if (vaga.getStatus().equals(StatusVaga.OCUPADA)) {
	                throw new IllegalStateException("Não é possível deletar uma vaga ocupada");
	            }
				sessao.remove(vaga);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;			
			}
		}
	}
	
	public long countAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v";
			Long quantidade = sessao.createQuery(query, Long.class)
					.uniqueResult();			
			return quantidade;
		}
	}
	
	public long count(TipoVaga tipoVaga, TipoReservaVaga tipoReserva) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, tipoVaga)
					.setParameter(2, tipoReserva)
					.uniqueResult();			
			return quantidade;
		}
	}
	
	public long countComStatus(TipoVaga tipoVaga, TipoReservaVaga tipoReserva, StatusVaga status) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2 AND v.status = ?3";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, tipoVaga)
					.setParameter(2, tipoReserva)
					.setParameter(3, status)
					.uniqueResult();			
			return quantidade;
		}
	}
	
	public void ajustarVagas(List<AjusteVaga> ajustes) {
		ajustes.forEach(a -> validarAjuste(a));
		
		ajustes.forEach(a -> realizarAjuste(a));
	}
	
	private void realizarAjuste(AjusteVaga ajuste) {
	    long atual = count(ajuste.tipo, ajuste.reserva);
	    long diferenca = ajuste.novaQuantidade - atual;

	    if (diferenca > 0) {
	        batchSave(ajuste.tipo, ajuste.reserva, diferenca);
	    } else if (diferenca < 0) {
	        excluirVagasLivres(ajuste.tipo, ajuste.reserva, Math.abs(diferenca));
	    }
	}
	
	private void validarAjuste(AjusteVaga ajuste) {
		long atual = count(ajuste.tipo, ajuste.reserva);
	    long diferenca = atual - ajuste.novaQuantidade;

	    if (diferenca > 0) {
	        long livres = countComStatus(ajuste.tipo, ajuste.reserva, StatusVaga.LIVRE);
	        if (livres < diferenca) {
	            throw new IllegalStateException(
	                "Vagas de " + ajuste.tipo + " insuficientes para redução. " +
	                "Livres: " + livres + ", necessário liberar: " + diferenca
	            );
	        }
	    }
	}

	private void excluirVagasLivres(TipoVaga tipo, TipoReservaVaga reserva, long quantidade) {
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction t = sessao.beginTransaction();
	        try {
	            String query = "FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2 AND v.ocupada = false";
	            List<Vaga> livres = sessao.createSelectionQuery(query, Vaga.class)
	                    .setParameter(1, tipo)
	                    .setParameter(2, reserva)
	                    .setMaxResults((int) quantidade)
	                    .getResultList();

	            if (livres.size() < quantidade) {
	                throw new IllegalStateException("Vagas ocupadas seriam removidas. Operação cancelada.");
	            }

	            livres.forEach(sessao::remove);
	            t.commit();
	        } catch (Exception e) {
	            t.rollback();
	            throw e;
	        }
	    }
	}
}
