package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
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
	
	public Vaga getFirstLivre(TipoVaga tipo, TipoReservaVaga reserva) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2";
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
	
	public void delete(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Vaga vaga = sessao.get(Vaga.class, id);
				if (vaga == null) {					
					throw new IllegalArgumentException("Vaga com id especificado não encontrada");
				}
				if (vaga.isOcupada()) {
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
	
	public long countComStatus(TipoVaga tipoVaga, TipoReservaVaga tipoReserva, boolean ocupacao) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Vaga v WHERE v.tipo = ?1 AND v.reserva = ?2 AND v.ocupada = ?3";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, tipoVaga)
					.setParameter(2, tipoReserva)
					.setParameter(3, ocupacao)
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
	        criarVagas(ajuste.tipo, ajuste.reserva, diferenca);
	    } else if (diferenca < 0) {
	        excluirVagasLivres(ajuste.tipo, ajuste.reserva, Math.abs(diferenca));
	    }
	}
	
	private void validarAjuste(AjusteVaga ajuste) {
		long atual = count(ajuste.tipo, ajuste.reserva);
	    long diferenca = atual - ajuste.novaQuantidade;

	    if (diferenca > 0) {
	        long livres = countComStatus(ajuste.tipo, ajuste.reserva, false);
	        if (livres < diferenca) {
	            throw new IllegalStateException(
	                "Vagas de " + ajuste.tipo + " insuficientes para redução. " +
	                "Livres: " + livres + ", necessário liberar: " + diferenca
	            );
	        }
	    }
	}

	private void criarVagas(TipoVaga tipo, TipoReservaVaga reserva, long quantidade) {
	    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction t = sessao.beginTransaction();
	        try {
	            for (int i = 0; i < quantidade; i++) {
	                Vaga vaga = new Vaga();
	                vaga.setTipo(tipo);
	                vaga.setReserva(reserva);
	                vaga.setOcupada(false);
	                sessao.persist(vaga);
	            }
	            t.commit();
	        } catch (Exception e) {
	            t.rollback();
	            throw e;
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
