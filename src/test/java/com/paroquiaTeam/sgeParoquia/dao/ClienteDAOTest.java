package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.Cliente;
import com.paroquiaTeam.sgeParoquia.model.entity.Convenio;
import com.paroquiaTeam.sgeParoquia.model.enums.StatusConvenio;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoCliente;

class ClienteDAOTest extends BaseDAOTest {
	ClienteDAO dao = new ClienteDAO();
	
	@AfterEach
	void removeDados() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM Cliente").executeUpdate();
                session.createMutationQuery("DELETE FROM Convenio").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
	}
	
	@Test
	void getAllByIdConvenio_retornaValoresComIdValido() {
		LocalDate data = LocalDate.now().plusMonths(1);
		Convenio conv = new Convenio("abc123", "Unimed", 1.2, 10, 0, StatusConvenio.ATIVO, 0.5, data);
		new ConvenioDAO().save(conv);
		
		Cliente c = new Cliente("Nome", "123", "123", TipoCliente.CONVENIADO, true, conv);
		dao.save(c);
		long idConv = conv.getId();
		List<Cliente> clientes = dao.getAllByIdConvenio(idConv);
		
		assertFalse(clientes.isEmpty());
		boolean valido = true;
		for (Cliente c2 : clientes) {
			valido = c2.getConvenio().getId() == idConv;
		}
		assertTrue(valido);
	}
}
