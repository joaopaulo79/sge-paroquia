package com.paroquiaTeam.sgeParoquia.dao;

import com.paroquiaTeam.sgeParoquia.model.PrecificacaoFracionada;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrecificacaoFracionadaDAOTest extends BaseDAOTest {

    private static PrecificacaoFracionadaDAO dao = new PrecificacaoFracionadaDAO();

    @AfterEach
    public void removePrecificacao() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM PrecificacaoFracionada").executeUpdate();
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
        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
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
        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);
        assertTrue(dao.exists());
    }

    @Test
    public void save_persisteComToleranciaZero() {
        PrecificacaoFracionada prec = new PrecificacaoFracionada(0, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        assertDoesNotThrow(() -> dao.save(prec));
    }

//    @Test
//    public void save_valorMeiaHoraNulaThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, -1.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorHoraNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, -1.0, 50.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorDiariaNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, -1.0, 3.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorMeiaHoraMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, -1.0, 6.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorHoraMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, -1.0, 30.0);
//            dao.save(prec);
//        });
//    }
//
//    @Test
//    public void save_valorDiariaMotoNegativoThrows() {
//        assertThrows(Exception.class, () -> {
//            PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, -1.0);
//            dao.save(prec);
//        });
//    }
//
    // --- update() ---

	@Test
	public void update_atualizaPrecificacaoValida() {
	    PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
	    dao.save(prec);
	
	    prec.setValorHora(15.0);
	    assertDoesNotThrow(() -> dao.update(prec));
	}
//
//    @Test
//    public void update_valorMeiaHoraNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorMeiaHora(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorHoraNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorHora(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorDiariaNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorDiaria(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorMeiaHoraMotoNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorMeiaHoraMoto(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorHoraMotoNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorHoraMoto(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }
//
//    @Test
//    public void update_valorDiariaMotoNegativoThrows() {
//        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
//        dao.save(prec);
//
//        prec.setValorDiariaMoto(-1.0);
//        assertThrows(Exception.class, () -> dao.update(prec));
//    }

    // --- load() ---

    @Test
    public void load_retornaPrecificacaoExistente() {
        PrecificacaoFracionada prec = new PrecificacaoFracionada(10, 5.0, 10.0, 50.0, 3.0, 6.0, 30.0);
        dao.save(prec);

        Optional<PrecificacaoFracionada> talvezPrec = dao.get();
        assertTrue(talvezPrec.isPresent());
    }

    @Test
    public void load_retornaVazioQuandoNaoExiste() {
        Optional<PrecificacaoFracionada> talvezPrec = dao.get();
        assertFalse(talvezPrec.isPresent());
    }
}
