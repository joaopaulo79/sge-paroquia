package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;

class UsuarioDAOTest extends BaseDAOTest{
	private static UsuarioDAO dao = new UsuarioDAO();
	
	@AfterEach
	public void removeUsers() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();
	        try {
	            session.createMutationQuery("DELETE FROM Usuario").executeUpdate();
	            tx.commit();
	        } catch (Exception e) {
	            tx.rollback();
	            throw e;
	        }
	    }
	}
	
	@Test
	public void exists_retornaTrueComIdExistente() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		assertTrue(dao.exists((long) user.getId()));
	}
	
	@Test
	public void exists_retornaFalseComIdNaoExistente() {
		assertFalse(dao.exists((long) 0));
	}
	
	@Test
	public void getById_retornaAlgoComIdValido() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		Optional<Usuario> talvezUsuario = dao.getById(user.getId());
		assertTrue(talvezUsuario.isPresent());
	}
	
	@Test
	public void getAll_retornaMaisDeUmValorDoBanco() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		senha = SenhaUtil.hash("123");
		Usuario user2 = new Usuario("Teste", "valido2", senha, true, TipoUsuario.OPERADOR);
		dao.save(user2);
		
		List<Usuario> usuarios = dao.getAll();
		assertTrue(usuarios.size() > 1);
	}
	
	@Test
	public void getAll_retornaListaVaziaQuandoBancoVazio() {
		List<Usuario> usuarios = dao.getAll();
		assertTrue(usuarios.isEmpty());
	}
	
	@Test
	public void save_persisteUserValido() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		Optional<Usuario> talvezUsuario = dao.getById(user.getId());
		assertTrue(talvezUsuario.isPresent());
	}
	
	@Test
	public void save_loginDuplicadoThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		senha = SenhaUtil.hash("123");
		Usuario user2 = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		assertThrows(ConstraintViolationException.class, () -> dao.save(user2));
	}
	
	@Test
	public void save_nomeNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario(null, "valido", senha, true, TipoUsuario.OPERADOR);

		assertThrows(PropertyValueException.class, () -> dao.save(user));
	}
	
	@Test
	public void save_loginNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", null, senha, true, TipoUsuario.OPERADOR);

		assertThrows(PropertyValueException.class, () -> dao.save(user));
	}
	
	@Test
	public void save_senhaNulaThrowsConstraintViolationException() {
		Usuario user = new Usuario("Teste", "valido", null, true, TipoUsuario.OPERADOR);

		assertThrows(PropertyValueException.class, () -> dao.save(user));
	}
	
	@Test
	public void save_tipoNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, null);

		assertThrows(PropertyValueException.class, () -> dao.save(user));
	}
	
	@Test
	public void update_atualizaUserValido() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		user.setNome("Teste 2");
		assertDoesNotThrow(() -> dao.update(user));
	}
	
	@Test
	public void update_loginDuplicadoThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		senha = SenhaUtil.hash("123");
		Usuario user2 = new Usuario("Teste", "valido2", senha, true, TipoUsuario.OPERADOR);
		dao.save(user2);
		
		user2.setLogin("valido");
		assertThrows(ConstraintViolationException.class, () -> dao.update(user2));
	}
	
	@Test
	public void update_nomeNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		user.setNome(null);
		assertThrows(PropertyValueException.class, () -> dao.update(user));
	}
	
	@Test
	public void update_loginNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		user.setLogin(null);
		assertThrows(PropertyValueException.class, () -> dao.update(user));
	}
	
	@Test
	public void update_senhaNulaThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		user.setSenha(null);
		assertThrows(PropertyValueException.class, () -> dao.update(user));
	}
	
	@Test
	public void update_tipoNuloThrowsConstraintViolationException() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		user.setTipo(null);
		assertThrows(PropertyValueException.class, () -> dao.update(user));
	}
	
	@Test
	public void delete_excluiUsuarioValido() {
		String senha = SenhaUtil.hash("123");
		Usuario user = new Usuario("Teste", "valido", senha, true, TipoUsuario.OPERADOR);
		dao.save(user);
		
		assertDoesNotThrow(() -> dao.delete(user.getId()));
	}
	
	@Test
	public void delete_idInvalidoThrows() {
		assertThrows(IllegalArgumentException.class, () -> dao.delete((long) 1));
	}
}
