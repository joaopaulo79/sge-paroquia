package com.paroquiaTeam.sgeParoquia.dao;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.entity.PrecificacaoPorHora;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrecificacaoPorHoraDAOTest {

    private static PrecificacaoPorHoraDAO dao = new PrecificacaoPorHoraDAO();

    @AfterEach
    public void removePrecificacao() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM PrecificacaoPorHora").executeUpdate();
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
        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);
        assertTrue(dao.exists());
    }

    @Test
    public void exists_retornaFalseQuandoNaoExiste() {
        assertFalse(dao.exists());
    }

    // --- save() ---

    @Test
    public void save_persistePrecificacaoValida() {
        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);
        assertTrue(dao.exists());
    }

    @Test
    public void save_persisteComToleranciaZero() {
        // tolerance of 0 means no free period — should be valid
        PrecificacaoPorHora prec = new PrecificacaoPorHora(0, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        assertDoesNotThrow(() -> dao.save(prec));
    }

//    @Test
//    public void save_valorEntradaNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, -1.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorHoraNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, -1.0, 50.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorDiariaNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, -1.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorEntradaMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, -1.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorHoraMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, -1.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorDiariaMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, -1.0);
//            dao.save(prec);
//        });
//    }

    // --- update() ---

    @Test
    public void update_atualizaPrecificacaoValida() {
        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);

        prec.setValorHora(15.0);
        assertDoesNotThrow(() -> dao.update(prec));
    }

//    @Test
//    public void update_valorHoraNegativoThrows() {
//        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorHora(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorDiariaNegativoThrows() {
//        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorDiaria(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }

    // --- load() ---

    @Test
    public void load_retornaPrecificacaoExistente() {
        PrecificacaoPorHora prec = new PrecificacaoPorHora(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);

        Optional<PrecificacaoPorHora> talvezPrec = dao.get();
        assertTrue(talvezPrec.isPresent());
    }

    @Test
    public void load_retornaVazioQuandoNaoExiste() {
        Optional<PrecificacaoPorHora> talvezPrec = dao.get();
        assertFalse(talvezPrec.isPresent());
    }
}
