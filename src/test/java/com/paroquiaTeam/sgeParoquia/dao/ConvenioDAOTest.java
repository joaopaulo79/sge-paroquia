package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Convenio;
import com.paroquiaTeam.sgeParoquia.model.StatusConvenio;

class ConvenioDAOTest extends BaseDAOTest{
	private static ConvenioDAO dao = new ConvenioDAO();
	
	@AfterEach
	public void removeConvenios() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();
	        try {
	            session.createMutationQuery("DELETE FROM Convenio c")
	            .executeUpdate();
	            tx.commit();
	        } catch (Exception e) {
	            tx.rollback();
	            throw e;
	        }
	    }
	}
	
	@Test
	public void exists_retornaTrueComIdExistente() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		assertTrue(dao.existsById((long) c.getId()));
	}
	
	@Test
	public void exists_retornaFalseComIdNaoExistente() {
		assertFalse(dao.existsById((long) 0));
	}
	
	@Test
	public void getById_retornaNadaComIdInvalido() {
		Optional<Convenio> talvezConvenio = dao.getById((long) 1);
		assertTrue(talvezConvenio.isEmpty());
	}
	
	@Test
	public void getById_retornaAlgoComIdValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Optional<Convenio> talvezConvenio = dao.getById(c.getId());
		assertTrue(talvezConvenio.isPresent());
	}
	
	@Test
	public void getByNome_retornaNadaComNomeInvalido() {
		Optional<Convenio> talvezConvenio = dao.getByNome("nome");
		assertTrue(talvezConvenio.isEmpty());
	}
	
	@Test
	public void getByNome_retornaAlgoComNomeValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Optional<Convenio> talvezConvenio = dao.getByNome("Unimed");
		assertTrue(talvezConvenio.isPresent());
	}
	
	@Test
	public void getAll_retornaMaisDeUmValorDoBanco() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Convenio c2 = new Convenio("abc1234", "Unimed2", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c2);
		
		List<Convenio> usuarios = dao.getAll();
		assertTrue(usuarios.size() > 1);
	}
	
	@Test
	public void save_persisteConvenioValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Optional<Convenio> talvezConvenio = dao.getById(c.getId());
		assertTrue(talvezConvenio.isPresent());
	}
	
	@Test
	public void save_cnpjDuplicadoThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Convenio c2 = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		assertThrows(ConstraintViolationException.class, () -> dao.save(c2));
	}
	
	@Test
	public void save_cnpjNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio(null, "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);

		assertThrows(PropertyValueException.class, () -> dao.save(c));
	}
	
	@Test
	public void save_nomeNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", null, 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);

		assertThrows(PropertyValueException.class, () -> dao.save(c));
	}
	
	@Test
	public void save_statusNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, null, 0.5, data);
		
		assertThrows(PropertyValueException.class, () -> dao.save(c));
	}
	
	@Test
	public void save_dataVencimentoNulaThrowsConstraintViolationException() {
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, null);

		assertThrows(PropertyValueException.class, () -> dao.save(c));
	}
	
	@Test
	public void update_atualizaConvenioValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		c.setNome("Teste 2");
		assertDoesNotThrow(() -> dao.update(c));
	}
	
	@Test
	public void update_cnpjDuplicadoThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		Convenio c2 = new Convenio("abc1234", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c2);
		
		c2.setCnpj("abc123");
		assertThrows(ConstraintViolationException.class, () -> dao.update(c2));
	}
	
	@Test
	public void update_cnpjNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		c.setCnpj(null);
		assertThrows(PropertyValueException.class, () -> dao.update(c));
	}
	
	@Test
	public void update_nomeNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		c.setNome(null);
		assertThrows(PropertyValueException.class, () -> dao.update(c));
	}

	@Test
	public void update_statusNuloThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		c.setStatus(null);
		assertThrows(PropertyValueException.class, () -> dao.update(c));
	}
	
	@Test
	public void update_dataVencimentoNulaThrowsConstraintViolationException() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		c.setDataVencimento(null);
		assertThrows(PropertyValueException.class, () -> dao.update(c));
	}
	
	
	@Test
	public void delete_excluiUsuarioValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio c = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		dao.save(c);
		
		assertDoesNotThrow(() -> dao.delete(c.getId()));
	}
	
	@Test
	public void delete_idInvalidoThrows() {
		assertThrows(IllegalArgumentException.class, () -> dao.delete((long) 1));
	}
}
