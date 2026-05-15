package com.paroquiaTeam.sgeParoquia.dao;

import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;
import com.paroquiaTeam.sgeParoquia.model.Vaga;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VagaDAOTest {

    private static VagaDAO dao = new VagaDAO();

    @AfterEach
    public void removeVagas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM Vaga").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    // --- exists() ---

    @Test
    public void exists_retornaTrueComIdExistente() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);
        assertTrue(dao.exists((long) vaga.getId()));
    }

    @Test
    public void exists_retornaFalseComIdNaoExistente() {
        assertFalse(dao.exists((long) 0));
    }

    // --- getById() ---

    @Test
    public void getById_retornaAlgoComIdValido() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        Optional<Vaga> talvezVaga = dao.getById(vaga.getId());
        assertTrue(talvezVaga.isPresent());
    }

    @Test
    public void getById_retornaVazioComIdInvalido() {
        Optional<Vaga> talvezVaga = dao.getById((long) 0);
        assertFalse(talvezVaga.isPresent());
    }

    // --- getAll() ---

    @Test
    public void getAll_retornaMaisDeUmValorDoBanco() {
        dao.save(new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM));
        dao.save(new Vaga(TipoVaga.MOTO, TipoReservaVaga.COMUM));

        List<Vaga> vagas = dao.getAll();
        assertTrue(vagas.size() > 1);
    }

    @Test
    public void getAll_retornaListaVaziaQuandoBancoVazio() {
        List<Vaga> vagas = dao.getAll();
        
        assertTrue(vagas.isEmpty());
    }

    // --- save() ---

    @Test
    public void save_persisteVagaValida() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        Optional<Vaga> talvezVaga = dao.getById(vaga.getId());
        assertTrue(talvezVaga.isPresent());
    }

    @Test
    public void save_persisteVagaComPlacaOpcional() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        vaga.setPlaca("ABC1D23");
        dao.save(vaga);

        Optional<Vaga> talvezVaga = dao.getById(vaga.getId());
        assertTrue(talvezVaga.isPresent());
    }

    @Test
    public void save_persisteVagaComConvenioNulo() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        vaga.setConvenio(null);
        assertDoesNotThrow(() -> dao.save(vaga));
    }

    @Test
    public void save_tipoNuloThrowsPropertyValueException() {
        Vaga vaga = new Vaga(null, TipoReservaVaga.COMUM);
        assertThrows(PropertyValueException.class, () -> dao.save(vaga));
    }

    @Test
    public void save_reservaNulaThrowsPropertyValueException() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, null);
        assertThrows(PropertyValueException.class, () -> dao.save(vaga));
    }

    // --- update() ---

    @Test
    public void update_atualizaVagaValida() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        vaga.setOcupada(true);
        vaga.setPlaca("ABC1234");
        assertDoesNotThrow(() -> dao.update(vaga));
    }

    @Test
    public void update_tipoNuloThrowsPropertyValueException() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        vaga.setTipo(null);
        assertThrows(PropertyValueException.class, () -> dao.update(vaga));
    }

    @Test
    public void update_reservaNulaThrowsPropertyValueException() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        vaga.setReserva(null);
        assertThrows(PropertyValueException.class, () -> dao.update(vaga));
    }

    // --- delete() ---

    @Test
    public void delete_excluiVagaValida() {
        Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
        dao.save(vaga);

        assertDoesNotThrow(() -> dao.delete(vaga.getId()));
    }

    @Test
    public void delete_idInvalidoThrows() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete((long) 0));
    }
}
