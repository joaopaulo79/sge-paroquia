package com.paroquiaTeam.sgeParoquia;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
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
				System.out.println("\nCriando usuario");
				Usuario user = new Usuario("Teste", "Teste", "Teste", true, TipoUsuario.ADMINISTRADOR);
				System.out.println("Persistindo usuario");
				UsuarioDAO userDao = new UsuarioDAO();
				userDao.save(user);
				
				System.out.println("\nCriando caixa");
				Caixa caixa = new Caixa();
				caixa.setDataHoraAbertura(LocalDateTime.now());
				caixa.setDataHoraFechamento(LocalDateTime.now().plusDays(1));
				caixa.setValorInicial(100);
				caixa.setOperador(user);
				
				System.out.println("Persistindo caixa");
				sessao.persist(caixa);
		
				
				System.out.println("\nCriando cliente");
				Cliente cliente = new Cliente();
				cliente.setNome("Teste");
				cliente.setCpf("Teste");
				cliente.setTelefone("Teste");
				cliente.setTipo(TipoCliente.AVULSO);
				cliente.setStatus(true);
				
				System.out.println("Persistindo cliente");
				sessao.persist(cliente);
				
				
				System.out.println("\nCriando veiculo");
				Veiculo veiculo = new Veiculo();
				veiculo.setPlaca("Teste");
				veiculo.setMarca("Teste");
				veiculo.setModelo("Teste");
				veiculo.setCor("Teste");
				veiculo.setObservacoes("Teste");
				veiculo.setTipo(TipoVeiculo.CARRO);
				veiculo.setCliente(cliente);
				
				System.out.println("Persistindo veiculo");
				sessao.persist(veiculo);
				
				
				System.out.println("\nCriando estadia");
				Estadia estadia = new Estadia();
				estadia.setDataHoraEntrada(LocalDateTime.now().plusHours(1));
				estadia.setDataHoraSaida(LocalDateTime.now().plusHours(3));
				estadia.setValor(15);
				estadia.setPlacaVeiculo(veiculo.getPlaca());
				
				
				System.out.println("Persistindo estadia");
				sessao.persist(estadia);
				
				System.out.println("\nCriando movimento");
				MovimentoCaixa movimento = new MovimentoCaixa();
				movimento.setTipoMovimento(TipoMovimento.SANGRIA);
				movimento.setFormaPagamento(TipoPagamento.DINHEIROFISICO);
				movimento.setCaixa(caixa);
				movimento.setValor(15);
			
				System.out.println("Persistindo movimento");
				sessao.persist(movimento);
			
			
				System.out.println("\nCriando movimento com estadia");
				MovimentoCaixa movimento2 = new MovimentoCaixa();
				movimento2.setTipoMovimento(TipoMovimento.ENTRADA);
				movimento2.setFormaPagamento(TipoPagamento.CREDITO);
				movimento2.setCaixa(caixa);
				movimento2.setEstadia(estadia);
				movimento2.setValor(15);
			
				System.out.println("Persistindo movimento com estadia");
				sessao.persist(movimento2);
				
				System.out.println("\nCriando convenio");
				Convenio convenio = new Convenio();
				convenio.setNome("Teste");
				convenio.setVagasContratadas(10);
				convenio.setStatus(true);
				convenio.setMensalidade(100);
				convenio.setCobrancaIndividual(0);
				
				System.out.println("Persistindo convenio");
				sessao.persist(convenio);
				
				System.out.println("\nCriando vaga comum");
				Vaga vaga = new Vaga();
				vaga.setTipo(TipoVaga.CARRO);
				vaga.setReserva(TipoReservaVaga.COMUM);
				vaga.setOcupada(false);
				
				System.out.println("Persistindo vaga comum");
				sessao.persist(vaga);
				
				System.out.println("\nCriando vaga conveniada");
				Vaga vaga2 = new Vaga();
				vaga2.setTipo(TipoVaga.CARRO);
				vaga2.setReserva(TipoReservaVaga.CONVENIO);
				vaga2.setOcupada(true);
				vaga2.setConvenio(convenio);
				
				System.out.println("Persistindo vaga conveniada");
				sessao.persist(vaga2);
				
				System.out.println("\nCriando cliente conveniado");
				Cliente cliente2 = new Cliente();
				cliente2.setNome("Teste");
				cliente2.setCpf("Teste");
				cliente2.setTelefone("Teste");
				cliente2.setTipo(TipoCliente.CONVENIADO);
				cliente2.setConvenio(convenio);
				cliente2.setStatus(true);
				
				System.out.println("Persistindo cliente conveniado");
				sessao.persist(cliente2);
				
				t.commit();
				
				System.out.println("Buscando usuário de id 1");
				Optional<Usuario> userOpt = userDao.getById(user.getId());
				userOpt.ifPresent(user2 -> {
					System.out.println(user2.toString());
				});
				
				System.out.println("Buscando TODOS os usuários");
				List<Usuario> users = userDao.getAll();
				for (Usuario u : users) {
					System.out.println(u.toString());
				}
				
				System.out.println("Alterando Usuário");
				Optional<Usuario> userAnt = userDao.getById(user.getId());
				userAnt.ifPresent(u -> {
					u.setNome("alterou!");
					userDao.update(u);
				});
				
				System.out.println("Removendo Usuário (NÃO PODE ENQUANTO TIVER CAIXAS)");
				userDao.delete(user.getId());
				
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
