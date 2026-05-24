package com.paroquiaTeam.sgeParoquia.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.TipoPagamento;
import com.paroquiaTeam.sgeParoquia.utils.SessaoSistema;

class MovimentoCaixaDAOTest extends BaseDAOTest {

    private static MovimentoCaixaDAO dao = new MovimentoCaixaDAO();

    @AfterEach
    public void removeMovimentos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM MovimentoCaixa").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    private Caixa getCaixaTest() {
        return SessaoSistema.getInstancia().getCaixa();
    }

    // --- TESTES DE CRUD BÁSICO ---

    @Test
    public void exists_retornaTrueComIdExistente() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 50.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest());
        dao.save(mov);
        assertTrue(dao.exists(mov.getId()));
    }

    @Test
    public void getById_retornaAlgoComIdValido() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 100.0, TipoPagamento.PIX, TipoMovimento.ENTRADA, getCaixaTest());
        dao.save(mov);
        Optional<MovimentoCaixa> talvezMov = dao.getById(mov.getId());
        assertTrue(talvezMov.isPresent());
        assertEquals(100.0, talvezMov.get().getValor());
    }

    @Test
    public void getAll_retornaListaComDadosPersistidos() {
        dao.save(new MovimentoCaixa(null, 50.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest()));
        dao.save(new MovimentoCaixa(null, 25.0, TipoPagamento.DEBITO, TipoMovimento.DESPESA, getCaixaTest()));
        
        List<MovimentoCaixa> movimentos = dao.getAll();
        assertEquals(2, movimentos.size());
    }

    @Test
    public void save_persisteMovimentoValido() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 150.0, TipoPagamento.CREDITO, TipoMovimento.ENTRADA, getCaixaTest());
        assertDoesNotThrow(() -> dao.save(mov));
    }

    @Test
    public void update_atualizaMovimentoValido() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 50.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest());
        dao.save(mov);
        mov.setValor(75.0);
        dao.update(mov);
        assertEquals(75.0, dao.getById(mov.getId()).get().getValor());
    }

    @Test
    public void delete_excluiMovimentoValido() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 50.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest());
        dao.save(mov);
        dao.delete(mov.getId());
        assertFalse(dao.exists(mov.getId()));
    }

    // --- TESTES DE SOMA (SUM) ---

    @Test
    public void getTotalPorCaixaEPagamento_retornaSomaCorreta() {
        dao.save(new MovimentoCaixa(null, 50.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest()));
        dao.save(new MovimentoCaixa(null, 30.0, TipoPagamento.DINHEIROFISICO, TipoMovimento.ENTRADA, getCaixaTest()));
        dao.save(new MovimentoCaixa(null, 100.0, TipoPagamento.PIX, TipoMovimento.ENTRADA, getCaixaTest()));
        
        Double totalDinheiro = dao.getTotalPorCaixaEPagamento(getCaixaTest().getId(), TipoPagamento.DINHEIROFISICO);
        assertEquals(80.0, totalDinheiro);
    }

    @Test
    public void getTotalPorCaixaEPagamento_retornaZeroQuandoNaoHaMovimentos() {
        Double total = dao.getTotalPorCaixaEPagamento(getCaixaTest().getId(), TipoPagamento.CREDITO);
        assertEquals(0.0, total);
    }

    // --- TESTES DE VALIDAÇÃO ---
    @Test
    public void exists_retornaFalseComIdInexistente() {
        assertFalse(dao.exists(0L));
    }
    
    @Test
    public void getById_retornaNadaComIdInvalido() {
        Optional<MovimentoCaixa> talvezMov = dao.getById(0L);
        assertTrue(talvezMov.isEmpty());    
    }
    
    @Test
    public void getAll_retornaListaVaziaSemDadosPersistidos() {
        List<MovimentoCaixa> movimentos = dao.getAll();
        assertTrue(movimentos.isEmpty());
    }
    
    @Test
    public void save_caixaNuloThrowsPropertyValueException() {
        MovimentoCaixa mov = new MovimentoCaixa(null, 50.0, TipoPagamento.PIX, TipoMovimento.ENTRADA, null);
        assertThrows(PropertyValueException.class, () -> dao.save(mov));
    }
    
    @Test
    public void delete_throwsIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(0L));
    }
}