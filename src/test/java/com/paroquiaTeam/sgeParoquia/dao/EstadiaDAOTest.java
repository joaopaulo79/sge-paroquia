package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;

class EstadiaDAOTest extends BaseDAOTest {
	
	private static EstadiaDAO dao = new EstadiaDAO();
	
	@AfterEach
	public void removeEstadias() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();
	        try {
	            session.createMutationQuery("DELETE FROM Estadia").executeUpdate();
	            tx.commit();
	        } catch (Exception e) {
	            tx.rollback();
	            throw e;
	        }
	    }
	}

	@Test
	public void exists_retornaTrueComIdExistente() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		assertTrue(dao.exists(estadia.getId()));
	}

	@Test
	public void exists_retornaFalseComIdNaoExistente() {
		assertFalse(dao.exists(0L));
	}

	@Test
	public void getById_retornaNadaComIdInvalido() {
		Optional<Estadia> talvezEstadia = dao.getById(1L);
		assertTrue(talvezEstadia.isEmpty());
	}

	@Test
	public void getById_retornaAlgoComIdValido() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		Optional<Estadia> talvezEstadia = dao.getById(estadia.getId());
		assertTrue(talvezEstadia.isPresent());
	}

	@Test
	public void getLastByPlaca_retornaNadaComPlacaInvalida() {
		Optional<Estadia> talvezEstadia = dao.getLastByPlaca("XYZ-9999");
		assertTrue(talvezEstadia.isEmpty());
	}

	@Test
	public void getLastByPlaca_retornaUltimaEstadiaComPlacaValida() {
		Estadia estadia1 = new Estadia(null, LocalDateTime.now().minusHours(2), null, 0.0, "ABC-1234");
		dao.save(estadia1);
		
		Estadia estadia2 = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia2); // Esta terá o ID maior
		
		Optional<Estadia> talvezEstadia = dao.getLastByPlaca("ABC-1234");
		assertTrue(talvezEstadia.isPresent());
		assertEquals(estadia2.getId(), talvezEstadia.get().getId());
	}

	@Test
	public void getAll_retornaMaisDeUmValorDoBanco() {
		Estadia estadia1 = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia1);
		
		Estadia estadia2 = new Estadia(null, LocalDateTime.now(), null, 0.0, "XYZ-9999");
		dao.save(estadia2);
		
		List<Estadia> estadias = dao.getAll();
		assertTrue(estadias.size() > 1);
	}

	@Test
	public void getAll_retornaListaVaziaQuandoBancoVazio() {
		List<Estadia> estadias = dao.getAll();
		assertTrue(estadias.isEmpty());
	}

	@Test
	public void save_persisteEstadiaValida() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		Optional<Estadia> talvezEstadia = dao.getById(estadia.getId());
		assertTrue(talvezEstadia.isPresent());
	}
	
	@Test
	public void save_placaNulaThrowsPropertyValueException() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, null);
		assertThrows(PropertyValueException.class, () -> dao.save(estadia));
	}
	
	@Test
	public void save_dataHoraEntradaNulaThrowsPropertyValueException() {
		Estadia estadia = new Estadia(null, null, null, 0.0, "ABC-1234");
		assertThrows(PropertyValueException.class, () -> dao.save(estadia));
	}

	@Test
	public void update_atualizaEstadiaValida() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		estadia.setPlacaVeiculo("DEF-5678");
		assertDoesNotThrow(() -> dao.update(estadia));
		
		Optional<Estadia> estadiaAtualizada = dao.getById(estadia.getId());
		assertTrue(estadiaAtualizada.isPresent());
		assertEquals("DEF-5678", estadiaAtualizada.get().getPlacaVeiculo());
	}
	
	@Test
	public void update_placaNulaThrowsPropertyValueException() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		estadia.setPlacaVeiculo(null);
		assertThrows(PropertyValueException.class, () -> dao.update(estadia));
	}
	
	@Test
	public void update_dataHoraEntradaNulaThrowsPropertyValueException() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		estadia.setDataHoraEntrada(null);
		assertThrows(PropertyValueException.class, () -> dao.update(estadia));
	}

	@Test
	public void delete_excluiEstadiaValida() {
		Estadia estadia = new Estadia(null, LocalDateTime.now(), null, 0.0, "ABC-1234");
		dao.save(estadia);
		
		assertDoesNotThrow(() -> dao.delete(estadia.getId()));
		assertFalse(dao.exists(estadia.getId()));
	}

	@Test
	public void delete_idInvalidoThrows() {
		assertThrows(IllegalArgumentException.class, () -> dao.delete(1L));
	}
}