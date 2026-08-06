package com.paroquiaTeam.sgeParoquia.dao;

import com.paroquiaTeam.sgeParoquia.model.entity.Estacionamento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPrecificacao;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EstacionamentoDAOTest extends BaseDAOTest {

    private static EstacionamentoDAO dao = new EstacionamentoDAO();

    // Estacionamento uses a fixed ID of 1 — reset it after each test
    @AfterEach
    public void removeEstacionamento() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM Estacionamento").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    // --- exists() ---

    @Test
    public void exists_retornaTrueQuandoExiste() {
        Estacionamento est = new Estacionamento(50, TipoPrecificacao.POR_HORA);
        dao.save(est);
        assertTrue(dao.exists());
    }

    @Test
    public void exists_retornaFalseQuandoNaoExiste() {
        assertFalse(dao.exists());
    }

    // --- save() ---

    @Test
    public void save_persisteEstacionamentoValido() {
        Estacionamento est = new Estacionamento(50, TipoPrecificacao.POR_HORA);
        dao.save(est);
        assertTrue(dao.exists());
    }
    
    @Test
    public void save_precificacaoNulaThrowsPropertyValueException() {
        assertThrows(PropertyValueException.class, () -> {
            Estacionamento est = new Estacionamento(50, null);
            dao.save(est);
        });
    }

    // --- update() ---

    @Test
    public void update_atualizaEstacionamentoValido() {
        Estacionamento est = new Estacionamento(50, TipoPrecificacao.POR_HORA);
        dao.save(est);

        est.setNumeroDeVagas(100);
        assertDoesNotThrow(() -> dao.update(est));
    }

    @Test
    public void update_precificacaoNulaThrowsPropertyValueException() {
        Estacionamento est = new Estacionamento(50, TipoPrecificacao.POR_HORA);
        dao.save(est);

        est.setPrecificacao(null);
        assertThrows(PropertyValueException.class, () -> dao.update(est));
    }

    // --- get() ---

    @Test
    public void get_retornaEstacionamentoExistente() {
        Estacionamento est = new Estacionamento(50, TipoPrecificacao.POR_HORA);
        dao.save(est);

        Optional<Estacionamento> talvezEst = dao.get();
        assertTrue(talvezEst.isPresent());
    }

    @Test
    public void get_retornaVazioQuandoNaoExiste() {
        Optional<Estacionamento> talvezEst = dao.get();
        assertFalse(talvezEst.isPresent());
    }
}
