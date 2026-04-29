package com.paroquiaTeam.sgeParoquia;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.paroquiaTeam.sgeParoquia.database.HibernateUtil;
import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.Cliente;
import com.paroquiaTeam.sgeParoquia.model.Estadia;
import com.paroquiaTeam.sgeParoquia.model.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.TipoPagamento;
import com.paroquiaTeam.sgeParoquia.model.TipoUsuario;
import com.paroquiaTeam.sgeParoquia.model.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.model.Usuario;
import com.paroquiaTeam.sgeParoquia.model.Veiculo;

public class TestesDeEntidades {
	public static void testaEntidades() {
		try (Session sessao = HibernateUtil.getSessionFactory().openSession()){
			Transaction t = sessao.beginTransaction();
			try {
				System.out.println("\nCriando usuario");
				Usuario user = new Usuario();
				user.setNome("Teste");
				user.setLogin("Teste");
				user.setSenha("Teste");
				user.setTipo(TipoUsuario.ADMINISTRADOR);
				
				System.out.println("Persistindo usuario");
				sessao.persist(user);
				
				
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
			
			
				System.out.println("\nCriando movimento2");
				MovimentoCaixa movimento2 = new MovimentoCaixa();
				movimento2.setTipoMovimento(TipoMovimento.ENTRADA);
				movimento2.setFormaPagamento(TipoPagamento.CREDITO);
				movimento2.setCaixa(caixa);
				movimento2.setEstadia(estadia);
				movimento2.setValor(15);
			
				System.out.println("Persistindo movimento2");
				sessao.persist(movimento2);
			
				t.commit();
			} catch (Exception e) {
				System.out.println("Persistência falhou: " + e.getMessage());
				t.rollback();
			}
		}
	}
}
