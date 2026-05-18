package com.paroquiaTeam.sgeParoquia;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.dao.VagaDAO;
import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.Cliente;
import com.paroquiaTeam.sgeParoquia.model.Convenio;
import com.paroquiaTeam.sgeParoquia.model.Estadia;
import com.paroquiaTeam.sgeParoquia.model.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.TipoCliente;
import com.paroquiaTeam.sgeParoquia.model.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.TipoPagamento;
import com.paroquiaTeam.sgeParoquia.model.TipoReservaVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.TipoVaga;
import com.paroquiaTeam.sgeParoquia.model.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.model.Vaga;
import com.paroquiaTeam.sgeParoquia.model.Veiculo;

public class TestesDeEntidades {
	public static void testaEntidades() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				Vaga vaga = new Vaga(TipoVaga.CARRO, TipoReservaVaga.COMUM);
				VagaDAO vagaDao = new VagaDAO();
				vagaDao.save(vaga);				
				
				System.out.println("\ngetById");
				Optional<Vaga> talvezVaga = vagaDao.getById(vaga.getId());
				talvezVaga.ifPresent((vagaSim) -> System.out.println(vagaSim));				
				
				System.out.println("\ngetAll");
				List<Vaga> vagas = vagaDao.getAll();
				for (Vaga vaga2 : vagas) {
					System.out.println(vaga2);
				}
				
				System.out.println("\nVai dar merda......");
				Vaga vagaErr = new Vaga(TipoVaga.CARRO, null);
				vagaDao.save(vagaErr);	
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				System.out.println("\nTipo: " + e.getClass());
				t.rollback();
			}
		}
	}
}
