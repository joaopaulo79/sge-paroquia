package com.paroquiaTeam.sgeParoquia.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Veiculo;

public class VeiculoDAO {
	
	public boolean existsById(long id) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Veiculo v WHERE v.id = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, id)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public boolean existsByPlaca(String placa) { 
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT COUNT(v) FROM Veiculo v WHERE placa = ?1";
			Long quantidade = sessao.createQuery(query, Long.class)
					.setParameter(1, placa)
					.uniqueResult();			
			return quantidade != null && quantidade > 0;
		}
	}
	
	public Optional<Veiculo> getById(Long id) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT v FROM Veiculo v WHERE v.id = :id";
			return sessao.createQuery(query, Veiculo.class)
						.setParameter("id", id)
						.uniqueResultOptional();
					
		}
	}
	
	public Optional<Veiculo> getByCpf(String placa) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT DISTINCT v FROM Veiculo v WHERE v.placa = :placa";
			return sessao.createQuery(query, Veiculo.class)
						.setParameter("placa", placa)
						.uniqueResultOptional();
					
		}
	}
	
	
	public List<Veiculo> getAll() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT v FROM Veiculo v";
			return sessao.createQuery(query, Veiculo.class)
						.getResultList();
					
		}
	}
	
	public List<Veiculo> getAllByIdCliente(Long idCliente) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			String query = "SELECT c FROM Veiculo c WHERE c.cliente.id = ?1";
			return sessao.createQuery(query, Veiculo.class)
						.setParameter(1, idCliente)
						.getResultList();
					
		}
	}
	
	public void save(Veiculo veiculo) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.persist(veiculo);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
	
	public void update(Veiculo veiculo) {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				sessao.merge(veiculo);
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
				Veiculo veiculo = sessao.get(Veiculo.class, id);
				if (veiculo == null) {					
					throw new IllegalArgumentException("Veículo com id especificado não encontrado");
				}
				sessao.remove(veiculo);
				t.commit();
			} catch (Exception e) {
				t.rollback();
				throw e;
			}
		}
	}
}
