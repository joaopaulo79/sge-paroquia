package com.paroquiaTeam.sgeParoquia.service;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.core.SessaoSistema;
import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosUsuario;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.utils.SenhaUtil;

import jakarta.persistence.EntityNotFoundException;

public class UsuarioService {
	UsuarioDAO dao = new UsuarioDAO();
	
	public boolean existePorId(Long id) {
		return dao.existsById(id);
	}
	
	public Optional<Usuario> buscarPorId(Long id) {
		return dao.getById(id);
	}
	
	public Usuario obterPorId(Long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o id "+id));
	}
	
	public Optional<Usuario> buscarPorLogin(String login) {
		return dao.getByLogin(login);
	}
	
	public Usuario obterPorLogin(String login) {
		return buscarPorLogin(login)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o login "+login));
	}
	
	public void salvar(DadosUsuario dados) {
		validar(dados);
		
		String senhaHash = SenhaUtil.hash(dados.senha());
		Usuario user = dados.paraUsuario(senhaHash);
		
		dao.save(user);
	}
	
	public void ativar(Long id) {		
		if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
			throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
		}
		
		Usuario user = obterPorId(id);
		user.setStatus(true);
		dao.update(user);
	}
	
	public void desativar(Long id) {
		if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
			throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
		}
		
		if (SessaoSistema.getInstancia().isIdDoUserLogado(id)) {
			throw new IllegalStateException("Usuário está logado atualmente. Não foi possível desativar");
		}
		
		Usuario user = obterPorId(id);
		user.setStatus(false);
		dao.update(user);
	}
	
	public void alterarSenha(Long id, String senhaNova) {
		if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
			throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
		}
		
		Usuario user = obterPorId(id);
		
		validarSenha(senhaNova, user.getLogin());
		
		if (SenhaUtil.isSenhaBate(senhaNova, user.getSenha())) {
			throw new IllegalArgumentException("Senha nova não pode ser igual à senha anterior");
		}
		
	}
	
	public void atualizar(Long id, DadosUsuario dadosNovos) {
		if (!SessaoSistema.getInstancia().isUserLogadoAdministrador()) {
			throw new IllegalStateException("Usuário logado não possuí permissão para realizar esta ação");
		}
		
		validar(dadosNovos);

		Usuario user = obterPorId(id);
		user.setNome(dadosNovos.nome());
		user.setLogin(dadosNovos.login());
		user.setTipo(dadosNovos.tipo());
		// Senha e status só podem ser mudados por seus métodos específicos
		
		dao.update(user);
	}
	
	public void excluir(Long id) {
		if (SessaoSistema.getInstancia().getUserLogado().getId() == id) {
			throw new IllegalStateException("Usuário está logado atualmente. Não foi possível excluir");
		}
		
		Usuario user = obterPorId(id);
		if (!user.getCaixas().isEmpty()) {
			throw new IllegalStateException("Usuário tem caixas. Não foi possível excluir");
		}
		
		dao.delete(id);
	}
	
	public boolean autenticar(String login, String senha) {
		Optional<Usuario> talvezUsuario = buscarPorLogin(login);
		if (talvezUsuario.isEmpty()) {
			return false;
		}
		return SenhaUtil.isSenhaBate(senha, talvezUsuario.get().getSenha());
	}
	
	private void validar(DadosUsuario dados) {
		if (dados.nome() == null || dados.nome().isBlank()) {
			throw new IllegalArgumentException("Nome é obrigatório");
		}
		
		if (dados.login() == null || dados.login().isBlank()) {
			throw new IllegalArgumentException("Login é obrigatório");
		}
		
		if (dados.senha() == null || dados.senha().isBlank()) {
			throw new IllegalArgumentException("Senha é obrigatória");
		}
		
		if (dados.tipo() == null) {
			throw new IllegalArgumentException("Tipo é obrigatório");
		}
		
		validarSenha(dados.senha(), dados.login());
	}
	
	private void validarSenha(String senha, String login) {
		if (SenhaUtil.validarRequisitosSenha(senha, login)) {
			throw new IllegalArgumentException("Senha não atende requisitos.");
		}
	}
}
